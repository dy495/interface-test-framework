from datetime import datetime
from threading import Timer

import os
import base64
import hmac
from hashlib import sha256
import time
import requests
import uuid
import json
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import sys


BASE_URL = "http://api.winsenseos.com/retail/api/data/device"
if os.environ.get('EDGE_BASE_URL'):
    BASE_URL = os.environ.get('EDGE_BASE_URL')

app_id = "c30dcafc59c8"
if os.environ.get('EDGE_APP_ID'):
    BASE_URL = os.environ.get('EDGE_APP_ID')

uid = "uid_580f244a"
if os.environ.get('EDGE_UID'):
    BASE_URL = os.environ.get('EDGE_UID')

ak = "0d17651c55595b9b"
if os.environ.get('EDGE_AK'):
    BASE_URL = os.environ.get('EDGE_AK')

sk = "0ebe6128aedb44e0a7bd3f7a5378a7fc"
if os.environ.get('EDGE_SK'):
    BASE_URL = os.environ.get('EDGE_SK')

device_id = "7698197825586176"
if os.environ.get('EDGE_DEVICE_ID'):
    BASE_URL = os.environ.get('EDGE_DEVICE_ID')

router = "/scenario/picture/ANALYSIS_PICTURE/v1.0"

version = "1.0"

photo_url = "http://admin:winsense2018@192.168.50.152/ISAPI/Streaming/channels/1/picture"

if os.environ.get('EDGE_DEVICE_URL'):
    photo_url = os.environ.get('EDGE_DEVICE_URL')

interval = 60

if os.environ.get('EDGE_UPLOAD_INTERVAL'):
    interval = int(os.environ.get('EDGE_UPLOAD_INTERVAL'))


def username_password(url):
    tmp_url = photo_url[7:]
    idx1 = tmp_url.find(":")
    idx2 = tmp_url.find("@")
    username = tmp_url[0:idx1]
    password = tmp_url[idx1 + 1:idx2]
    return (username, password)


(photo_username, photo_password) = username_password(BASE_URL)


def encrypt_image(image_src):
    print(sk)
    password = sk[-8:] + sk[0:8]
    print(password)
    password = password.encode('utf-8')
    f = open(image_src, 'rb')
    text = f.read()
    padtext = pad(text, 16, style='pkcs7')

    model = AES.MODE_ECB
    aes = AES.new(password, model)

    en_text = aes.encrypt(padtext)
    # print(en_text)
    #f = open(image_src, 'rb')
    #text = f.read()
    en_text = base64.b64encode(en_text)
    return en_text


def get_sign(data, key):
    key = key.encode('utf-8')
    message = data.encode('utf-8')
    sign = base64.b64encode(hmac.new(key, message, digestmod=sha256).digest())
    sign = str(sign, 'utf-8')
    print(sign)
    return sign


def get_auth_token(timestamp, nonce):
    s1 = '.'
    li = [uid, app_id, ak, router, timestamp, nonce]
    sign_str = s1.join(li)
    token = get_sign(sign_str, sk)
    return token


def getImage(url, username, pwd, dst):
    cmd = "curl --insecure --anyauth -u %s:%s -X GET %s > %s" % (username, pwd, url, dst)
    print(cmd)
    os.system(cmd)


def uploadImage(src):
    nonce = "12345678"
    timestamp = str(round(time.time() * 1000))
    request_id = str(uuid.uuid4())

    enc_src = encrypt_image(src)
    # print(enc_src)
    # gateway request
    headers = {
        'request_id': request_id, 'nonce': nonce, 'timestamp': timestamp, 'ExpiredTime': '300',
        'Content-Type': 'application/json;charset=utf-8',
        'Authorization': get_auth_token(timestamp, nonce)
    }
    request_body = {
        "uid": uid,
        "app_id": app_id,
        "request_id": request_id,
        "device_id": device_id,
        "router": router,
        "data": {
            "resource": [str(enc_src, encoding="utf-8")],
            #"resource": ["utf-8"],
            "device_id": device_id,
            "resource_size": 1,
            "sec_key_path": [],
            "biz_data": {
                "time": timestamp,
                "pic_url": "@0",
                "image_data": {
                    "width": 1920,
                    "height": 1080
                }
            },
        }
    }

    print(request_body)

    response = requests.post(BASE_URL, json=request_body, headers=headers)
    if response.status_code == 200:
        response_data = json.loads(response.text)
        print(response_data)
    else:
        print(response["reason"])


def process(inc):
    t = Timer(inc, process, (inc,))
    t.start()
    print(datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    fn = "tmp.jpg"
    getImage(photo_url, photo_username, photo_password, fn)
    uploadImage(fn)


if __name__ == '__main__':
    uploadImage(sys.argv[1])
    # process(interval)

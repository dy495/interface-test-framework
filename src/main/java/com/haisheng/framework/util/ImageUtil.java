package com.haisheng.framework.util;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    /**
     * @Description:  8、获取图片的base64编码
     * @Param: [picPath]
     * @return: java.lang.String
     * @Author: Shine
     * @Date: 2019/4/9
     */
    public String getImageBinary(String picPath){
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        File f = new File(picPath);
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            return encoder.encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.haisheng.framework.util;

import com.google.common.io.Closer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReaderUtil {
    public String getProperty(String fileName, String key) throws IOException{
        Closer closer = Closer.create();
        Properties props = new Properties();
        String value = null;
        try {
            InputStream in = new FileInputStream(fileName);
            props.load(in);
            closer.register(in);
            value = props.getProperty(key);
        } catch (Exception e) {
            closer.rethrow(e);
        }finally {
            closer.close();
        }
        return value;
    }
}


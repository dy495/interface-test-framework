package com.haisheng.framework.util;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class GenTxt {
    @Test
    public void test() throws Exception {

        ArrayList<String> userId = new ArrayList();
        String dir = "C:\\Users\\Shine\\Desktop\\temp";

        for (int j = 0; j < 100000; j++) {
            Random random = new Random();
            int df = random.nextInt(88) + 11;
            userId.add("" +df);
        }
        String filename = dir + "\\" + "age.csv";
        FileUtils.writeLines(new File(filename ), userId, true);

    }
}

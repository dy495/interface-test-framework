package com.haisheng.framework.testng.service;

/**
 * @author yuhaisheng(Jason)
 * <Util>
 * CSV data provider for testng data seperation testing
 * @update
 *      yuhaisheng, 2017.08.17, initial vesrion
 **/



import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.collections.CollectionUtils;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CsvDataProvider {

    private static CSVReader reader = null;
    private static Class<?>[] parameterTypes;
    private static String[] last;

    @DataProvider(name = "CsvDataProvider", parallel = false)
    public static Iterator<Object[]> getDataProvider(Method method) throws IOException {
        List<Object[]> resultList = new ArrayList<>();

        String methodName = method.getName();
        List<String> fs = new ArrayList<>();
        if ("allTest".equals(methodName)) {
            fs = FileUtil.getCsvFilesByResources();
        } else {
            String dir = methodName.split("_")[0];
            String fileName = FileUtil.dirPlusPrefix + File.separator + dir + File.separator + method.getName().replace(dir + "_", "") + ".csv";
            fs.add(fileName);
        }
        parameterTypes = method.getParameterTypes();
        if (CollectionUtils.isNotEmpty(fs)) {
            for (String file : fs) {
                File csvFile = new File(file);
                if (csvFile.isFile()) {
                    String business = csvFile.getParentFile().getName();
                    String fileName = csvFile.getName().replace(".csv", "");
                    InputStream is = new FileInputStream(csvFile);
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    reader = new CSVReader(isr, ',', '\"', 0);
                    while (hasNext()) {
                        String[] next;
                        if (last != null) {
                            next = last;
                        } else {
                            next = getNextLine();
                        }
                        Object[] result = parseLine(next);
                        if (null != result[0] && ! result[0].toString().startsWith("#")) {
                            //remove comment-off line in csv file
                            resultList.add(result);
                        }
                    }
                }
            }
        }
        return resultList.iterator();
    }

    private static boolean hasNext() {
        try {
            last = reader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return last != null;
    }

    private static Object[] parseLine(String[] svals) {
        // @test method paramters' total number
        int testParamLen  = parameterTypes.length;
        int csvLineLength = svals.length;

        // get values in csv
        Object[] result = new Object[testParamLen];
        if (testParamLen <= csvLineLength) {
            for (int i=0; i<csvLineLength; i++) {
                result[i] = svals[i];
            }
        }

        return result;
    }

    private static String[] getNextLine() {
        if (last == null) {
            try {
                last = reader.readNext();
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return last;
    }
}

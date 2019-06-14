package com.haisheng.framework.testng.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class TestFilterListener implements IMethodInterceptor {

    private static Set<Pattern> patterns;
    private List<IMethodInstance> includeTest(String testsToInclude, List<IMethodInstance> methods) {
        List<IMethodInstance> matchList = new ArrayList<>();

        if(patterns==null) {
            patterns = new HashSet<>();
            String[] testPatterns = testsToInclude.split(",");
            for(String testPattern:testPatterns) {
                patterns.add(Pattern.compile(testPattern, Pattern.CASE_INSENSITIVE));
            }
        }

        try {
            for (IMethodInstance item : methods) {
                System.out.println("method: " + item.getMethod().getMethodName());
                for(Pattern pattern:patterns) {
                    if(pattern.matcher(item.getMethod().getMethodName()).find()) {
                        matchList.add(item);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }


        System.out.println("matchList.size() == " + matchList.size());
        if (matchList.size() == 0) {
            matchList = null;
        }
        return matchList;
    }

    @Override

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        String testNames = System.getProperty("test_names_to_include");

        if(testNames==null || testNames.trim().isEmpty()) {
            return methods;
        }else {
            System.out.println("run testNames: " + testNames);
            List<IMethodInstance> methodInstanceList = includeTest(testNames, methods);
            if(null != methodInstanceList && methodInstanceList.size() > 0) {
                return methodInstanceList;
            }else {
                return new ArrayList<IMethodInstance>();
            }
        }
    }
}

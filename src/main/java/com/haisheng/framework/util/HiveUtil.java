package com.haisheng.framework.util;

import java.util.List;
//import com.sankuai.data.talos.AsyncTalosClient;
//import com.sankuai.data.talos.exception.TalosException;
//import com.sankuai.data.talos.model.Engine;
//import com.sankuai.data.talos.model.QueryInfo;
//import com.sankuai.data.talos.model.QueryResult;



public class HiveUtil {

    public List<List<Object>> executeSql(String sql) {

//        //登录的账户（mis ID）和密码
//        String username = "it_talos.dataapp.ptqa";
//        String password = "ptqa+666";
//        AsyncTalosClient asyncTalosClient = new AsyncTalosClient(username, password);
//
//        List<List<Object>> result = null;
//
//        //必须开启session
//        try {
//            asyncTalosClient.openSession();
//            Engine engine = Engine.Hive;
//            String dsn = "hive";
//            //提交SQL查询，返回查询ID
//            String qid = asyncTalosClient.submit(engine, dsn, sql);
//            //等待查询结束
//            int res = asyncTalosClient.waitForFinished(qid);
//
//            if (res == 1) {
//                //查询成功，获取查询结果
//                QueryResult queryResult = asyncTalosClient.getQueryResult(qid);
//                result = queryResult.fetchAll();
//
//            } else if (res == -1) {
//                //查询失败，获取查询失败原因
//                QueryInfo queryInfo = asyncTalosClient.getQueryInfo(qid);
//                String errorMessage = queryInfo.getMessage();
//                System.out.println("Query failed, error: " + errorMessage);
//            } else {
//                //查询任务尚未结束
//                System.out.println("Query is running.");
//            }
//
//            //关闭session
//            asyncTalosClient.closeSession();
//            return  result;
//
//        } catch (TalosException e) {
//            e.printStackTrace();
//        }

        return null;
    }


}

package com.haisheng.framework.testng.bigScreen.crm.wm.util;

import com.alibaba.fastjson.JSONObject;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.HtmlPage;

public class ParseFileUtil {
    public static String test5(String resource) throws Exception {
        Parser myParser = new Parser(resource);
        //设置编码
        myParser.setEncoding("UTF-8");
        HtmlPage visitor = new HtmlPage(myParser);
        myParser.visitAllNodesWith(visitor);
        NodeList nodeList = visitor.getBody();
        SimpleNodeIterator simpleNodeIterator = nodeList.elements();
        simpleNodeIterator.nextNode();
        return nodeList.toString();
    }

    public static void main(String[] args) {
        NodeList content = ss("http://192.168.50.3/api-doc/business-jiaochen/pc/index.html");
        assert content != null;
        Node[] contents = content.toNodeArray();//只有一个
        Node[] sect1s = contents[0].getChildren().toNodeArray();

        System.out.println(JSONObject.toJSON(sect1s[0]));
        for(Node temp :sect1s ){
           /* Node[] sectionBodys = temp.getChildren().toNodeArray();
            Node[] sect2s = sectionBodys[1].getChildren().toNodeArray();
            Node[] paragraphs = sect2s[0].getChildren().toNodeArray();
            Node[] ps = paragraphs[1].getChildren().toNodeArray();
            Node[] text = ps[0].getLastChild().getChildren().toNodeArray();
            String[] strings = text[0].getText().split(";");
            String s = strings[1].replace(EnumTestProduce.JIAOCHEN_DAILY.getAddress(), "");
            System.err.println(s);*/
             System.out.println(temp.toString());
        }

    }

    public static NodeList ss(String htmlStr) {
//        List<String> mp3List = new ArrayList<>();
        try {
            //初始化Parser，这里要注意导入包为org.htmlparser。这里参数有很多。这个地方我写的是提前获取好的html文本。也可以传入URl对象
            Parser parser = new Parser(htmlStr);
            //设置编码机
            parser.setEncoding("utf-8");
            //通过filter找到div且div的id为songListWrapper
            AndFilter filter =
                    new AndFilter(
                            new TagNameFilter("div"),
                            new HasAttributeFilter("id", "content")
                    );
            return parser.parse(filter);//通过filter获取nodes
//            for (int i = 2; i < nodesli.length; i++) {
//                //System.out.println(nodesli[i].toHtml());
//                Node tempNode = nodesli[i];
//                TagNode tagNode = new TagNode();//通过TagNode获得属性，只有将Node转换为TagNode才能获取某一个标签的属性
//                tagNode.setText(tempNode.toHtml());
//                String claStr = tagNode.getAttribute("class");//claStr为bb-dotimg clearfix  song-item-hook { 'songItem': { 'sid': '113275822', 'sname': '我的要求不算高', 'author': '黄渤' } }
//                claStr = claStr.replaceAll(" ", "");
//                if (!claStr.contains("\\?")) {
//                    Pattern pattern = Pattern.compile("[\\s\\wa-z\\-]+\\{'songItem':\\{'sid':'([\\d]+)','sname':'([\\s\\S]*)','author':'([\\s\\S]*)'\\}\\}");
//                    Matcher matcher = pattern.matcher(claStr);
//                    if (matcher.find()) {
//                        Mp3 mp3 = new Mp3();
//                        mp3.setSid(matcher.group(1));
//                        mp3.setSname(matcher.group(2));
//                        mp3.setAuthor(matcher.group(3));
//                        mp3List.add(mp3);
            //for(int j=1;j<=matcher.groupCount();j++){
            //System.out.print("   "+j+"--->"+matcher.group(j));
            //}
//                    }
//                }
//                //System.out.println(matcher.find());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.linzi.daily.html.jsoup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.linzi.daily.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsoupHtml {
    public static void main(String[] args) throws IOException {
        String path = "F:\\self\\chm\\www.ahan.tk\\view\\10";
        List<File> fileList = FileUtils.visitAllFiles(path,"htm");
        File outTxt = new File(path+"\\比丘戒律.txt");
        List<String> txtList = new ArrayList<>();
        for(File file:fileList){
            Document doc = Jsoup.parse(file,"GBK");
            String content = CharSequenceUtil.EMPTY;
            //获取h2
            Elements h2s = doc.select("h2");
            if(!h2s.isEmpty()){
                content = h2s.get(0).text();
            }
            Element main1Div = doc.getElementById("main1");
            if(main1Div!=null){
                content = content+"\n原文："+main1Div.text();
            }
            Element main2Div = doc.getElementById("main2");
            if(main2Div!=null){
                content = content+"\n白话文："+main2Div.text();
            }
            // 获取所有 <DIV> 标签
//            Elements divs = doc.select("div");
//            if(!divs.isEmpty()){
//                content = content+"\n原文："+divs.get(0).text();
//            }
            txtList.add(content);
        }
        FileUtil.writeLines(txtList,outTxt, StandardCharsets.UTF_8);
    }
}

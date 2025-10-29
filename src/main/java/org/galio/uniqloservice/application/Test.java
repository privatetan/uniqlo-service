package org.galio.uniqloservice.application;

import java.io.File;
import java.io.IOException;
import java.util.*;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Test
 */
public class Test {
    public static void main(String[] args) {
        try {
            // 读取 HTML 文件
            File input = new File("/Users/privatetan/Desktop/CDM_LST.HTM"); // 替换为你的 HTML 文件路径
            Document doc = Jsoup.parse(input, "UTF-8");
            // 提取 <A NAME> 的值
            Elements aNameElements = doc.select(" h2 a[name]");
            for (Element aName : aNameElements) {
                System.out.println(aName.attr("name"));
            }

            System.out.println("------------ " );
            // 提取 <BLOCKQUOTE> 下的 <TABLE> 下的 <TABLE> 下的 <TR><TD> 的值
            List<String> values = new ArrayList<>();
            Elements blockquoteTables = doc.select("blockquote table tr td table tr td");
            for (Element td : blockquoteTables) {
                String trim = td.text().trim();
                if (containsDigits(trim)
                        || "CbTRUE".equals(trim)
                        || "CbFALSE".equals(trim)
                        || "CeIDLE_ELOAD_SOURCE_NONE".equals(trim)
                        || "CeIDLE_EstAmbtAirTemp".equals(trim)
                        || "CeIDLE_STEPGEN_NONE".equals(trim)) {
                    System.out.println(td.text());
                    values.add(td.text());
                }
            }

            System.out.println("aNameElements共计 " + aNameElements.size());
            System.out.println("values共计 " + values.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean containsDigits(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // 找到数字，返回 true
            }
        }
        return false; // 没有找到数字，返回 false
    }
}
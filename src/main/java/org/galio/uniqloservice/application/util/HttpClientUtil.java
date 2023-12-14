package org.galio.uniqloservice.application.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    public static String sendGETMessage(String url) {
        String result = "";

        try {
            URL requestUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "org/galio/uniqloservice/application/json");
            conn.setConnectTimeout(45000);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }

            in.close();
            result = sb.toString();
        } catch (Exception var7) {
            log.error("{}查询失败，失败原因{}", url, var7);
            var7.printStackTrace();
        }

        return result;
    }

    public static String sendPOSTMessage(String url, Map params) {
        String result = "";

        try {
            URL requestUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(45000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Gson gson = new Gson();
            String paramsString = gson.toJson(params);
            log.info("请求参数为{}", paramsString);
            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            dataOutputStream.write(paramsString.getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }

            in.close();
            result = sb.toString();
            log.info("请求参数为{}", result);
        } catch (Exception e) {
            log.error("{}查询失败，失败原因{}", url, e);
            e.printStackTrace();
        }

        return result;
    }

}

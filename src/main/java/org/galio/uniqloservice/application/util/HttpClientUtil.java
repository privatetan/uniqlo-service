package org.galio.uniqloservice.application.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil() {
    }

    /**
     * 发送GET请求
     * 
     * @param url       请求的URL地址
     * @param headerMap 请求头参数（可为null）
     * @return 响应内容字符串，若失败返回空字符串
     */
    public static String sendGETMessage(String url, Map<String, String> headerMap) {
        StringBuilder result = new StringBuilder();

        HttpURLConnection conn = null;
        try {
            // 创建URL对象
            URL requestUrl = new URL(url);
            // 打开连接
            conn = (HttpURLConnection) requestUrl.openConnection();
            // 设置请求方法为GET
            conn.setRequestMethod("GET");
            // 设置连接超时时间（毫秒）
            conn.setConnectTimeout(45000);

            // 设置请求头
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 获取响应码，判断是否请求成功
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 使用try-with-resources自动关闭流
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    // 逐行读取响应内容
                    while ((line = in.readLine()) != null) {
                        result.append(line).append(System.lineSeparator());
                    }
                }
            } else {
                // 非200响应，记录错误日志
                log.error("GET请求失败，URL: {}，响应码: {}", url, responseCode);
            }
        } catch (Exception e) {
            // 异常处理，记录日志
            log.error("GET请求异常，URL: {}，原因: {}", url, e.getMessage(), e);
        } finally {
            // 关闭连接
            if (conn != null) {
                conn.disconnect();
            }
        }

        return result.toString();
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
            dataOutputStream.write(paramsString.getBytes(StandardCharsets.UTF_8));
            dataOutputStream.flush();
            dataOutputStream.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
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

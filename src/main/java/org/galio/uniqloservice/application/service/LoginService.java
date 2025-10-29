package org.galio.uniqloservice.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.galio.uniqloservice.application.service.dto.LoginTokenDTO;
import org.galio.uniqloservice.application.util.HttpClientUtil;
import org.galio.uniqloservice.application.util.Url;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginService {

    public String getCode(String phone) {
        Map<String, Object> getLoginCode = new HashMap<>();
        getLoginCode.put("mobile", phone);
        getLoginCode.put("type", "QUICK_LOGIN");
        String result = HttpClientUtil.sendPOSTMessage(Url.GET_LOGIN_CODE + System.currentTimeMillis(), getLoginCode);
        log.info("获取验证码结果：{}", result);
        return "获取验证码成功";
    }

    public String codeLogin(String phone, String code) {
        Map<String, Object> codeLoginMap = new HashMap<>();
        codeLoginMap.put("ald", null);
        codeLoginMap.put("isCheckedPolicy", "Y");
        codeLoginMap.put("membershipAgreementVersion", "4");
        codeLoginMap.put("privacyPolicyVersion", "5");
        codeLoginMap.put("t", System.currentTimeMillis());
        codeLoginMap.put("mobileNumber", phone);
        codeLoginMap.put("msg", code);
        String result = HttpClientUtil.sendPOSTMessage(Url.CODE_LOGIN_IN, codeLoginMap);
        log.info("登录结果：{}", result);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        JsonElement msgCodeJson = jsonObject.get("msgCode");
        String msgCode = msgCodeJson.getAsString();
        if ("MSG_04".equals(msgCode)) {
            JsonElement resp = jsonObject.get("resp");
            JsonArray respAsJsonArray = resp.getAsJsonArray();
            log.info("respAsJsonArray：{}", respAsJsonArray);
            for (JsonElement element : respAsJsonArray) {
                LoginTokenDTO tokenDTO = gson.fromJson(element, LoginTokenDTO.class);
                log.info("Token:{}", tokenDTO);
            }
            return "登录成功";
        } else {
            return "登录失败";
        }
    }
}

package org.galio.uniqloservice.application.api;

import org.galio.uniqloservice.application.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({ "/login" })
public class LoginApi {

    @Autowired
    LoginService loginService;

    /**
     * 获取验证码
     *
     * @param phone 用户的电话号码，用于发送验证码
     * @return 返回生成的验证码字符串
     */
    @GetMapping({ "/getCode" })
    public String getCode(@RequestParam("phone") String phone) {
        return loginService.getCode(phone);
    }

    /**
     * 通过验证码进行登录，并保存token。
     * 
     * @param phone 用户手机号，作为登录标识。
     * @param code  用户输入的验证码，用于验证用户身份。
     * @return 返回登录结果，通常为一个token，用于后续请求的用户认证。
     */
    @PostMapping({ "/codeLogin" })
    public String codeLogin(@RequestParam("phone") String phone,
            @RequestParam("code") String code) {
        // 调用登录服务完成验证码登录，并返回登录结果
        return loginService.codeLogin(phone, code);
    }

}

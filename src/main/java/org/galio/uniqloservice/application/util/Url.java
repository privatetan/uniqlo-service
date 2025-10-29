package org.galio.uniqloservice.application.util;

/**
 * 链接
 */
public class Url {

    private Url() {
    }

    public static final String QUERYPRODUCTIDURL = "https://d.uniqlo.cn/p/hmall-sc-service/search/searchWithDescriptionAndConditions/zh_CN";
    /**
     * 登录验证码
     * 参数 t:时间戳
     */
    public static final String GET_LOGIN_CODE = "https://d.uniqlo.cn/p/hmall-sms-service/mobile/send/zh_CN?t=";

    /**
     * 验证码登录
     */
    public static final String CODE_LOGIN_IN = "https://d.uniqlo.cn/p/hmall-ur-service/login/quick/zh_CN";
}

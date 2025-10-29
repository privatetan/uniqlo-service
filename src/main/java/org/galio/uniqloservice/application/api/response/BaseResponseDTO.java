package org.galio.uniqloservice.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseDTO {
    /**
     * 状态码
     */
    private String code;
    /**
     * 状态信息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;
}

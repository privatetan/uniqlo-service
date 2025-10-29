package org.galio.uniqloservice.application.api.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeDTO {
    /**
     * 尺码名称
     */
    private String sizeValue;
    /**
     * 尺码代码
     */
    private String sizeCode;
    /**
     * 尺码ID
     */
    private String sizeNoSuffix;

}

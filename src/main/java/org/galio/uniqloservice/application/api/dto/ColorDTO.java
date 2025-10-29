package org.galio.uniqloservice.application.api.dto;


import lombok.*;

/**
 * 颜色数据传输对象 (DTO)
 *
 * <p>用于 API 层与服务层之间传递颜色相关信息。包含颜色名称、颜色编码（背景/字体色）、颜色ID 以及内部版本号等字段。</p>
 *
 * 字段说明：
 * - colorName: 颜色名称
 * - objectVersionNumber: 版本号/内部代码
 * - background: 颜色编码（例如：#F5BCC1）
 * - colorNo: 颜色 ID/编号
 * - fontColor: 字体颜色编码
 *
 * 作者: galio
 * 日期: 2025-10-29
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColorDTO {
    /**
     * 颜色名称
     */
    private String colorName;
    /**
     * 颜色代码
     */
    private String objectVersionNumber;
    /**
     * 颜色编码 #F5BCC1
     */
    private String background;
    /**
     * 颜色ID
     */
    private String colorNo;
    /**
     * 颜色代码
     */
    private String fontColor;

}

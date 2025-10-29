package org.galio.uniqloservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailRowsDTO {

    /** 商品ID */
    private String productId;
    /** 商品样式 */
    private String styleText;
    /** 启用标志 */
    private String enabledFlag;
    /** 价格变动 */
    private Integer varyPrice;
    /** 税率 */
    private Integer taxRate;
    /** 商品编码 */
    private String productCode;
    /** 尺码 */
    private String size;
    /** 尺码文本 */
    private String sizeText;
    /** 商品名称 */
    private String name;
    /** OMS SKU编码 */
    private String omsSkuCode;
    /** 款式 */
    private String style;
    /** 颜色编号 */
    private String colorNo;
    /** SKU ID */
    private Integer skuId;
    /** 尺码编号 */
    private String sizeNo;

}

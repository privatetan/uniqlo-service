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
    private String productId;
    private String styleText;
    private String enabledFlag;
    private Integer varyPrice;
    private Integer taxRate;
    private String productCode;
    private String size;
    private String sizeText;
    private String name;
    private String omsSkuCode;
    private String style;
    private String colorNo;
    private Integer skuId;
    private String sizeNo;

}

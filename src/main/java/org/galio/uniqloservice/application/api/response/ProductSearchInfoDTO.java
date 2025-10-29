package org.galio.uniqloservice.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchInfoDTO {
    /**
     * 6位短号
     */
    private String sixCode;

    /**
     * 商品描述ID
     * 例：u0000000054124
     */
    private String productCode;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String mainPic;
    /**
     * 商品价格
     */
    private String productPrice;
    /**
     * 商品链接
     */
    private String productLink;
    /**
     * 商品库存
     */
    private String productStock;

    /**
     * 初始价格
     */
    private String originPrice;
    /**
     * 当前价格
     */
    private String minVaryPrice;



    /**
     * 是否有库存
     */
    private String stock;

    /**
     * 库存颜色列表
     */
    private List<String> colorNums;
    /**
     * 尺寸列表
     */
    private List<String> size;


}

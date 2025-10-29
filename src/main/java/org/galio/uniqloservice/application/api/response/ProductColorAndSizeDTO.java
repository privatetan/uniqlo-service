package org.galio.uniqloservice.application.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.galio.uniqloservice.application.api.dto.ColorDTO;

import java.util.List;

/**
 * 商品描述DTO
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorAndSizeDTO {

    /**
     * 颜色
     */
    private List<ColorDTO> colorList;
    /**
     * 尺寸
     */
    private List<String> sizeList;

}

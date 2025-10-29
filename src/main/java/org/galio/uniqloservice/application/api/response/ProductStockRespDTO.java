package org.galio.uniqloservice.application.api.response;

import org.galio.uniqloservice.application.service.dto.ProductStockDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockRespDTO {

    private List<ProductStockDTO> bplList;

    private List<ProductStockDTO> skuList;

    private List<ProductStockDTO> ekuList;

}

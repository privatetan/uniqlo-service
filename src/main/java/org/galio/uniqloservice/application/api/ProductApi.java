package org.galio.uniqloservice.application.api;

import org.galio.uniqloservice.application.service.CommonService;
import org.galio.uniqloservice.application.service.ProductService;
import org.galio.uniqloservice.application.service.dto.PriceRangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/product"})
@CrossOrigin(
        // 允许所有来源的跨域请求
        origins = {"*"}
)
public class ProductApi {

    @Autowired
    ProductService productService;
    @Autowired
    CommonService commonService;

    /**
     * 获取商品库存
     *
     * @param code 商品描述ID
     * @return 商品库存
     */
    @GetMapping({"/get/{code}"})
    public String getProductStockByCode(@PathVariable("code") String code) {
        return productService.getProductStockByCode(code);
    }

    /**
     * 校验商品是否有库存
     *
     * @param code 商品描述ID
     * @return true:有库存 false:无库存
     */
    @GetMapping({"/check/{code}"})
    public boolean checkStocks(@PathVariable("code") String code) {
        return productService.checkStocks(code);
    }


    @GetMapping({"/get2/{code}"})
    public void get2(@PathVariable("code") String code) {
        List<String> colorList = new ArrayList<>();
      //  colorList.add("灰色系");
      //  colorList.add("米色系");
        List<String> sizeList = new ArrayList<>();
      //  sizeList.add("XS");
      //  sizeList.add("S");
        PriceRangeDTO priceRangeDTO = new PriceRangeDTO();
        priceRangeDTO.setLow(0);
        priceRangeDTO.setHigh(0);
        commonService.getProductIdByCode(code, colorList, sizeList, priceRangeDTO);
    }
}


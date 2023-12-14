package org.galio.uniqloservice.application.api;

import org.galio.uniqloservice.application.api.response.ProductStockRespDTO;
import org.galio.uniqloservice.application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/product"})
@CrossOrigin(
        origins = {"*"}
)
public class ProductApi {
    @Autowired
    ProductService productService;

    @GetMapping({"/get/{descriptionId}"})
    public ProductStockRespDTO method(@PathVariable("descriptionId") String descriptionId) {
        return this.productService.method(descriptionId);
    }

}


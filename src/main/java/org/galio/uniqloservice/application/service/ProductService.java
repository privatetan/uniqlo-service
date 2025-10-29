package org.galio.uniqloservice.application.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.galio.uniqloservice.application.api.response.ProductStockRespDTO;
import org.galio.uniqloservice.application.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@Slf4j
public class ProductService {

    @Autowired
    CommonService commonService;

    /**
     * 根据商品描述ID查询库存
     *
     * @param descriptionId 商品描述ID
     * @return 库存信息
     */
    public ProductStockRespDTO getProductStockByDescriptionIdOld(String descriptionId) {
        // 1.根据6位短号查询productId
        String productId = commonService.getProductIdByCode(descriptionId);
        if (productId == null) {
            log.error("根据6位短号查询productId失败");
            return null;
        }
        // 2.根据productId查询库存
        StockRespDTO stock = commonService.getStockByProductId(productId);
        if (stock == null) {
            log.error("根据6位短号查询productId失败");
            return null;
        }
        Gson gson = new Gson();
        List<Map<String, Object>> respList = stock.getResp();
        Map<String, Object> stockListMap = respList.get(0);
        log.info("respList:{}", gson.toJson(respList));
        String hasStock = (String) stockListMap.get("hasStock");
        Map<String, Double> bplStocks = (Map<String, Double>) stockListMap.get("bplStocks");
        Map<String, Double> skuStocks = (Map<String, Double>) stockListMap.get("skuStocks");
        Map<String, Double> expressSkuStocks = (Map<String, Double>) stockListMap.get("expressSkuStocks");
        log.info("{}库存详情如下：", descriptionId);
        List<ProductStockDTO> bplList = new ArrayList();
        List<ProductStockDTO> skuList = new ArrayList();
        List<ProductStockDTO> ekuList = new ArrayList();

        List<DetailRowsDTO> detailRowsListNew = commonService.getDetailByProductId(productId);
        if (detailRowsListNew == null) {
            log.error("根据productId查询商品详情失败");
            return null;
        }
        Iterator var25 = detailRowsListNew.iterator();

        while (var25.hasNext()) {
            DetailRowsDTO rows = (DetailRowsDTO) var25.next();
            bplList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double) bplStocks.get(rows.getProductId())).build());
            skuList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double) skuStocks.get(rows.getProductId())).build());
            ekuList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double) expressSkuStocks.get(rows.getProductId())).build());
            log.info("门店急送：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), bplStocks.get(rows.getProductId())});
            log.info("门店自提：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), skuStocks.get(rows.getProductId())});
            log.info("快递配送：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), expressSkuStocks.get(rows.getProductId())});
            log.info("快递配送：颜色：{}，尺码：{}，数量：{}", new Object[]{"√", rows.getSize(), expressSkuStocks.get(rows.getProductId())});
        }

        ProductStockRespDTO respDTO = ProductStockRespDTO.builder().bplList(bplList).skuList(skuList).ekuList(ekuList).build();
        log.info("返回结果：{}", gson.toJson(respDTO));
        return respDTO;

    }

    /**
     * 根据商品描述ID查询库存
     *
     * @param code 商品描述ID
     * @return 库存信息
     */
    public String getProductStockByCode(String code) {
        // 1.根据6位短号查询productId
        String productId = commonService.getProductIdByCode(code);
        if (productId == null) {
            log.error("根据6位短号查询productId失败");
            return null;
        }
        // 2.根据productId查询库存
        StockRespDTO stockRespDTO = commonService.getStockByProductId(productId);
        if (stockRespDTO == null) {
            log.error("根据6位短号查询productId失败");
            return null;
        }
        Gson gson = new Gson();
        List<Map<String, Object>> respList = stockRespDTO.getResp();
        Map<String, Object> stockListMap = respList.get(0);
        log.info("respList:{}", gson.toJson(respList));
        return gson.toJson(stockListMap);

    }


    /**
     * 校验商品是否有库存
     *
     * @param code 商品描述ID
     * @return true:有库存 false:无库存
     */
    public boolean checkStocks(String code) {
        // 1.根据6位短号查询productId
        String productId = commonService.getProductIdByCode(code);
        if (productId == null) {
            log.error("根据6位短号查询productId失败");
            return false;
        }
        // 2.根据productId查询库存
        StockRespDTO stockRespDTO = commonService.getStockByProductId(productId);
        if (stockRespDTO == null) {
            log.error("根据6位短号查询productId失败");
            return false;
        }
        Gson gson = new Gson();
        List<Map<String, Object>> respList = stockRespDTO.getResp();
        Map<String, Object> stockListMap = respList.get(0);
        log.info("respList:{}", gson.toJson(respList));
        String hasStock = (String) stockListMap.get("hasStock");
        return "Y".equals(hasStock);
    }
}

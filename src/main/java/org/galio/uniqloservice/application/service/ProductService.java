package org.galio.uniqloservice.application.service;

import org.galio.uniqloservice.application.api.response.ProductStockRespDTO;
import org.galio.uniqloservice.application.service.dto.*;
import org.galio.uniqloservice.application.util.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductStockRespDTO method(String descriptionId) {
        String queryProductIdUrl = "https://d.uniqlo.cn/p/hmall-sc-service/search/searchWithDescriptionAndConditions/zh_CN";
        Map<String, Object> queryProductIdMap = new HashMap();
        queryProductIdMap.put("belongTo", "pc");
        queryProductIdMap.put("description", descriptionId);
        queryProductIdMap.put("insiteDescription", "");
        queryProductIdMap.put("pageInfo", PageInfoDTO.builder().page(1).pageSize(24).withSideBar("Y").build());
        queryProductIdMap.put("priceRange", PriceRangeDTO.builder().low(0).high(0).build());
        queryProductIdMap.put("rank", "overall");
        queryProductIdMap.put("searchFlag", true);
        String result = HttpClientUtil.sendPOSTMessage(queryProductIdUrl, queryProductIdMap);
        log.info(result);
        if (!result.contains("u00000000")) {
            return null;
        } else {
            int index = result.indexOf("u00000000");
            log.info("第一次出现的位置{}", index);
            log.info("根据6位短号查询productId:{}", result.substring(index, index + 14));
            String var10000 = result.substring(index, index + 14);
            String url = "https://www.uniqlo.cn/data/products/spu/zh_CN/" + var10000 + ".json";
            String info = HttpClientUtil.sendGETMessage(url);
            log.info("商品详细信息：{}", info);
            Gson gson = new Gson();
            JsonObject jsonObject = (JsonObject)gson.fromJson(info, JsonObject.class);
            JsonElement jsonElement = jsonObject.get("rows");
            List<DetailRowsDTO> detailRowsListNew = new ArrayList();
            JsonArray asJsonArray = jsonElement.getAsJsonArray();
            Iterator var13 = asJsonArray.iterator();

            while(var13.hasNext()) {
                JsonElement element = (JsonElement)var13.next();
                detailRowsListNew.add((DetailRowsDTO)gson.fromJson(element, DetailRowsDTO.class));
            }

            String stockUrl = "https://d.uniqlo.cn/p/stock/stock/query/zh_CN";
            Map<String, Object> stockMap = new HashMap();
            stockMap.put("distribution", "EXPRESS");
            stockMap.put("productCode", result.substring(index, index + 14));
            stockMap.put("type", "DETAIL");
            String stockResult = HttpClientUtil.sendPOSTMessage(stockUrl, stockMap);
            log.info(stockResult);
            StockRespDTO stockRespDTO = (StockRespDTO)gson.fromJson(stockResult, StockRespDTO.class);
            log.info(gson.toJson(stockRespDTO));
            List<Map<String, Object>> respList = stockRespDTO.getResp();
            Map<String, Object> stockListMap = (Map)respList.get(0);
            Map<String, Double> bplStocks = (Map)stockListMap.get("bplStocks");
            Map<String, Double> skuStocks = (Map)stockListMap.get("skuStocks");
            Map<String, Double> expressSkuStocks = (Map)stockListMap.get("expressSkuStocks");
            log.info("{}库存详情如下：", descriptionId);
            List<ProductStockDTO> bplList = new ArrayList();
            List<ProductStockDTO> skuList = new ArrayList();
            List<ProductStockDTO> ekuList = new ArrayList();
            Iterator var25 = detailRowsListNew.iterator();

            while(var25.hasNext()) {
                DetailRowsDTO rows = (DetailRowsDTO)var25.next();
                bplList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double)bplStocks.get(rows.getProductId())).build());
                skuList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double)skuStocks.get(rows.getProductId())).build());
                ekuList.add(ProductStockDTO.builder().size(rows.getSize()).style(rows.getStyle()).stock((Double)expressSkuStocks.get(rows.getProductId())).build());
                log.info("门店急送：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), bplStocks.get(rows.getProductId())});
                log.info("门店自提：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), skuStocks.get(rows.getProductId())});
                log.info("快递配送：颜色：{}，尺码：{}，数量：{}", new Object[]{rows.getStyle(), rows.getSize(), expressSkuStocks.get(rows.getProductId())});
                log.info("快递配送：颜色：{}，尺码：{}，数量：{}", new Object[]{"√", rows.getSize(), expressSkuStocks.get(rows.getProductId())});
            }

            ProductStockRespDTO respDTO = ProductStockRespDTO.builder().bplList(bplList).skuList(skuList).ekuList(ekuList).build();
            return respDTO;
        }
    }
}

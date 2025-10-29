package org.galio.uniqloservice.application.service;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.galio.uniqloservice.application.api.dto.ColorDTO;
import org.galio.uniqloservice.application.api.dto.SizeDTO;
import org.galio.uniqloservice.application.service.dto.DetailRowsDTO;
import org.galio.uniqloservice.application.service.dto.PageInfoDTO;
import org.galio.uniqloservice.application.service.dto.PriceRangeDTO;
import org.galio.uniqloservice.application.service.dto.StockRespDTO;
import org.galio.uniqloservice.application.util.HttpClientUtil;
import org.galio.uniqloservice.application.util.Url;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CommonService {

    /**
     * 根据6位短号查询productId
     *
     * @param code 6位短号
     * @return productId
     */
    public String getProductIdByCode(String code) {
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("belongTo", "pc");
        codeMap.put("description", code);
        codeMap.put("insiteDescription", "");
        codeMap.put("pageInfo", PageInfoDTO.builder().page(1).pageSize(24).withSideBar("Y").build());
        codeMap.put("priceRange", PriceRangeDTO.builder().low(0).high(0).build());
        codeMap.put("rank", "overall");
        codeMap.put("searchFlag", true);
        String result = HttpClientUtil.sendPOSTMessage(Url.QUERYPRODUCTIDURL, codeMap);
        log.info("根据6位短号查询productId查询结果：{}", result);
        if (!result.contains("u00000000")) {
            return null;
        }
        int index = result.indexOf("u00000000");
        return result.substring(index, index + 14);
    }

    /**
     * 根据productId查询商品详情
     *
     * @param productId 商品ID
     * @return 商品详情
     */
    public List<DetailRowsDTO> getDetailByProductId(String productId) {
        String url = "https://www.uniqlo.cn/data/products/spu/zh_CN/" + productId + ".json";
        String info = HttpClientUtil.sendGETMessage(url, null);
        log.info("查询后返回的商品详细信息：{}", info);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(info, JsonObject.class);
        JsonElement jsonElement = jsonObject.get("rows");
        List<DetailRowsDTO> detailRowsList = new ArrayList<>();
        JsonArray asJsonArray = jsonElement.getAsJsonArray();
        for (JsonElement element : asJsonArray) {
            detailRowsList.add(gson.fromJson(element, DetailRowsDTO.class));
        }
        log.info("处理后的商品详细信息：{}", gson.toJson(detailRowsList));
        return detailRowsList;
    }

    /**
     * 根据商品ID查询库存
     *
     * @param productId 商品ID
     * @return 库存信息
     */
    public StockRespDTO getStockByProductId(String productId) {
        String stockUrl = "https://d.uniqlo.cn/p/stock/stock/query/zh_CN";
        Map<String, Object> stockMap = new HashMap<>();
        stockMap.put("distribution", "EXPRESS");
        stockMap.put("productCode", productId);
        stockMap.put("type", "DETAIL");
        String stockResult = HttpClientUtil.sendPOSTMessage(stockUrl, stockMap);
        log.info("查询库存结果：{}", stockResult);
        Gson gson = new Gson();
        StockRespDTO stockRespDTO = gson.fromJson(stockResult, StockRespDTO.class);
        log.info("库存查询结果Object:{}", gson.toJson(stockRespDTO));
        return stockRespDTO;
    }

    /**
     * 根据6位短号、颜色、尺码和价格区间查询商品信息，并解析颜色和尺码列表
     *
     * @param code       6位短号
     * @param colorList  颜色过滤条件
     * @param sizeList   尺码过滤条件
     * @param priceRange 价格区间
     */
    public void getProductIdByCode(String code, List<String> colorList, List<String> sizeList, PriceRangeDTO priceRange) {
        // 1. 构建请求参数
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("belongTo", "pc");
        codeMap.put("description", code);
        codeMap.put("insiteDescription", code);
        codeMap.put("color", colorList);
        codeMap.put("size", sizeList);
        codeMap.put("priceRange", priceRange);
        codeMap.put("rank", "overall");
        codeMap.put("searchFlag", true);
        codeMap.put("pageInfo", PageInfoDTO.builder().page(1).pageSize(24).withSideBar("Y").build());

        // 2. 发送POST请求获取商品信息
        String result = HttpClientUtil.sendPOSTMessage(Url.QUERYPRODUCTIDURL, codeMap);
        log.info("根据6位短号查询productId查询结果：{}", result);

        // 3. 解析返回的JSON字符串
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(result);
        } catch (JsonSyntaxException e) {
            log.error("返回结果不是合法的JSON格式: {}", result, e);
            return;
        }
        if (!jsonElement.isJsonObject()) {
            log.error("返回结果不是JSON对象: {}", result);
            return;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // 4. 获取resp数组并做健壮性检查
        JsonArray respArray = jsonObject.getAsJsonArray("resp");
        if (respArray == null || respArray.size() < 2) {
            log.info("resp数组为空或长度不足，原始返回: {}", result);
            return;
        }

        // 5. 获取库存信息数组（resp[1]）
        JsonArray stockArray = respArray.get(1).getAsJsonArray();
        if (stockArray == null || stockArray.size() == 0) {
            log.info("暂无库存");
            return;
        }

        // 6. 解析颜色和尺码信息（resp[0]）
        List<SizeDTO> sizeDTOList = new ArrayList<>();
        List<ColorDTO> colorDTOList = new ArrayList<>();
        JsonArray attrArray = respArray.get(0).getAsJsonArray();
        for (JsonElement attrElement : attrArray) {
            JsonObject attrObject = attrElement.getAsJsonObject();
            String name = attrObject.get("name").getAsString();
            if ("尺码".equals(name)) {
                // 解析尺码信息
                JsonArray itemArray = attrObject.getAsJsonArray("item");
                if (itemArray != null && itemArray.size() > 0) {
                    JsonArray sizeArray = itemArray.get(0).getAsJsonArray();
                    for (JsonElement sizeEle : sizeArray) {
                        JsonObject sizeObj = sizeEle.getAsJsonObject();
                        SizeDTO sizeDTO = SizeDTO.builder()
                                .sizeValue(sizeObj.get("sizeValue").getAsString())
                                .sizeCode(sizeObj.get("sizeCode").getAsString())
                                .sizeNoSuffix(sizeObj.get("sizeNoSuffix").getAsString())
                                .build();
                        sizeDTOList.add(sizeDTO);
                    }
                }
                log.info("解析到尺码列表：{}", new Gson().toJson(sizeDTOList));
            } else if ("颜色".equals(name)) {
                // 解析颜色信息
                JsonArray itemArray = attrObject.getAsJsonArray("item");
                if (itemArray != null) {
                    for (JsonElement colorEle : itemArray) {
                        JsonObject colorObj = colorEle.getAsJsonObject();
                        ColorDTO colorDTO = ColorDTO.builder()
                                .colorName(colorObj.get("colorName").getAsString())
                                .objectVersionNumber(colorObj.get("objectVersionNumber").getAsString())
                                .background(colorObj.get("background").getAsString())
                                .colorNo(colorObj.get("colorNo").getAsString())
                                .fontColor(colorObj.get("fontColor").getAsString())
                                .build();
                        colorDTOList.add(colorDTO);
                    }
                }
                log.info("解析到颜色列表：{}", new Gson().toJson(colorDTOList));
            }
        }
        // 7. 可根据需要返回颜色和尺码列表，当前仅日志输出
    }
}
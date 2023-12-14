package org.galio.uniqloservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRespDTO {
    private String msgCode;
    private String msg;
    private List<Map<String, Object>> resp;
}

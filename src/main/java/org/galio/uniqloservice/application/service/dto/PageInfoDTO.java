package org.galio.uniqloservice.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageInfoDTO {
    private Integer page;
    private Integer pageSize;
    private String withSideBar;

}

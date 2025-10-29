package org.galio.uniqloservice.application.service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoginTokenDTO {
    private String access_token;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String env;
    private String expires_in;
}

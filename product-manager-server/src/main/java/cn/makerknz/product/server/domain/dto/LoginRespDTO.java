package cn.makerknz.product.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 朱康南
 * @Date: 2021/5/31/031 17:11
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRespDTO {

    /**
     * 用户信息
     */
    private UserRespDTO user;

    /**
     * token
     */
    private JwtTokenRespDTO token;

}

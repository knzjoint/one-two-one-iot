package cn.makerknz.product.server.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 朱康南
 * @Date: 2021/6/10/010 13:58
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "UserLoginDTO", description = "登录信息")
public class UserLoginDTO {

    @Schema(name = "phone", description = "手机号")
    private String phone;

    /**
     * 用户密码，加密
     */
    @Schema(name = "password", description = "密码")
    private String password;

}

package cn.makerknz.product.server.domain.vo;

import cn.makerknz.product.server.domain.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 朱康南
 * @Date: 2021/6/2/002 11:41
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "UserInfoVO", description = "用户信息")
public class UserInfoVO {

    @Schema(name = "id", description = "用户ID")
    private Integer id;

    @Schema(name = "username", description = "昵称")
    private String username;

    /**
     * 真实姓名
     */
    @Schema(name = "truename", description = "真实姓名")
    private String truename;

    /**
     * 角色0-管理员,1-普通用户,2-工作人员
     */
    @Schema(name = "role", description = "角色")
    private RoleEnum role;

    @Schema(name = "avatar", description = "图标")
    private String avatar;

}

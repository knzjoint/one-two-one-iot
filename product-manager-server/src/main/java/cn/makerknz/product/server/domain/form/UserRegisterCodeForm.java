package cn.makerknz.product.server.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author: maker_knz
 * @Date: 2022/3/3/003 16:23
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterCodeForm {

    @NotBlank(message = "手机号不能为空")
    private String phone;

}

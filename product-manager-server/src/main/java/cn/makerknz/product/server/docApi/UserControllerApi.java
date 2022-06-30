package cn.makerknz.product.server.docApi;

import cn.makerknz.product.server.domain.dto.UserLoginDTO;
import cn.makerknz.product.server.domain.form.UserForm;
import cn.makerknz.product.server.domain.form.UserRegisterCodeForm;
import cn.makerknz.product.server.domain.form.UserRegisterForm;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.domain.vo.UserInfoVO;
import cn.makerknz.product.server.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Tag(name = "UserControllerApi", description = "用户的增删改查登录注册")
public interface UserControllerApi {

    @Operation(summary = "添加登录",
            description = "通过手机号密码登录",
//            parameters = {
//                    @Parameter(name = "name", description = "姓名")
//            },
            responses = {
                    @ApiResponse(description = "返回添加的用户",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserLoginDTO.class))),
                    @ApiResponse(responseCode = "400", description = "返回400时候错误的原因")}
    )
    ResultVO login(@RequestBody UserLoginDTO userLoginDTO);

    ResultVO logout();


    ResultVO<UserInfoVO> userInfo(HttpServletRequest httpServletRequest);

    User getUser(@PathVariable("id") Integer userId);

    ResultVO updateUser(@PathVariable("id") Integer id, @RequestBody UserForm userForm);

    ResultVO addUser(@RequestBody UserForm userForm);

    ResultVO deleteUser(@PathVariable("id") Integer id);

    ResultVO getWatiers();

    String genToken();



    ResultVO registerUser(@RequestBody @Valid UserRegisterForm userForm, BindingResult result);

    ResultVO changePassword(@RequestBody UserRegisterForm userForm);

    ResultVO registerCode(@RequestBody UserRegisterCodeForm userCodeForm);

    ResultVO changePasswordCode(@RequestBody UserRegisterForm userForm);

}

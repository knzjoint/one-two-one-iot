package cn.makerknz.product.server.controller;

import cn.makerknz.product.server.annotation.CheckThirdApplicationAccess;
import cn.makerknz.product.server.exception.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maker_knz
 * @version 1.0
 * @date 2022/4/11 7:48
 */
@RestController
@RequestMapping("/open-api")
public class OpenApiController {

    @GetMapping("/connected-test")
    @CheckThirdApplicationAccess
    ResultVO connectedTest() {
        return ResultVO.success();
    }

}

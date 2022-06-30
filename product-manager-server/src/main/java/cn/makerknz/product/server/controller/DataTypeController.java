package cn.makerknz.product.server.controller;


import cn.makerknz.product.server.annotation.CheckLogin;
import cn.makerknz.product.server.entity.DataType;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.service.IDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author maker knz
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/data-type")
public class DataTypeController {

    @Autowired
    private IDataTypeService dataTypeService;

    @GetMapping("/list")
    @CheckLogin
    ResultVO getDataTypeList() {
        List<DataType> dataTypeList = dataTypeService.list();
        return ResultVO.success(dataTypeList);
    }

}

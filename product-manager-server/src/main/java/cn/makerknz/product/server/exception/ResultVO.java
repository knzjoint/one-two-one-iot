package cn.makerknz.product.server.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @Author: maker_knz
 * @Date: 2021/4/19/019 14:30
 * @Version 1.0
 */

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Schema(name = "ResultVO", description = "通用返回对象")
public class ResultVO<T> {

    /**
     * 状态码
     */
    @Schema(name = "code", description = "状态码")
    private Integer code;

    /**
     * 描述
     */
    @Schema(name = "msg", description = "提示信息")
    private String msg;

    /**
     * 数据
     */
    @Schema(name = "data", description = "数据封装")
    private T data;

    /**
     * 构造函数
     * @param code
     * @param msg
     */
    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResultVO(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 成功返回
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ResultVO<T> successMsg(String msg) {
        return new ResultVO<>(ExceptionEnum.SUCCESS.getCode(), msg);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(ExceptionEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMsg());
    }

    public static <T> ResultVO<T> error(ExceptionEnum exceptionEnum) {
        return new ResultVO<>(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public static <T> ResultVO<T> error(ExceptionEnum exceptionEnum, String msg) {
        return new ResultVO<>(exceptionEnum.getCode(), msg);
    }

    public static <T> ResultVO<T> error(String msg) {
        return ResultVO.error(ExceptionEnum.INTERNAL_SERVER_ERROR, msg);
    }

    public static <T> ResultVO<T> error(Integer code, String msg) {
        return new ResultVO<>(code, msg);
    }

    public static <T> ResultVO<T> error(ExceptionEnum exceptionEnum, BindingResult bindingResult) {
        return new ResultVO<>(exceptionEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }

    public static <T> ResultVO<T> errorInfoToString(Exception e) {
        return new ResultVO<>(500, e.getMessage());
    }
}
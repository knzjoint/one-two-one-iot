package cn.makerknz.product.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 朱康南
 * @Date: 2021/6/1/001 9:24
 * @Version 1.0
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ResultVO> error(SecurityException e) {
        log.warn("发生了SecurityException异常！");
        e.printStackTrace();
        return new ResponseEntity<ResultVO>(
                ResultVO.error(ExceptionEnum.INTERNAL_SERVER_ERROR),
                HttpStatus.UNAUTHORIZED
        );
    }


    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultVO exceptionHandler(BusinessException e) {
        log.error(e.getErrorMsg());
        e.printStackTrace();
        return ResultVO.error(e.getCode(), e.getErrorMsg());
    }

    /**
     * 未知异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO exceptionHandler(Exception e) {
        // 把错误信息输入到日志中
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultVO.error(ExceptionEnum.UNKNOWN.getCode(),
                ExceptionEnum.UNKNOWN.getMsg());
    }

//    /**
//     * 错误页面异常
//     */
//    @ExceptionHandler(value = ErrorPageException.class)
//    @ResponseBody
//    public ResultVO exceptionHandler(ErrorPageException e) {
//        log.error(ErrorUtil.errorInfoToString(e));
//        return ApiResponse.error(e.getCode(), e.getErrorMsg());
//    }

    /**
     * 空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultVO exceptionHandler(NullPointerException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultVO.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

}

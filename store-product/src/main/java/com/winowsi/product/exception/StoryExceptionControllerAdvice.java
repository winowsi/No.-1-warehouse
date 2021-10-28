package com.winowsi.product.exception;

import com.winowsi.common.exception.BizCodeExceptionEnume;
import com.winowsi.common.utils.R;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @description:
 * @Author:ZaoYao
 * @Time: 2021/10/8 12:58
 */
@RestControllerAdvice
public class StoryExceptionControllerAdvice {
    /**
     *
     * @param e
     * @return 数据异常统一处理
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        HashMap<String, String> map = new HashMap<>(100);
        e.getBindingResult().getFieldErrors().forEach((item)->{
            String field = item.getField();
            String defaultMessage = item.getDefaultMessage();
            map.put(field,defaultMessage);
        });
        return R.error(BizCodeExceptionEnume.VALID_EXCEPTION.getCode(),BizCodeExceptionEnume.VALID_EXCEPTION.getMsg()).put("data",map);
    }

    /**
     * @description:处理任意类型的异常
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        System.out.println(throwable);
        return R.error(BizCodeExceptionEnume.UNKNOWN_EXCEPTION.getCode(),BizCodeExceptionEnume.UNKNOWN_EXCEPTION.getMsg());
    }

}

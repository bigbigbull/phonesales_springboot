package com.lck.exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
/**
 * describe:异常处理，主要是在处理删除父类信息的时候，因为外键约束的存在，而导致违反约束。返回给前台
 *
 * @author lichangkai
 * @date 2018/11/29
 */
@RestController
@ControllerAdvice
public class GloabalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(Exception e) throws Exception {
        e.printStackTrace();
        Class constraintViolationException = Class.forName("org.hibernate.exception.ConstraintViolationException");
        if(null!=e.getCause()  && constraintViolationException==e.getCause().getClass()) {
            return "须先删除其他关联数据才删除";
        }
        return e.getMessage();
    }

}
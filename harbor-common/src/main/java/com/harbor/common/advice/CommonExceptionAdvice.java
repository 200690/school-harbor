package com.harbor.common.advice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harbor.common.exception.BadRequestException;
import com.harbor.common.exception.CommonException;
import com.harbor.common.exception.DbException;
import com.harbor.common.result.Result;
import com.harbor.common.utils.WebUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

import java.net.BindException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {

    @ExceptionHandler(DbException.class)
    public Object handleDbException(DbException e) {
        log.error("mysql数据库操作异常 -> ", e);
        return processResponse(e);
    }

    @ExceptionHandler(CommonException.class)
    public Object handleBadRequestException(CommonException e) {
        log.error("自定义异常 -> {} , 异常原因：{}  ",e.getClass().getName(), e.getMessage());
        log.debug("", e);
        return processResponse(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return processResponse(new BadRequestException(msg));
    }
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e) {
        log.error("请求参数绑定异常 ->BindException， {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求参数格式错误"));
    }

    @ExceptionHandler(NestedServletException.class)
    public Object handleNestedServletException(NestedServletException e) {
        log.error("参数异常 -> NestedServletException，{}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求参数处理异常"));
    }

    @ExceptionHandler(Exception.class)
    public Object handleRuntimeException(Exception e) {
        if (e instanceof FeignException) {
            FeignException fe = (FeignException) e;
            String responseBody = fe.contentUTF8();
            String errorMsg = "服务调用失败";
            int statusCode = fe.status(); // HTTP 状态码，如 403

            try {
                ObjectMapper mapper = new ObjectMapper(); // 如果已有注入的 ObjectMapper，建议使用注入的
                JsonNode root = mapper.readTree(responseBody);
                if (root.has("msg")) {
                    errorMsg = root.get("msg").asText(); // 提取原始 msg
                }
                // 可选：从响应体中提取 code 覆盖 statusCode
                // if (root.has("code")) {
                //     statusCode = root.get("code").asInt();
                // }
            } catch (Exception ex) {
                log.warn("解析 Feign 异常响应体失败，原始响应体: {}", responseBody, ex);
                errorMsg = "服务调用失败: " + fe.getMessage(); // 回退
            }

            return processResponse(new CommonException(errorMsg, statusCode));
        }

        log.error("其他异常 uri : {} -> ", WebUtils.getRequest().getRequestURI(), e);
        return processResponse(new CommonException("服务器内部异常", 500));
    }

    private ResponseEntity<Result<Void>> processResponse(CommonException e){
        return ResponseEntity.status(e.getCode()).body(Result.error(e.getMessage()));
    }
}

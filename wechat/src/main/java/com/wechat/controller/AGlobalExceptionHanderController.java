package com.wechat.controller;

import com.wechat.entity.vo.ResponseVO;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

import java.net.BindException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class AGlobalExceptionHanderController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(AGlobalExceptionHanderController.class);

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request) {
        logger.error("请求错误，请求地址{},错误信息{}", request.getRequestURI(), e);
        @SuppressWarnings("rawtypes")
        ResponseVO ajaxResponse = new ResponseVO();
        if (e instanceof NoHandlerFoundException) {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_404.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_404.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);

        } else if (e instanceof BussinessException) {
            BussinessException bussinessException = (BussinessException) e;
            ajaxResponse.setCode(bussinessException.getCode());
            ajaxResponse.setInfo(bussinessException.getMsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else if (e instanceof BindException){
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else if (e instanceof DuplicateKeyException){
            ajaxResponse.setCode(ResponseCodeEnum.CODE_601.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_601.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        } else if(e instanceof MissingServletRequestParameterException){
            logger.info("缺少请求参数{}", ((MissingServletRequestParameterException) e).getParameterName());
            ajaxResponse.setCode(ResponseCodeEnum.CODE_602.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_602.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);
            
        }else if(e instanceof HandlerMethodValidationException || e instanceof IllegalArgumentException){
            // 获取请求参数
            String queryString = request.getQueryString(); // 获取 URL 查询参数
            logger.error(queryString);
            String requestMethod = request.getMethod();
            logger.error(requestMethod);
            String requestURI = request.getRequestURI();
            logger.error(requestURI);
            // 获取错误信息
            Map<String, String[]> params = request.getParameterMap();
            for (String paramName : params.keySet()) {
                String[] paramValues = params.get(paramName);
                for (String paramValue : paramValues) {
                    logger.error(paramName + ": " + paramValue);
                }
            }
            ajaxResponse.setCode(ResponseCodeEnum.CODE_600.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_600.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }else {
            ajaxResponse.setCode(ResponseCodeEnum.CODE_500.getCode());
            ajaxResponse.setInfo(ResponseCodeEnum.CODE_500.getmsg());
            ajaxResponse.setStatus(STATUS_ERROR);
        }
        return ajaxResponse;
    }
}

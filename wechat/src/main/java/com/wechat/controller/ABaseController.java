package com.wechat.controller;

import com.wechat.entity.vo.ResponseVO;
import com.wechat.redis.RedisUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

import javax.annotation.Resource;

import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.vo.ResponseCodeEnum;

public class ABaseController {
    protected static final String STATUS_SUCCESS = "success";

    protected static final String STATUS_ERROR = "error";

    @Resource
    private RedisUtils redisUtils;

    protected<T> ResponseVO<T> getSuccessResponseVO(T data) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getmsg());
        responseVO.setData(data);
        return responseVO;
    }

    protected TokenUserInfo getTokenUserInfo(HttpServletRequest request) {
        String token =  request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return null;
        }
        TokenUserInfo tokenUserInfo = (TokenUserInfo)redisUtils.get(Constans.REDIS_KEY_WS_TOKEN + token);
        return tokenUserInfo;
    }
}

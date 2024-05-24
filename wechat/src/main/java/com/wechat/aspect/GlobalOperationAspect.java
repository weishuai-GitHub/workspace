package com.wechat.aspect;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.vo.ResponseCodeEnum;
import com.wechat.exception.BussinessException;
import com.wechat.redis.RedisUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component("globalOperationAspect")
public class GlobalOperationAspect {
    @Resource
    private RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(GlobalOperationAspect.class);

    @Before("@annotation(com.wechat.annotation.GlobalIntercepter)")    
    public void interceptorDO(JoinPoint point) throws Exception {
        
        Method method = ((MethodSignature)point.getSignature()).getMethod();

        GlobalIntercepter interceptor = method.getAnnotation( GlobalIntercepter.class);

        if(interceptor==null){
            return;
        }

        if(interceptor.checkLogin() || interceptor.checkAdmin()){
            checkLogin(interceptor.checkAdmin());
            // try {
            //     checkLogin(interceptor.checkAdmin());
            // } catch (BussinessException e) {
            //     logger.error("checkLogin error",e);
            //     throw e;
            // }catch(Exception e)
            // {
            //     logger.error("全局拦截异常",e);
            //     throw new BussinessException(ResponseCodeEnum.CODE_500);
            // }catch(Throwable e)
            // {
            //     logger.error("全局拦截异常",e);
            //     throw new BussinessException(ResponseCodeEnum.CODE_500);
            // }

        }
    }

    private void checkLogin(Boolean checkAdmin) throws Exception{
        @SuppressWarnings("null")
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader("token");
        if (token==null || StringUtils.isEmpty(token)) {
            logger.error("token is null");
            throw new BussinessException(ResponseCodeEnum.CODE_901);
        }
        TokenUserInfo tokenUserInfo = (TokenUserInfo)redisUtils.get(Constans.REDIS_KEY_WS_TOKEN + token);
        if(tokenUserInfo==null){
            logger.error("tokenUserInfo is null");
            throw new BussinessException(ResponseCodeEnum.CODE_901);
        }
        if(checkAdmin && !tokenUserInfo.getAdmin()){
            logger.error("tokenUserInfo is not admin");
            throw new BussinessException(ResponseCodeEnum.CODE_404);
        }
    }
}

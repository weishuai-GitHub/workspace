
package com.wechat.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.entity.vo.UserInfoVO;
import com.wechat.exception.BussinessException;
import com.wechat.redis.RedisCompent;
import com.wechat.redis.RedisUtils;
import com.wechat.service.UserInfoService;
import com.wechat.utils.CustomArithmeticCaptcha;
import com.wf.captcha.ArithmeticCaptcha;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 账号相关接口
 *
 * @author: ShuaiWei
 * @date: 2024/05/12
 */
@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisCompent redisCompent;

    @SuppressWarnings("rawtypes")
    @RequestMapping("checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new CustomArithmeticCaptcha(100, 42);
        captcha.setLen(2);
        String result = captcha.text();
        String code = captcha.toBase64();
        String checkCodeKey = UUID.randomUUID().toString();
        Map<String,String> resultMap = new HashMap<>();
        redisUtils.set(Constans.REDIS_KEY_CHECK_CODE+checkCodeKey, result, Constans.REDIS_TIME_1MIN*10);
        resultMap.put("checkCode", code);
        resultMap.put("checkCodeKey", checkCodeKey);
        logger.info("验证码是:{}", result);
        return getSuccessResponseVO(resultMap);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO register(@RequestParam("checkCodeKey") @NotEmpty String checkCodeKey,
    @RequestParam("email") @NotEmpty @Email String email,
    @RequestParam("password") @NotEmpty @Pattern( regexp = Constans.REGEX_PASSWORD) String password, 
    @RequestParam("nickName") @NotEmpty String nickName,
    @RequestParam("checkCode") @NotEmpty String checkCode) throws Exception {
        try {
            if(!checkCode.equalsIgnoreCase((String) redisUtils.get(Constans.REDIS_KEY_CHECK_CODE+checkCodeKey))) {
                throw new BussinessException( "验证码错误");
            }
            userInfoService.register(email, password, nickName);
        } finally {
            redisUtils.del(Constans.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
        return getSuccessResponseVO(null);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("login")
    public ResponseVO login(@RequestParam("checkCode") @NotEmpty String checkCode,
    @RequestParam("email") @NotEmpty @Email String email,
    @RequestParam("password") @NotEmpty String password, 
    @RequestParam("checkCodeKey") @NotEmpty String checkCodeKey) throws Exception {
        try {
            if(!checkCode.equalsIgnoreCase((String) redisUtils.get(Constans.REDIS_KEY_CHECK_CODE+checkCodeKey))) {
                throw new BussinessException( "验证码错误");
            }
            UserInfoVO userInfoVO  =  userInfoService.login(email, password);
            return getSuccessResponseVO(userInfoVO);
        } finally {
            redisUtils.del(Constans.REDIS_KEY_CHECK_CODE+checkCodeKey);
        }
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("getSysSetting")
    @GlobalIntercepter
    public ResponseVO getSysSetting() throws Exception {
        
        return getSuccessResponseVO(redisCompent.getSysSetting());
    }

}

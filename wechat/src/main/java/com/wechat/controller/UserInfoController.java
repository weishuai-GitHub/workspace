package com.wechat.controller;

import com.wechat.annotation.GlobalIntercepter;
import com.wechat.entity.constants.Constans;
import com.wechat.entity.dto.TokenUserInfo;
import com.wechat.entity.po.UserInfo;
import com.wechat.entity.vo.ResponseVO;
import com.wechat.entity.vo.UserInfoVO;

// import java.util.List;

// import com.wechat.entity.po.UserInfo;
// import com.wechat.entity.query.UserInfoQuery;
// import com.wechat.entity.vo.ResponseVO;
import com.wechat.service.UserInfoService;
import com.wechat.utils.ToolUtils;
import com.wechat.websocket.ChannelContextUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 用户信息Controller
 *
 * @author: ShuaiWei
 * @date: 2024/05/13
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController {

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private ChannelContextUtils channelContextUtils;

	@SuppressWarnings("rawtypes")
	@RequestMapping("getUserInfo")
	@GlobalIntercepter
	public ResponseVO getUserInfo(HttpServletRequest request) {
		TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
		UserInfo userInfo = userInfoService.getByUseId(tokenUserInfo.getUserId());
		UserInfoVO  useInfoVO = ToolUtils.copy(userInfo, UserInfoVO.class);
		useInfoVO.setUserId(userInfo.getUseId());
		useInfoVO.setAdmin(tokenUserInfo.getAdmin());
		return getSuccessResponseVO(useInfoVO);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("saveUserInfo")
	@GlobalIntercepter
	public ResponseVO saveUserInfo(HttpServletRequest request,
	UserInfo userInfo,
	@RequestParam("avatarFile") MultipartFile avatarFile,
	@RequestParam("avatarCover") MultipartFile avatarCover) throws Exception {
		TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
		userInfo.setUseId(tokenUserInfo.getUserId());
		userInfo.setPassword(null);
		userInfo.setStatus(null);
		userInfo.setCreateTime(null);
		userInfo.setLastLoginTime(null);
		this.userInfoService.updateInfo(userInfo, avatarFile,avatarCover);
		return getSuccessResponseVO(null);
	}

    @SuppressWarnings("rawtypes")
	@RequestMapping("updatePassword")
	@GlobalIntercepter
	@Transactional(rollbackFor = Exception.class)
	public ResponseVO updatePassword(HttpServletRequest request,
	@RequestParam("password") @NotEmpty @Pattern( regexp = Constans.REGEX_PASSWORD) String  password) throws Exception {
		TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
		UserInfo userInfo = new UserInfo();
		userInfo.setUseId(tokenUserInfo.getUserId());
		userInfo.setPassword(ToolUtils.encodeMD5(password));
		userInfoService.updateByUseId(userInfo, tokenUserInfo.getUserId());
		//强制登录，让用户重新登录
		channelContextUtils.colseContext(tokenUserInfo.getUserId());
		return getSuccessResponseVO(null);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("logout")
	@GlobalIntercepter
	@Transactional(rollbackFor = Exception.class)
	public ResponseVO logout(HttpServletRequest request) throws Exception {
		TokenUserInfo tokenUserInfo = getTokenUserInfo(request);
		//退出登录，关闭ws连接
		channelContextUtils.colseContext(tokenUserInfo.getUserId());
		return getSuccessResponseVO(null);
	}

}

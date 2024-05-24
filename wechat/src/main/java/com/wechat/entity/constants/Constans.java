package com.wechat.entity.constants;

import com.wechat.entity.enums.UserContactTypeEnums;

public class Constans {
    public static final String APP_UPDATE_FOLDER = "/app/";
    public static final String APP_NAME = "wechat";
    public static final String REDIS_KEY_CHECK_CODE = "wechat:account:check_code:";
    
    public static final String REDIS_KEY_WS_USER_HEART_BEAT = "wechat:ws:user:heartbeat:";
    public static final String REDIS_KEY_WS_TOKEN = "wechat:ws:token:";
    public static final String REDIS_KEY_WS_TOKEN_USERID = "wechat:ws:token:userid:";
    public static final String REDIS_KEY_SYS_SETTING = "wechat:syssetting";
    public static final String REDIS_KEY_USER_CONTACT = "wechat:contact:userid:";

    public static final Integer REDIS_TIME_1MIN = 60;
    public static final Integer REDIS_KEY_EXPRESS_DAT = REDIS_TIME_1MIN*24*60;
    public static final Integer REDIS_KEY_EXPRESS_TOKEN = REDIS_KEY_EXPRESS_DAT;
    public static final Integer REDIS_KEY_EXPRESS_HEART_BEAT=6;

    public static final Integer LENGHT_11 = 11;
    public static final Integer LENGHT_20 = 20;

    public static final String ROBOT_UID = UserContactTypeEnums.USER.getPrefix() + "robot";
    public static final String ROBOT_NICK_NAME = "robot";

    public static final String FILE_FOLDER_FILE = "file/";

    public static final String FILE_FOLDER_AVATAR_NAME = "avatar/";

    public static final String IMAGE_SUFFIX = ".png";
    public static final String COVER_IMAGE_SUFFIX = "_cover.png";

    public static final String APPLY_INFO_TEMPLATE = "我是%s,请求添加你为好友";
    
    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])[\\da-zA-Z~!@#$%^&*\\_]{8,18}$";

    public static final String APP_EXE_SUFFIX = ".exe";

    public static final Long TIMEMILLIS_3DAYS_AGC = 3*24*60*60*1000L;

    public static final String[] IMAGE_SUFFIX_LIST = {".jpg",".jpeg",".png",".gif",".bmp",".webp"};
    
    public static final String[] VIDEO_SUFFIX_LIST = {".mp4",".avi",".rmvb",".mov",".mkv"};

    public static final Long FILE_ZISE_ME = 1024*1024L;
}

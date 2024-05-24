
package com.wechat.entity.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {

    /**
     * websocket服务端口
     */
    @Value("${ws.port}")
    private Integer wsPort;

    /**
     * 项目文件夹
     */
    @Value("${project.folder}")
    private String projectFolder;

    /**
     * 管理员邮箱
     */
    @Value("${admin.email}")
    private String adminEmail;

    public Integer getWsPort() {
        return wsPort;
    }

    public String getProjectFolder() {
        if(StringUtils.isEmpty(projectFolder) || !projectFolder.endsWith("/")){
            projectFolder = projectFolder + "/";
        }
        return projectFolder;
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}

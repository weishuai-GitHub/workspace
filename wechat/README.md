# 仿wechat聊天-服务端

## 项目介绍

仿wechat聊天-服务端，实现了一个简单的聊天室，支持群聊、私聊、表情发送、文件发送等功能。

## 项目结构

```shell
├─src
  ├─main
    ├─java
    │  └─com
    │      └─wechat
    │          ├─annotation
    │          ├─aspect
    │          ├─controller
    │          ├─entity
    │          │  ├─config
    │          │  ├─constants
    │          │  ├─dto
    │          │  ├─enums
    │          │  ├─po
    │          │  ├─query
    │          │  └─vo
    │          ├─exception
    │          ├─mappers
    │          ├─redis  #redis相关操作
    │          ├─service
    │          │  └─impl
    │          ├─utils    
    │          └─websocket # websocket相关操作
    │              └─netty
    └─resources
        ├─com
        │  └─wechat
        │      └─mappers
        └─META-INF
```

## 相关配置

- `application.yml`：配置文件，主要配置了数据库、redis、websocket等相关配置
- `logback.xml`：日志配置文件
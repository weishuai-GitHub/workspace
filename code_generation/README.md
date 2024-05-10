# Java代码自动生成工具

## 介绍

Java代码自动生成工具，通过配置模板，可以生成Java代码，自动配置实体类，mybats XML文件。生成的代码可以直接使用，也可以根据需要进行修改。

### 生成的目录结构

```
├─main
│  ├─java
│  │  └─xxx
│  │      └─xxx
│  │          ├─controller  // 控制器
│  │          ├─entity      // 实体类
│  │          │  ├─po       // 表对应的实体类
│  │          │  ├─query    // 查询实体类
│  │          │  └─vo       // 返回实体类
│  │          ├─exception   // 异常
│  │          ├─mappers     // mybatis mapper映射
│  │          ├─service     // 服务接口
│  │          │  └─impl     // 服务实现
│  │          └─utils       // 工具类
│  └─resources
│      └─xxx
│          └─xxx
│              └─mappers    // mybatis mapper xml文件

```

###  配置文件

配置文件为`application.properties`，配置文件中包含了数据库连接信息，生成代码的模板信息等。

```properties
#作者
author=ShuaiWei

#数据库配置
db.driver.name=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC
db.username=root
db.password=root

#Json序列化需要忽略的属性，比如password等敏感信息
ignore.bean.tojson.fields=status,companyId
ignore.bean.tojson.expression=@JsonIgnore
ignore.bean.tojson.classes=import com.fasterxml.jackson.annotation.JsonIgnore

#日期格式序列化
bean.date.serialization.expression=@JsonFormat(pattern = "%s", timezone = "GMT+8")
bean.date.serialization.classes=import com.fasterxml.jackson.annotation.JsonFormat

#日期格式反序列化
bean.date.deserialization.expression=@DateTimeFormat(pattern = "%s")
bean.date.deserialization.classes=import org.springframework.format.annotation.DateTimeFormat


#生成代码的路径
path.base = xxxx/java_demo/src/main/java
path.base.resources = xxxx/java_demo/src/main/resources
#生成代码的包名
package.base=com.example
#生成代码的模块名
package.po=entity.po
package.param=entity.query
package.vo=entity.vo
package.utils=utils
package.mappers=mappers
package.service=service
package.service.impl=service.impl
package.controller=controller
package.exception=exception


#是否忽略表前缀
db.table.prefix=true
#搜索bean的后缀
suffix.bean.param=Query
#参数模糊搜索后缀
suffix.bean.param.fuzzy=Fuzzy
#参数日期起始于结束后缀
suffix.bean.param.time.start=Start
suffix.bean.param.time.end=End
#生成xml文件的后缀
suffix.mappers=Mapper
```
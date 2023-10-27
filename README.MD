# 使用说明

## 1.启动服务

### 1) Application中设置好环境必要的参数

- setClusterNumber: 集群编码
- setTenantNumber: 租户编码
- setServerIP: 服务端IP

例：

```java
cosmic.setClusterNumber("cosmic");
cosmic.setTenantNumber("sample");
cosmic.setServerIP("192.168.95.177");
```

使用苍穹5.0之前的版本，需注释掉kd.cosmic.server.Launcher的@SdkPublic标注。

### 2) 启动服务

```java
kd.cosmic.Application
```
首次运行，IDEA会弹出命令行过长的提示，形如：
```text
Error running kd.cosmic.Application. Command line is too long. 
Shorten the command line via JAR manifest or via a classpath file and rerun.
```
点击<code>JAR manifest</code>链接执行启动。

参考文章: https://developer.kingdee.com/article/357806944599232512

### 3) 登录
启动完毕后打开：http://localhost:8080/ierp

### 4) IP变更
若服务器IP发生变化，则同步做以下修改：

- Application.java：cosmic.setServerIP(...)

- cosmic.properties：MCServerURL的值

- 苍穹虚拟机：苍穹开发助手->服务器管理->初始化设置->一键设置IP

## 2.更新环境
菜单操作路径：苍穹开发助手-->更新环境

将从MC服务器上更新最新包：cosmic.zip、webapp.zip，解压到当前工程同一个目录下，目录名为"工程名称-server"。

Application.java中已自动设置好解压后的web路径：

```java
cosmic.setWebPath("C:/Users/xxx/kingdee/myproject-server/webapp")
```

注意：更新版本时请先停止在正在跑或调试的工程，避免文件占用无法覆盖。

## 3.插件选项
菜单操作路径：File->Settings->苍穹开发助手


## 4.搜索工具窗
菜单操作路径：View->Tool Windows->苍穹开发助手

若当前为苍穹工程(工程根目录下有cosmic.properties文件)，苍穹开发助手自行显示，停靠在右上角。

提供文档检索功能。


## 5.菜单-登录开发者门户
未登录：苍穹开发助手(工具窗)-社区文章-可搜索“开发者社区”文档
登录后：苍穹开发助手(工具窗)-社区文章-可搜索“开发者社区+开发者门户”文档


## 6.工程打包
给当前工程打jar包，jar包配置文件: cosmic.build.xml。


## 7.菜单-服务器管理
通过SSH远程管理服务器。


## 8.设置zk用户密码

1) Application启动中配置：
```java
cosmic.setConfigUrl("xxx.xxx.xxx.xxx:2181","user","password");
```

2) 菜单-服务器管理-初始化设置:
```properties
ZK地址=xxx.xxx.xxx.xxx:2181?user=xxx&password=xxx
```



## 9.相关文档

### 如何快速搭建二开环境

https://vip.kingdee.com/article/357950002276007424

### 金蝶云·苍穹开发助手

https://vip.kingdee.com/article/355335902929259776

### 苍穹虚拟机下载列表(全)

https://vip.kingdee.com/article/357923047296550912

### IDEA启动命令行过长解决办法

https://vip.kingdee.com/article/357806944599232512

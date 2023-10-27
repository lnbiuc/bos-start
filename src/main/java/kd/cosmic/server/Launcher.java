package kd.cosmic.server;

import kd.bos.config.client.util.ConfigUtils;
import kd.bos.service.webserver.JettyServer;
import kd.sdk.annotation.SdkPublic;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * cosmic服务启动器，含默认配置，如需更改请在Application中设置。
 */
@SdkPublic
public final class Launcher {

    private boolean setConfigUrl = false;

    public Launcher() {
        setDefault();
    }

    private void setDefault() {
        set("configAppName", "mservice,web");
        set("webmserviceinone", "true");
        set("file.encoding", "utf-8");
        set("mq.consumer.register", "true");
        set("MONITOR_HTTP_PORT", "9998");
        set("JMX_HTTP_PORT", "9091");
        set("dubbo.protocol.port", "28888");
        set("dubbo.consumer.url", "dubbo://localhost:28888");
        set("dubbo.consumer.url.qing", "dubbo://localhost:30880");
        set("dubbo.registry.register", "false");
        set("dubbo.service.lookup.local", "true");
        set("appSplit", "false");
        set("tenant.code.type", "config");
        set("JETTY_WEB_PORT", "8080");
        set("domain.contextUrl", "http://localhost:8080/ierp");
		set("lightweightdeploy", "true");
        set("redismodelcache.enablelua", "false");
        set("lightweightdeploy.services", "");

        try {
            String logConfig = new String(Files.readAllBytes(Paths.get(getClass().getResource("log.config.xml").toURI())), "UTF-8");
            set("log.config", logConfig);
        } catch (Exception e) {
            //ignore
        }

        setClusterNumber("cosmic");
        setTenantNumber("sample");
        setAppName("cosmic-server");
        setStartWithQing(true);
        setXdbEnable(false);
        setSqlOut(true, true);
    }

    public void start() {
        JettyServer.main(null);
    }

    public void set(String key, String value) {
        System.setProperty(key, value);
    }

    public String get(String key) {
        return System.getProperty(key);
    }

    /**
     * 设置苍穹服务器IP地址
     */
    public void setServerIP(String ip) {
        setMCServerUrl("http://" + ip + ":8090");
        if (!setConfigUrl) {
            setConfigUrl(ip + ":2181");
        }
        set("fileserver", "http://" + ip + ":8100/fileserver/");
        set("imageServer.url", "http://" + ip + ":8100/fileserver/");
    }

    /**
     * 设置MC服务地址
     *
     * @param mcServerUrl
     */
    public void setMCServerUrl(String mcServerUrl) {
        set("mc.server.url", mcServerUrl);
    }

    /**
     * @param configUrl 配置服务地址
     */
    public void setConfigUrl(String configUrl) {
        set(ConfigUtils.CONFIG_URL_KEY, configUrl);
        setConfigUrl = true;
    }

    /**
     * 配置服务地址
     *
     * @param connectString zookeeper链接URL，如 127.0.0.1:2181
     * @param user          用户
     * @param password      密码
     */
    public void setConfigUrl(String connectString, String user, String password) {
        if (user != null && password != null) {
            setConfigUrl(connectString + "?user=" + user + "&password=" + password);
        } else {
            setConfigUrl(connectString);
        }
    }

    /**
     * @param clusterNumber 集群编码
     */
    public void setClusterNumber(String clusterNumber) {
        set(ConfigUtils.CLUSTER_NAME_KEY, clusterNumber);
    }


    /**
     * @param appName 本节点服务名称
     */
    public void setAppName(String appName) {
        setAppName(appName, true);
    }

    public void setAppName(String appName, boolean alsoSetQueueTag) {
        set(ConfigUtils.APP_NAME_KEY, appName);
        if (alsoSetQueueTag) {
            setQueueTag(appName);
        }
    }

    public void setStartWithQing(boolean b) {
        set("bos.app.special.deployalone.ids", b ? " " : "qing");
    }

    /**
     * @param tenantNumber 租户编码
     */
    public void setTenantNumber(String tenantNumber) {
        set("domain.tenantCode", tenantNumber);
    }

    /**
     * @param enable 是否开启水平分表服务
     */
    public void setXdbEnable(boolean enable) {
        set("xdb.enable", String.valueOf(enable));
    }

    /**
     * @param tag 队列标记
     */
    public void setQueueTag(String tag) {
        set("mq.debug.queue.tag", tag);
    }

    /**
     * @param path web路徑
     */
    public void setWebPath(String path) {
        set("JETTY_WEBRES_PATH", path);
    }

    /**
     * 控制台输出SQL开关
     *
     * @param outSQL        是否输出SQL
     * @param withParameter 是否输出参数
     */
    public void setSqlOut(boolean outSQL, boolean withParameter) {
        set("db.sql.out", String.valueOf(outSQL));
        set("db.sql.out.withParameter", String.valueOf(withParameter));
    }

}
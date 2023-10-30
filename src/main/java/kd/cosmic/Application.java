package kd.cosmic;

import kd.bos.config.client.util.ConfigUtils;
import kd.cosmic.server.Launcher;

/**
 * 启动本地应用程序(微服务节点)
 */
public class Application {

    public static void main(String[] args) {
        Launcher cosmic = new Launcher();

        cosmic.setClusterNumber("cosmic");
        cosmic.setTenantNumber("ierp");
        cosmic.setServerIP("127.0.0.1");

        cosmic.setAppName("cosmic-ZZSLL-tb3XlXxx");
        cosmic.setWebPath("D:/Code/bos-server/webapp/static-file-service");
        cosmic.setConfigUrl("127.0.0.1:2181", "zookeeper", "Dd112211");
        cosmic.setStartWithQing(false);

        cosmic.start();
    }
}
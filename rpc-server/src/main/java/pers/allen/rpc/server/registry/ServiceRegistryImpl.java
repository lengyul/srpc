package pers.allen.rpc.server.registry;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import pers.allen.rpc.server.RpcServer;
import pers.allen.rpc.server.annotation.Service;
import pers.allen.rpc.server.handler.RequestLocalCall;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class ServiceRegistryImpl implements ServiceRegistry,ApplicationContextAware, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ServiceRegistryImpl.class);

    @Value("${registry.address}")
    private String registryAddress;
    @Value("${rpc.server.address}")
    private String serverAddress;

    private final Map<String,Object> serviceMap  =  new HashMap<>();
    private ZkClient client = null;

    public void registry() {
        if(serverAddress != null) {
            client = new ZkClient(registryAddress,2000);
            if(client != null) {
                createRootNode(client);
                addServiceNode(serverAddress); // 在根节点下创建服务节点
            }
        }
    }

    //在/rpc根节点下，创建临时顺序子节点
    private void addServiceNode(String serverAddress) {
        if(serviceMap.size() > 0) {
            serviceMap.forEach((k,v) -> {
                String serviceName = k;
                String path = ZK_REGISTRY_PATH + "/" +serviceName;
                if(client.exists(path)) {
                        client.delete(path);
                 //   client.writeData(path,serverAddress);
                 //   logger.info("更新zookeeper临时数据节点：" + path + " " + serverAddress);
                }
                path =  client.create(path,serverAddress,CreateMode.EPHEMERAL);
                logger.info("创建zookeeper临时数据节点：" + path + " " + serverAddress);
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(Service.class);
        beans.forEach((k,v) -> {
            Class<?> clazz = v.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                String interfaceName = anInterface.getName();
                logger.info("加载注册服务类："+ interfaceName);
                serviceMap.put(interfaceName, v);
            }
        });
        RequestLocalCall.setServers(Collections.unmodifiableMap(serviceMap));
        logger.info("所有注册服务类已加载完成："+ serviceMap);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
      new Thread(() -> {
          registry();
          new RpcServer().start(serverAddress); // 启动RPC服务器
        }).start();
    }


}

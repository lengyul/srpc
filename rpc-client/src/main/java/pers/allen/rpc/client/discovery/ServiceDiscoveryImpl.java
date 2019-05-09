package pers.allen.rpc.client.discovery;

import com.alibaba.fastjson.JSONObject;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    @Value("${registry.address}")
    private String registryAddress;
    private static volatile Map<String,String> serviceMap = new HashMap<>();
    private ZkClient zkClient = null;
    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryImpl.class);

    private ServiceDiscoveryImpl() {}
    private static final ServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl();
    public static ServiceDiscovery getInstance() {
        return serviceDiscovery;
    }

    @PostConstruct
    public void init() {
        discovery();
    }

    public void discovery() {
        zkClient = new ZkClient(registryAddress,2000);
        if(zkClient != null) {
            readRegistryNode(zkClient);
            monitorAndChangeNode(zkClient);
        }
    }

    public SocketAddress getServiceAddress(String serviceName) {
        if (serviceMap.containsKey(serviceName)) {
            String address = serviceMap.get(serviceName);
            String[] info  = address.split(":");
            return new InetSocketAddress(info[0],Integer.parseInt(info[1]));
        }
        logger.error("The service name is not found" + serviceName);
        throw new NullPointerException(serviceName);
    }

    private void readRegistryNode(ZkClient zkClient) {
        List<String> children = zkClient.getChildren(ZK_REGISTRY_PATH);
        if(children.size() > 0) {
            children.forEach((node) -> {
                updateNodeValue(node);
            });
        }
        logger.info("发现服务注册列表：" + JSONObject.toJSONString(serviceMap));
    }

    private void updateNodeValue(String node) {
        String address = zkClient.readData(ZK_REGISTRY_PATH+ "/" + node);
        serviceMap.put(node,address);
    }

    private void monitorAndChangeNode(ZkClient zkClient) {
        zkClient.subscribeChildChanges(ZK_REGISTRY_PATH, (s, nodes) -> {
            logger.info("监听到子节点数据变化：" + JSONObject.toJSONString(nodes));
            serviceMap.clear();
            nodes.forEach((node) -> {
                updateNodeValue(node);
            });
            logger.info("更新缓存服务列表：" + JSONObject.toJSONString(serviceMap));
        });
    }

}

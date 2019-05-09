package pers.allen.rpc.server.registry;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by lengyul on 2019/4/25 15:31
 */
public interface ServiceRegistry {

    String ZK_REGISTRY_PATH = "/rpc"; // 根节点

    //创建根节点
    default void createRootNode(ZkClient client){
        boolean exists = client.exists(ZK_REGISTRY_PATH);
        if (!exists){
            client.createPersistent(ZK_REGISTRY_PATH);
        }
    }

    void registry();

}

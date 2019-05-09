package pers.allen.rpc.client.discovery;


import java.net.SocketAddress;

/**
 * Created by lengyul on 2019/4/25 17:15
 */
public interface ServiceDiscovery {

     String ZK_REGISTRY_PATH = "/rpc";

     void discovery();

     SocketAddress getServiceAddress(String serviceName);
}

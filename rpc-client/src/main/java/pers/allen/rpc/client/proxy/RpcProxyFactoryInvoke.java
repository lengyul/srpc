package pers.allen.rpc.client.proxy;

import pers.allen.rpc.client.annotation.Reference;
import pers.allen.rpc.client.channel.RpcChannelGroup;
import pers.allen.rpc.client.channel.RpcClient;
import pers.allen.rpc.server.dto.RequestMsg;
import pers.allen.rpc.server.utils.RequestTypeContants;
import pers.allen.rpc.server.utils.async.RequestAsyncFutureUtils;
import pers.allen.rpc.server.utils.sync.RequestSyncQueueUtils;
import pers.allen.rpc.server.dto.BuilderMsg;
import pers.allen.rpc.server.dto.ResponseMsg;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class RpcProxyFactoryInvoke implements InvocationHandler {

    private RpcProxyFactoryInvoke() {}
    private static RpcProxyFactoryInvoke proxy = new RpcProxyFactoryInvoke();
    private RpcChannelGroup channelGroup = RpcClient.getInstance();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("toString".equals(method.getName())) {
            return proxy.getClass().getName();
        }
        System.out.println(proxy);
        RequestMsg requestMsg  = BuilderMsg.buildRequestMsg(method,args);
        int type = requestMsg.getType();
        Long requestId = requestMsg.getRequestId();
        channelGroup.remoteRequest(requestMsg); // 请求远程调用
        if (RequestTypeContants.SYNC == type) { // sync
            // 阻塞等待远程调用完成并返回
            ResponseMsg responseMsg = RequestSyncQueueUtils.waitResponse(requestId);
            System.out.println(responseMsg);
            if(responseMsg != null)
                return responseMsg.getData();
        } else if(RequestTypeContants.ASYNC == type) { // async
            RequestAsyncFutureUtils.asyncRequest(Thread.currentThread().getId(), requestId);
        }
        return null;
    }

    public static <T> T proxyInstance(Class<T> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = new Class[]{clazz};
        return (T) Proxy.newProxyInstance(classLoader,interfaces, proxy);
    }

}

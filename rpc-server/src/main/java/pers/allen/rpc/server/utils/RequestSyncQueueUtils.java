package pers.allen.rpc.server.utils;

import pers.allen.rpc.server.dto.ResponseMsg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 远程调用线程等待唤醒类
 */
public class RequestSyncQueueUtils {

    private static Map<String, SynchronousQueue<ResponseMsg>> queueMap = new ConcurrentHashMap<>();

    private RequestSyncQueueUtils() {
        throw new AssertionError();
    }

    public static SynchronousQueue getQueue(String requestId) {
        if(queueMap.containsKey(requestId)) {
            return queueMap.get(requestId);
        }
        throw new NullPointerException(requestId);
       // return null;
    }

    public static ResponseMsg waitResponse(String requestId) {
        return waitResponse(requestId,5000); // default 5000ms
    }

    /**
     * 接收响应结果
     * @param requestId
     * @param timeout
     * @return
     */
    public static ResponseMsg waitResponse(String requestId, long timeout) {
        ResponseMsg msg = null;
        SynchronousQueue<ResponseMsg> synchronousQueue = new SynchronousQueue();
        queueMap.put(requestId,synchronousQueue);
        try {
         //   msg = synchronousQueue.take();
              msg = synchronousQueue.poll(timeout, TimeUnit.MILLISECONDS);
              if(queueMap.containsKey(requestId)){
                  queueMap.remove(requestId);
              }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 唤醒正在等待的SynchronousQueue线程并返回结果
     * @param msg
     */
    public static void notifyRequest(ResponseMsg msg) {
        String requestId = String.valueOf(msg.getRequestId());
        SynchronousQueue synchronousQueue = getQueue(requestId);
        if (synchronousQueue != null){
            synchronousQueue.add(msg);
            queueMap.remove(requestId);
        }
    }

}

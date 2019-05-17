package pers.allen.rpc.server.utils.sync;

import pers.allen.rpc.server.dto.BuilderMsg;
import pers.allen.rpc.server.dto.ResponseMsg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 远程调用线程等待唤醒类
 */
public class RequestSyncQueueUtils {

    private static Map<Long, SynchronousQueue<ResponseMsg>> queueMap = new ConcurrentHashMap<>();

    private RequestSyncQueueUtils() {
        throw new AssertionError();
    }

    private static SynchronousQueue getQueue(Long requestId) {
        if(queueMap.containsKey(requestId)) {
            return queueMap.get(requestId);
        }
        return null;
    }

    public static ResponseMsg waitResponse(Long requestId) {
        return waitResponse(requestId,10000L); // default 5000ms
    }

    /**
     * 接收响应结果
     * @param requestId
     * @param timeout 如果没有唤醒或者提前唤醒，保证线程不会一直阻塞
     * @return
     */
    public static ResponseMsg waitResponse(Long requestId, Long timeout) {
        ResponseMsg msg = null;
        SynchronousQueue<ResponseMsg> synchronousQueue = new SynchronousQueue<>();
        queueMap.put(requestId,synchronousQueue);
        try {
              msg = synchronousQueue.poll(timeout, TimeUnit.MILLISECONDS);
              queueMap.remove(requestId);
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
        SynchronousQueue synchronousQueue = RequestSyncQueueUtils.getQueue(msg.getRequestId());
        if (synchronousQueue != null){
            synchronousQueue.add(msg);
        }
    }

}

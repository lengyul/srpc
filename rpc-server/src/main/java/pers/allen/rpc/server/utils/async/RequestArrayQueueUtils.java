package pers.allen.rpc.server.utils.async;

import pers.allen.rpc.server.dto.BuilderMsg;
import pers.allen.rpc.server.dto.ResponseMsg;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lengyul on 2019/5/16 16:56
 */
public class RequestArrayQueueUtils {

    private static Map<Long, BlockingQueue<ResponseMsg>> blockingQueueMap = new ConcurrentHashMap<>();

    private static BlockingQueue newArrayQueue() {
        return new ArrayBlockingQueue<>(1);
    }

    public static ResponseMsg waitAsyncResponse(Long requestId) throws InterruptedException {
        return waitAsyncResponse(requestId, 10000L);
    }

    private static BlockingQueue getBlockingQueue(Long requestId) {
        if (blockingQueueMap.containsKey(requestId)) {
            return blockingQueueMap.get(requestId);
        }
        return null;
    }

    public static int size() {
        return blockingQueueMap.size();
    }


    public static ResponseMsg waitAsyncResponse(Long requestId, Long timeout) throws InterruptedException {
        ResponseMsg msg = null;
        BlockingQueue<ResponseMsg> queue =  null;
        queue = getBlockingQueue(requestId);
        if(queue != null) {
            msg = queue.poll(timeout, TimeUnit.MILLISECONDS);
        } else {
            queue = newArrayQueue();
            blockingQueueMap.put(requestId,queue);
            msg = queue.poll(timeout, TimeUnit.MILLISECONDS);
        }
        blockingQueueMap.remove(requestId);
        return msg;
    }

    public static void notifyAsyncRequest(ResponseMsg msg) throws InterruptedException {
        Long requestId = msg.getRequestId();
        BlockingQueue queue = getBlockingQueue(requestId);
        if (queue != null) {
            queue.put(msg);
        } else {
            queue =  newArrayQueue();
            queue.put(msg);
            blockingQueueMap.put(requestId, queue);
        }
    }

}

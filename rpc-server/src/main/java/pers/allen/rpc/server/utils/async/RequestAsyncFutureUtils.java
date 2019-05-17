package pers.allen.rpc.server.utils.async;

import pers.allen.rpc.server.dto.ResponseMsg;
import pers.allen.rpc.server.utils.sync.RequestSyncQueueUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lengyul on 2019/5/15 17:14
 */
public class RequestAsyncFutureUtils {

    private static Map<Long, Future<?>> futureMap = new ConcurrentHashMap<>();

    private static ExecutorService asyncService = Executors.newCachedThreadPool();

    public static void asyncRequest(Long rtId, Long requestId) {
            AsyncRequestThread art = new AsyncRequestThread<>(requestId);
            Future future = asyncService.submit(art);
            futureMap.put(rtId,future);
    }

    public static Future getContextFuture() {
        Future<?> future = null;
        Long tid = Thread.currentThread().getId();
        if (futureMap.containsKey(tid)) {
            future = futureMap.get(tid);
            futureMap.remove(tid);
        }
        return future;
    }

    private static class AsyncRequestThread<T> implements Callable<T> {

        private Long requestId;
        private T result;

        AsyncRequestThread(Long requestId) {
            this.requestId = requestId;
        }

        @Override
        public T call() throws InterruptedException {
            ResponseMsg responseMsg = RequestArrayQueueUtils.waitAsyncResponse(requestId);
            if (responseMsg != null) return (T)responseMsg.getData();
            return null;
        }
    }

}

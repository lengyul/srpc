package pers.allen.rpc.server.utils.async;

import pers.allen.rpc.server.dto.ResponseMsg;
import pers.allen.rpc.server.utils.sync.RequestSyncQueueUtils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lengyul on 2019/5/15 17:14
 */
public class RequestAsyncFutureUtils {

    private static final ThreadLocal<Future> localFuture = new ThreadLocal<>();

    private static final ExecutorService asyncService = Executors.newCachedThreadPool();

    private static AsyncRequestThread newAsyncRequestThread(Long requestId) {
        AsyncRequestThread art = new AsyncRequestThread<>(requestId);
        return art;
    }

    public static void asyncRequest(Long requestId) {
        Future future = asyncService.submit(newAsyncRequestThread(requestId));
        localFuture.set(future);
    }

    public static void callRequest(Long rtId, Long requestId) {
   //         CompletableFuture.runAsync();
    }

    public static Future getContextFuture() {
        Future<?> future = null;
        future = localFuture.get();
       localFuture.remove();
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

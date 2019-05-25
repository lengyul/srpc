package pers.allen.rpc.server.utils.async;

import pers.allen.rpc.server.dto.ResponseMsg;
import pers.allen.rpc.server.utils.sync.RequestSyncQueueUtils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lengyul on 2019/5/15 17:14
 */
public class RequestAsyncFutureUtils {

    private static final ThreadLocal<CompletableFuture> localFuture = new ThreadLocal<>();
    private static final ExecutorService asyncService = Executors.newCachedThreadPool();

    /*
    private static AsyncRequestThread newAsyncRequestThread(Long requestId) {
        AsyncRequestThread art = new AsyncRequestThread<>(requestId);
        return art;
    }
    public static void asyncRequest(Long requestId) {
        Future future = asyncService.submit(newAsyncRequestThread(requestId));
        localFuture.set(future);
    }*/

    public static void asyncRequest(final Long requestId) {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseMsg responseMsg = RequestArrayQueueUtils.waitAsyncResponse(requestId);
                if (responseMsg != null) return responseMsg.getData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        },asyncService);
        localFuture.set(future);
    }

    public static CompletableFuture getContextFuture() {
        CompletableFuture<?> future = null;
        future = localFuture.get();
        localFuture.remove();
        return future;
    }

    /*public static void callRequest(Long requestId) {
           Future future = CompletableFuture.supplyAsync(() -> {
               try {
                   ResponseMsg responseMsg = RequestArrayQueueUtils.waitAsyncResponse(requestId);
                   if(responseMsg != null) return responseMsg.getData();
               } catch (InterruptedException e) {
                    e.printStackTrace();
               }
               return null;
           },asyncService);
           localFuture.set(future);
    }*/


    private static class AsyncRequestThread<T> implements Callable<T> {

        private Long requestId;

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

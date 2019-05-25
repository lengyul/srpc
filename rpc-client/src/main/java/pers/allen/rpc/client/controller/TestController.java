package pers.allen.rpc.client.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.allen.rpc.client.annotation.Reference;
import pers.allen.rpc.server.service.TestService;
import pers.allen.rpc.server.service.UserService;
import pers.allen.rpc.server.utils.async.RequestArrayQueueUtils;
import pers.allen.rpc.server.utils.async.RequestAsyncFutureUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {

  //  @Reference

  //  TestService testService = RpcProxyFactoryInvoke.proxyInstance(TestService.class);

    @Reference(type = 0)
    TestService testService;

    @Reference(type = 1)
    TestService testService2;

    @Reference
    UserService userService;

    @GetMapping("/service")
    public String getService() {
        System.out.println("testService: "+ testService);
        System.out.println("testService2: "+ testService2);
        return null;
    }

    @GetMapping("/list")
    public String getList() {
        String s1 = JSONObject.toJSONString(testService.getList());
        String s2 = JSONObject.toJSONString(userService.getUserName());
        return s1 + s2;
    }

    @PostMapping("/list")
    public List<String> getList(@RequestParam("n") String n) throws InterruptedException {
        int threadNumber = Integer.parseInt(n);
        System.out.println(threadNumber);
        CountDownLatch countDownLatch  = new CountDownLatch(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            new Thread(() -> {
                System.out.println(testService.getList());
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("-------------------------------end ");
        return testService.getList();
    }

    @GetMapping("/async")
    public Object ref() throws ExecutionException, InterruptedException {

     List<String> list =   testService.getList();
     System.out.println(list);
     // 获取当前请求线程Future
        CompletableFuture<List<String>> future = RequestAsyncFutureUtils.getContextFuture();
     //   CompletableFuture<String> ff = RequestAsyncFutureUtils.getContextFuture();
        System.out.println(future.getNow(null));
     List<String> list2 =   future.get();
     System.out.println(list2);
     return null;
    }

    @PostMapping("/asyncList")
    public Object asyncList(@RequestParam("n") String n) throws InterruptedException {
        int threadNumber = Integer.parseInt(n);
        System.out.println(threadNumber);
        CountDownLatch countDownLatch  = new CountDownLatch(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            new Thread(() -> {
                testService.getList();
                CompletableFuture<List<String>> future = RequestAsyncFutureUtils.getContextFuture();

                try {
                    List<String> list2 =   future.get();
                    System.out.println(list2);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.await();
        System.out.println("-------------------------------end ");
        return RequestArrayQueueUtils.size();
    }

}

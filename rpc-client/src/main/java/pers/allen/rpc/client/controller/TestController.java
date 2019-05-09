package pers.allen.rpc.client.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.allen.rpc.client.annotation.Reference;
import pers.allen.rpc.server.service.TestService;
import pers.allen.rpc.server.service.UserService;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
public class TestController {

  //  @Reference

  //  TestService testService = RpcProxyFactoryInvoke.proxyInstance(TestService.class);

    @Reference
    TestService testService;

    @Reference
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
        CountDownLatch countDownLatch  = new CountDownLatch(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            new Thread(() -> {
                testService.getList();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("-------------------------------end ");
        return testService.getList();
    }

    /*@GetMapping("/ref")
    public String ref() {
        return JSONObject.toJSONString(Arrays.asList(testService,testService2));
    }*/

}

package pers.allen.rpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class RpcServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcServerApplication.class, args);
    }

}

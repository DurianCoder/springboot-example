package com.example.nettyclient;

import com.example.nettyclient.netty.NettyClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new NettyClient().start("127.0.0.1", 9000);
    }
}

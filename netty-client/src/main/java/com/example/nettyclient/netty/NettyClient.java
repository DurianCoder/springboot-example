package com.example.nettyclient.netty;

import com.example.nettyclient.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ying.jiang
 * @define Todo
 * @date 2020-11-24-10:37:00
 */

@Component
public class NettyClient {

    Logger log = LoggerFactory.getLogger(NettyClient.class);

    public void start(String host, int port) throws Exception {

        /**
         * Netty用于接收客户端请求的线程池职责如下。
         * （1）接收客户端TCP连接，初始化Channel参数；
         * （2）将链路状态变更事件通知给ChannelPipeline
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 根据换行符来对数据进行组装
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            // 将接收到的对象转换成String
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            //绑定端口
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            group.shutdownGracefully().sync();
        }
    }


}

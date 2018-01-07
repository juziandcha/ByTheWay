package com.example.wmj.bytheway.ConnSup;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

//http://blog.csdn.net/u012348345/article/details/50655388

public class MinaThread implements Runnable {

    private static int port = 10086;
    private static String hostAddress = "47.100.124.11";

    private IoConnector connector = null;

    @Override
    public void run(){
        System.out.println("Client Start Connection");
        connector=new NioSocketConnector();
        connector.getSessionConfig().setReadBufferSize(10240);

        //设置连接超时时间
        connector.setConnectTimeoutMillis(10000);

        TextLineCodecFactory lineCodec=new TextLineCodecFactory(Charset.forName("UTF-8"));
        lineCodec.setDecoderMaxLineLength(1024*1024); //1M
        lineCodec.setEncoderMaxLineLength(1024*1024); //1M

        //set filter,threadPool过滤器用于将处理I/O的线程和处理业务逻辑的线程分开
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(lineCodec));

        connector.setHandler(new MinaClientHandler());

        connector.setDefaultRemoteAddress(new InetSocketAddress(hostAddress,port));

        //监听客户端是否断线
        connector.addListener(new IoListener(){
            @Override
            public void sessionDestroyed(IoSession arg0) throws Exception {
                while (true) {
                    try {
                        System.out.println("5秒后开始尝试重连");
                        Thread.sleep(5000);//等待5秒后开始重连
                        ConnectFuture future = connector.connect();
                        future.awaitUninterruptibly();// 等待连接创建完成
                        ByTheWayActivity.session = future.getSession();// 获得session
                        if (ByTheWayActivity.session != null && ByTheWayActivity.session.isConnected()) {
                            System.out.println("断线重连["
                                    + ((InetSocketAddress) ByTheWayActivity.session.getRemoteAddress()).getAddress().getHostAddress()
                                    + ":" + ((InetSocketAddress) ByTheWayActivity.session.getRemoteAddress()).getPort() + "]成功");
                            break;
                        } else {
                            System.out.println("断线重连失败---->5秒后再次重连.....");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        //开始连接
        while(true) {
            try {
                ConnectFuture future = connector.connect();
                future.awaitUninterruptibly();//等待连接创建完成
                ByTheWayActivity.session = future.getSession();//获得Session
                break;
            } catch (Exception ex) {
                try {
                    System.out.println("客户端连接异常");
                    Thread.sleep(5000);
                } catch (InterruptedException iex) {
                    iex.printStackTrace();
                }
            }
            finally {
                if (ByTheWayActivity.session != null && ByTheWayActivity.session.isConnected()) {
                    ByTheWayActivity.session.getCloseFuture().awaitUninterruptibly();
                    System.out.println("断开连接");
                }
            }
        }
    }
}
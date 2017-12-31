package com.example.wmj.bytheway.ConnSup;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONObject;

//http://blog.csdn.net/u012348345/article/details/50655388

public class MinaThread implements Runnable {

    private static int port = 10086;
    private static String hostAddress = "47.100.124.11";

    public IoSession session = null;
    private IoConnector connector = null;

    @Override
    public void run(){
        System.out.println("Client Start Connection");
        connector=new NioSocketConnector();
        //设置连接超时时间
        connector.setConnectTimeoutMillis(10000);

        //set filter,threadPool过滤器用于将处理I/O的线程和处理业务逻辑的线程分开
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));

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
                        session = future.getSession();// 获得session
                        if (session != null && session.isConnected()) {
                            System.out.println("断线重连["
                                    + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress()
                                    + ":" + ((InetSocketAddress) session.getRemoteAddress()).getPort() + "]成功");
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
                session = future.getSession();//获得Session
//                if (session != null && session.isConnected()) {
//                    String md5= MD5.getMD5("123");
//                    String sql="select * from User where ID=? and Password=? ;";
//
//                    JSONObject keyValue=new JSONObject();
//                    keyValue.put("ID","123");//顺序放置要填充?部分的值，防止sql注入
//                    keyValue.put("Password",md5);
//
//                    JsonContent JsonObj= new JsonContent(sql,"query",keyValue);

                    break;
//                }
            } catch (Exception ex) {
                try {
                    System.out.println("客户端连接异常");
                    Thread.sleep(7000);
                } catch (InterruptedException iex) {
                    iex.printStackTrace();
                }
            }
            finally {
                if (session != null && session.isConnected()) {
                    session.getCloseFuture().awaitUninterruptibly();
                    System.out.println("断开连接");
                }
            }
        }
    }
}
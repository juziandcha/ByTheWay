package com.example.wmj.bytheway.ConnSup;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MinaClientHandler implements IoHandler {

    public void exceptionCaught(IoSession session, Throwable ex)
            throws Exception {
        System.out.println("与" + session.getRemoteAddress() + "通信过程中出现错误:[" + ex.getMessage() + "]..连接即将关闭....");
        session.close(false);
        session.getService().dispose();
    }

    public void inputClosed(IoSession session) throws Exception {
    }

    public void messageReceived(IoSession session, Object obj) throws Exception {
        String msg = (String) obj;
        System.out.println("client messageReceived: " + msg);
    }

    public void messageSent(IoSession session, Object obj) throws Exception {
        System.out.println("client messageSent->" + (String)obj);
    }

    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed "+session.hashCode());
    }

    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated "+session.hashCode());
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("sessionIdle "+session.hashCode()+" , "+status);
    }

    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened "+session.hashCode());
    }

}
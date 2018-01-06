package com.example.wmj.bytheway.ConnSup;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;

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
        ByTheWayActivity.lock.lock();
        String msg = (String) obj;

        ByTheWayActivity.dataResult=msg;//ByTheWayActivity中的dataResult时一个全局变量，用于保存数据库查询结果
        //由于该变量有多线程访问，因而需要加锁进行同步和互斥处理
        ByTheWayActivity.condition.signalAll();
        ByTheWayActivity.lock.unlock();
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
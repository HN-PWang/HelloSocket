package com.kxs.hello_socket.listener;

import com.kxs.hello_socket.Request;

import java.net.Socket;

/**
 * @Author wang.peng
 * @Email 1929774468@qq.com
 * @Date 2020/8/17
 * @Description 连接监听
 */
public interface ConnectListener {

    /**
     * 连接成功
     *
     * @param socket
     * @param request
     */
    void onOpen(Socket socket, Request request);

    /**
     * 消息返回
     *
     * @param socket
     * @param text
     */
    void onMessage(Socket socket, String text);

    /**
     * 处于闲置状态,需要发送心跳保活
     *
     * @param socket
     */
    void onTrigger(Socket socket);

    /**
     * 关闭中
     *
     * @param socket
     * @param code
     * @param reason
     */
    void onClosing(Socket socket, int code, String reason);

    /**
     * 关闭
     *
     * @param socket
     * @param code
     * @param reason
     */
    void onClosed(Socket socket, int code, String reason);

    /**
     * 连接异常
     *
     * @param socket
     * @param throwable
     * @param request
     */
    void onFailure(Socket socket, Throwable throwable, Request request);
}

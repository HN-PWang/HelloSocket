package com.kxs.hello_socket;

import com.kxs.hello_socket.listener.ConnectListener;

import java.net.Socket;

/**
 * @Author wang.peng
 * @Email 1929774468@qq.com
 * @Date 2020/8/18
 * @Description
 */
public class IConnectListener implements ConnectListener {
    @Override
    public void onOpen(Socket socket, Request request) {

    }

    @Override
    public void onMessage(Socket socket, String text) {

    }

    @Override
    public void onTrigger(Socket socket) {

    }

    @Override
    public void onClosing(Socket socket, int code, String reason) {

    }

    @Override
    public void onClosed(Socket socket, int code, String reason) {

    }

    @Override
    public void onFailure(Socket socket, Throwable throwable, Request request) {

    }
}

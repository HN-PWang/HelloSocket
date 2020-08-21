package com.kxs.hello_socket;

/**
 * @Author wang.peng
 * @Email 1929774468@qq.com
 * @Date 2020/8/17
 * @Description 连接状态
 */
public enum ConnectStatus {
    DISCONNECT(0),
    CONNECTING(1),
    CONNECTED(2),
    CLOSING(3);

    public int status;

    ConnectStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

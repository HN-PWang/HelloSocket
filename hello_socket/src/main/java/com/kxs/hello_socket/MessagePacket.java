package com.kxs.hello_socket;

/**
 * @Author wang.peng
 * @Email 1929774468@qq.com
 * @Date 2020/8/20
 * @Description
 */
public class MessagePacket {

    private byte[] data;

    public MessagePacket(byte[] data) {
        this.data = data;
    }

    byte[] getData() {
        return this.data;
    }

}

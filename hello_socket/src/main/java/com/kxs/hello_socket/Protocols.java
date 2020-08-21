package com.kxs.hello_socket;

import java.io.InputStream;

/**
 * @Author wang.peng
 * @Email 1929774468@qq.com
 * @Date 2020/8/19
 * @Description
 */
public interface Protocols {

    void pack();

    byte[] unpack(InputStream is);
}

package com.kxs.hello_socket;

/**
 * @author wang.peng
 * @email 1929774468@qq.com
 * @date 2020/8/17
 */
public class Request {

    private String host;
    private int port;

    private Request(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }

    public static class Builder {
        private String host;
        private int port;

        public Builder setHost(String host, int port) {
            this.host = host;
            this.port = port;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

    @Override
    public String toString() {

        return "host=" + host + "\n" +
                "port=" + port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}

package com.kxs.hello_socket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kxs.hello_socket.listener.ConnectListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wang.peng
 * @email 1929774468@qq.com
 * @date 2020/8/17
 */
public class SocketClient {
    private static final String TAG = SocketClient.class.getSimpleName();

    private static final int WHAT_STATUS_CODE = 0x1001;

    private final boolean tcpNoDelay;
    private final int soTimeout;
    private final boolean soLinger;
    private final int soLingerTime;
    private final int sendBufferSize;
    private final int receiveBufferSize;
    private final boolean keepAlive;
    private final boolean oOBInline;
    private final boolean reuseAddress;

    /**
     * Socket对象
     */
    private Socket mSocket;

    /**
     * 地址管理
     */
    private InetSocketAddress mSocketAddress;

    /**
     * 请求对象
     */
    private Request mRequest;

    /**
     * 连接状态监听
     */
    private ConnectListener mConnectListener;

    /**
     * 连接状态
     */
    private AtomicInteger state = new AtomicInteger(ConnectStatus.DISCONNECT.getStatus());

    /**
     * 发送的消息队列
     */
    private BlockingQueue<MessagePacket> mMessagePackets = new LinkedBlockingDeque<>();

    SocketClient(Builder builder) {
        this.tcpNoDelay = builder.tcpNoDelay;
        this.soTimeout = builder.soTimeout;
        this.soLinger = builder.soLinger;
        this.soLingerTime = builder.soLingerTime;
        this.sendBufferSize = builder.sendBufferSize;
        this.receiveBufferSize = builder.receiveBufferSize;
        this.keepAlive = builder.keepAlive;
        this.oOBInline = builder.oOBInline;
        this.reuseAddress = builder.reuseAddress;
    }

    SocketClient(boolean tcpNoDelay) {
        this(new Builder());
    }

    private Handler clientHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    public void newSocket(Request request, ConnectListener listener) {
        this.mRequest = request;
        this.mConnectListener = listener;

        mSocketAddress = new InetSocketAddress(mRequest.getHost(), mRequest.getPort());

        connect();
    }

    private void sendStatusMessage(int status) {
        Message message = new Message();
        message.what = WHAT_STATUS_CODE;
        message.arg1 = status;
        clientHandler.sendMessage(message);
    }

    public static class Builder {
        int soTimeout = 0;
        int sendBufferSize = 8096;
        int receiveBufferSize = 8096;
        int soLingerTime = 30;
        boolean soLinger = false;
        boolean tcpNoDelay = false;
        boolean keepAlive = false;
        boolean oOBInline = false;
        boolean reuseAddress = false;

        public Builder setTcpNoDelay(boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
            return this;
        }

        public Builder setSoTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setSoLinger(boolean soLinger, int soLingerTime) {
            this.soLinger = soLinger;
            this.soLingerTime = soLingerTime;
            return this;
        }

        public Builder setSendBufferSize(int sendBufferSize) {
            this.sendBufferSize = sendBufferSize;
            return this;
        }

        public Builder setReceiveBufferSize(int receiveBufferSize) {
            this.receiveBufferSize = receiveBufferSize;
            return this;
        }

        public Builder setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder setoOBInline(boolean oOBInline) {
            this.oOBInline = oOBInline;
            return this;
        }

        public Builder setReuseAddress(boolean reuseAddress) {
            this.reuseAddress = reuseAddress;
            return this;
        }

        public SocketClient build() {
            return new SocketClient(this);
        }
    }

    private void connect() {
        try {
            mSocket = new Socket();
            //高可靠性和最小延迟传输
            mSocket.setTrafficClass(0x04 | 0x10);
            mSocket.setOOBInline(oOBInline);
            mSocket.setReuseAddress(reuseAddress);
            mSocket.setKeepAlive(keepAlive);
            mSocket.setSoTimeout(soTimeout);
            mSocket.setReceiveBufferSize(receiveBufferSize);
            mSocket.setSendBufferSize(sendBufferSize);
            mSocket.setTcpNoDelay(tcpNoDelay);
            state.set(ConnectStatus.CONNECTING.getStatus());

            mSocket.connect(mSocketAddress);
            state.set(ConnectStatus.CONNECTED.getStatus());

            if (mConnectListener != null)
                mConnectListener.onOpen(mSocket, mRequest);
        } catch (IOException e) {
            state.set(ConnectStatus.DISCONNECT.getStatus());

            if (mConnectListener != null)
                mConnectListener.onFailure(mSocket, e, mRequest);
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 发送消息
     *
     * @param data
     */
    public void send(byte[] data) {
        MessagePacket messagePacket = new MessagePacket(data);
        try {
            mMessagePackets.put(messagePacket);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!mSocket.isConnected()) {
            state.set(ConnectStatus.DISCONNECT.getStatus());
        }
    }

    /**
     * 重新连接
     */
    public void reconnect() {
        disconnect();
        connect();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.shutdownInput();
                mSocket.shutdownOutput();
                mSocket.close();
            }

            mSocket = null;
            state.set(ConnectStatus.DISCONNECT.getStatus());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public boolean isSoLinger() {
        return soLinger;
    }

    public int getSoLingerTime() {
        return soLingerTime;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public boolean isoOBInline() {
        return oOBInline;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public InetSocketAddress getmSocketAddress() {
        return mSocketAddress;
    }

    public Request getmRequest() {
        return mRequest;
    }

    public AtomicInteger getState() {
        return state;
    }
}

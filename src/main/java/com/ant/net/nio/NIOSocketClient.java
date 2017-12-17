package com.ant.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @DESCRIPTION 每次请求都需要一个SocketChannel，就是说每次请求都会生成一个channel对象，然后所有的后续操作
 * 都是针对这个channel中数据操作
 * @author hh
 * @create 2017-12-10
 */
public class NIOSocketClient extends Thread{

    private SocketChannel socketChannel ;

    private Selector selector ;

    private int Port ;

    public NIOSocketClient(int port) {
        Port = port;
    }

    public static void main(String[] args) {
        NIOSocketClient nioSocketClient = new NIOSocketClient(1521);
        try{
            nioSocketClient.initClient();
            nioSocketClient.start();
        }catch (IOException e){
            e.printStackTrace();
            nioSocketClient.stopServer();
        }
    }

    private void stopServer() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initClient() throws IOException{
        InetSocketAddress inetSocketAddress = new InetSocketAddress(Port);
        socketChannel = SocketChannel.open();
        selector = Selector.open() ;
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        socketChannel.connect(inetSocketAddress);
        while(!socketChannel.finishConnect()){
            System.out.println("check finish connection");
        }
    }

    public void run () {
        try{
          writeMessage();
        }catch (IOException e){

        }
       while(true){
            try{
                int select = selector.select() ;
                if( select > 0 ){
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys() ;
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeySet.iterator();
                    while (selectionKeyIterator.hasNext()){
                        SelectionKey selectionKey = selectionKeyIterator.next() ;
                        if(selectionKey.isValid()){
                            if(selectionKey.isReadable()){
                                readMessage(selectionKey);
                            }
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                if (socketChannel.isOpen()){
                    try {
                        socketChannel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }


    }

    private void writeMessage() throws IOException {
        /**
         * 包协议规定：
         *   长度 + 数值
         *   数据包的总长 =   长度所占的字节（4） + 数据的长度
         */
        String msg = "how are you !!!";
        //数据的长度
        byte[] bytes = msg.getBytes() ;
       /* ByteBuffer buffer = ByteBuffer.allocate( 4 + bytes.length );
        //第一个写数据的长度，4字节
        buffer.putInt(bytes.length);
        buffer.put(bytes);*/
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes("UTF-8"));
        while (buffer.hasRemaining()) {
            System.out.println("buffer.hasRemaining() is true.");
            socketChannel.write(buffer);
        }
    }

    private void readMessage(SelectionKey selectionKey) throws IOException{
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            System.out.println("Receive from server:"
                    + new String(buffer.array(), "UTF-8"));
            buffer.clear();
        }

    }


}

package com.ant.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOSocketServer extends Thread{

    private Selector selector ;

    private ServerSocketChannel serverSocketChannel ;

    private int Port ;

    public NIOSocketServer(int port) {
        Port = port;
    }

    public static void main(String[] args) {
        System.out.println(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        NIOSocketServer nioSocketServer = new NIOSocketServer(1521);
        try{
            nioSocketServer.initServer();
            nioSocketServer.start();
        }catch (IOException e){
            e.printStackTrace();
            nioSocketServer.stopServer();
        }
    }

    private void stopServer() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initServer() throws IOException{
        selector = Selector.open() ;
        serverSocketChannel = ServerSocketChannel.open() ;
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(Port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    }

    public void  run() {
        while(true){
            System.out.println("处理各种请求");
            try{
                int select = selector.select();
                if( select > 0 ){
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterable = selectionKeySet.iterator();
                    while (selectionKeyIterable.hasNext()){
                        SelectionKey selectionKey =  selectionKeyIterable.next() ;
                        if(selectionKey.isValid()){
                            if(selectionKey.isAcceptable()){
                                doAcceptable(selectionKey);
                            }
                            if(selectionKey.isConnectable()){
                                doConnectable(selectionKey);
                            }
                            if(selectionKey.isReadable()){
                                doReadable(selectionKey);
                            }
                            if(selectionKey.isWritable()){
                                doWritable(selectionKey);
                            }
                            selectionKeyIterable.remove();
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void doWritable(SelectionKey selectionKey) throws IOException{
        System.out.println("is writable");
        SocketChannel selectableChannel =  (SocketChannel)selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.wrap("how are you?".getBytes("UTF-8"));
        while (buffer.hasRemaining()) {
            selectableChannel.write(buffer);
        }
    }

    private void doConnectable(SelectionKey selectionKey) {
        System.out.println("is connectalbe");
    }

    private void doReadable(SelectionKey selectionKey) throws IOException{
        System.out.println("is readable");
        SocketChannel selectableChannel =  (SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        int read = selectableChannel.read(byteBuffer);
        while (read > 0) {
            byteBuffer.flip();
            byte[] barr = new byte[byteBuffer.limit()];
            byteBuffer.get(barr);
            System.out.print(new String(barr, "UTF-8"));
            byteBuffer.clear();
            read = selectableChannel.read(byteBuffer);
        }
    }

    private void doAcceptable(SelectionKey selectionKey) throws IOException{
        System.out.println("is acceptable");
        ServerSocketChannel selectableChannel =  (ServerSocketChannel)selectionKey.channel();
        SocketChannel socketChannel = selectableChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;

/**
 *
 * @author dakata
 */
public class UnixSocketServer {

    private final UnixDomainSocketAddress address;

    public UnixSocketServer(Path socketFile) {
        this.address = UnixDomainSocketAddress.of(socketFile);
    }

    public void start() {
        synchronized (this) {
            ServerSocketChannel serverChannel;
            Thread t = new Thread(UnixSocketServer.this.toString() + "listnening thread") {
                @Override
                public void run() {
                    try {
                        listen();
                    } catch (Exception e) {
                        onListenException(e);
                    }
                }

            };
            t.start();
        }

    }

    private void listen() {
        synchronized (this) {
            ServerSocketChannel serverChannel;
            try {
                serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
                serverChannel.bind(address);
                System.out.println("[INFO] Waiting for client to connect");
                Thread t = new Thread("Listen Thread") {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                SocketChannel channel = serverChannel.accept();
                                ByteBuffer buff = ByteBuffer.allocate(1024);
                                channel.read(buff);
                                buff.flip();
                                String s = StandardCharsets.UTF_8.decode(buff).toString();
                                System.out.println("Received: " + s);
                            } catch (IOException e) {
                                onListenException(e);
                            }
                        }
                    }
                };
                t.start();
            } catch (IOException ex) {
                onListenException(ex);

            }
        }

    }

    private void onListenException(Exception e) {
        System.out.println("Listen exception " + Arrays.toString(e.getStackTrace()));
    }

}

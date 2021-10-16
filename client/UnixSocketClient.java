/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

/**
 *
 * @author dakata
 */
public class UnixSocketClient {
     private UnixDomainSocketAddress address;
     private SocketChannel clientChannel;
     
     public UnixSocketClient(Path socketFile){
         this.address = UnixDomainSocketAddress.of(socketFile);
     }
     
     
    public void connect() throws IOException{
        this.clientChannel = SocketChannel.open(address);
    }
    public void send_message(String message) throws IOException{
        ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
        clientChannel.write(buf);
    }
            
                
}

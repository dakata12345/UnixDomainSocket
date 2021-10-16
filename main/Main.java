/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import client.UnixSocketClient;
import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import server.UnixSocketServer;

/**
 *
 * @author dakata
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        Path socketFile = Path.of(System.getProperty("user.home"))
                .resolve("server.socket");
        UnixDomainSocketAddress address = UnixDomainSocketAddress.of(socketFile);
        Files.deleteIfExists(address.getPath());
        UnixSocketClient client = new UnixSocketClient(socketFile);
        UnixSocketServer server = new UnixSocketServer(socketFile);
        server.start();
        Thread.sleep(1000);
        client.connect();
        client.send_message("Hello");
        
        UnixSocketClient client2 = new UnixSocketClient(socketFile);
        client2.connect();
        
        client2.send_message("It's me Picasso");
        
        UnixSocketClient client3 = new UnixSocketClient(socketFile);
        client3.connect();
        client3.send_message("Hello World");

    }

}

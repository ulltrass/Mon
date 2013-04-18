package com.network.monitor.service;

import com.network.monitor.domain.GeneralInfo;
import com.network.monitor.domain.Server;
import com.network.monitor.domain.ServerInfo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import org.apache.log4j.Logger;

public class RegisterServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(RegisterServer.class);
    private DatagramSocket socket;
    private int infoPort;

    public RegisterServer(DatagramSocket ds, int infoPort) {
        socket = ds;
        this.infoPort = infoPort;

    }

    @Override
    public void run() {

        LOGGER.info("Register server started...");
        while (true) {
            
            byte[] buffer = new byte[1024];
            byte[] bufferOut = new byte[1024];
            String remoteIp = "";

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                remoteIp = packet.getAddress().toString().split("/")[1];
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            InetAddress client = packet.getAddress();
            LOGGER.info("Register Server Received package from: " + client.toString() + " on port: " + packet.getPort());
            int client_port = packet.getPort();

            String message = new String(buffer).trim();

            if (message.startsWith("RegisterMe")) {

                String u = message;
                message = "RegisteredOK;" + infoPort + ";";
                bufferOut = message.getBytes();
                packet = new DatagramPacket(bufferOut, bufferOut.length, client, client_port);
                LOGGER.info("Send Hub confirmation to  " + message + " -- " + client + " using port " + client_port + "\n");

                try {
                    socket.send(packet);

                } catch (IOException e) {
                    LOGGER.error("ERROR SENDIG CONFIRMATION PACKAGE TO " + client);
                    LOGGER.error(e.getMessage(), e);
                }

                if (u.startsWith("RegisterMe")) {
                    Server server = new Server();
                    ServerInfo serverInfo = new ServerInfo();
                    GeneralInfo generalInfo = new GeneralInfo();

                    generalInfo.setIpv4Address(client.getHostAddress());

                }
                
            }
            
            


        }

    }
}

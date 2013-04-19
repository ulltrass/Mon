/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.network.monitor.service;

import com.network.monitor.domain.Server;
import com.network.monitor.view.MainForm;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Ovi
 */
public class PeriodicalCheckService implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(PeriodicalCheckService.class);
    MainForm mainForm;
    DatagramSocket checkSocket;
    HealthCheckService healthCheckService;
    private static final int PORT = 5555;

    public PeriodicalCheckService(MainForm mainForm, DatagramSocket checkSocket) {
        this.mainForm = mainForm;
        this.checkSocket = checkSocket;
        healthCheckService = new HealthCheckService(mainForm);
    }

    public void run() {
        List<Server> downServers = new ArrayList<Server>();
        while (true) {
            List<Server> servers = mainForm.getServersList();

            LOGGER.info("*******Server List: " + servers.size());
            for (Server server : servers) {
                try {
                    InetAddress client = InetAddress.getByName(server.getServerInfo().getGeneralInfo().getIpv4Address());
                    byte[] bufferOut = "Check".getBytes();
                    DatagramPacket packet = new DatagramPacket(bufferOut, bufferOut.length, client, PORT);

                    LOGGER.info("****Send Check to  -- " + client + " using port " + PORT + "\n");

                    checkSocket.send(packet);
                    checkSocket.setSoTimeout(10000);
                    checkSocket.receive(packet);

                } catch (Exception ex) {
                    LOGGER.error("*****ERROR Receiving Check status PACKAGE");
                    LOGGER.error(ex.getMessage(), ex);
                    downServers.add(server);
                }
            }
            if (!downServers.isEmpty()) {
                healthCheckService.notifyServerDownEvent(downServers);
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}

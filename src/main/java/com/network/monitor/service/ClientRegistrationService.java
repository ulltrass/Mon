package com.network.monitor.service;

import com.network.monitor.view.MainForm;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public class ClientRegistrationService implements Runnable {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ClientRegistrationService.class);
    RegisterServer registerServer;
    DatagramSocket registerServSocket;
    DatagramSocket echoSocket;
    ServerSocket monitoringServSocket;
    MonitoringInfoServer monitoringInfoServer;
    PeriodicalCheckService periodicalCheckService;
    MainForm mainForm;

    public ClientRegistrationService(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    @Override
    public void run() {
        try {
            registerServSocket = new DatagramSocket(4445);
            monitoringServSocket = new ServerSocket(4446);
            echoSocket = new DatagramSocket(4448);
        } catch (SocketException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        registerServer = new RegisterServer(registerServSocket, 4446);
        Thread registerThread = new Thread(registerServer);
        registerThread.start();

        monitoringInfoServer = new MonitoringInfoServer(monitoringServSocket, mainForm);
        Thread monitoringThread = new Thread(monitoringInfoServer);
        monitoringThread.start();

        periodicalCheckService = new PeriodicalCheckService(mainForm, echoSocket);
        Thread checkThread = new Thread(periodicalCheckService);
        checkThread.start();

    }
}

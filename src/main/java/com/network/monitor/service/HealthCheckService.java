/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.network.monitor.service;

import com.network.monitor.controller.NotificationController;
import com.network.monitor.domain.DriveUsage;
import com.network.monitor.domain.EventLog;
import com.network.monitor.domain.EventType;
import com.network.monitor.domain.Server;
import com.network.monitor.view.MainForm;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ovi
 */
public class HealthCheckService {

    private static final String GENERAL_USAGE_MESSAGE = "%s usage is %d%%";
    private NotificationController notificationController = new NotificationController();
    private MainForm mainForm;

    public HealthCheckService(MainForm mainForm) {
        this.mainForm = mainForm;
    }

    public void checkServer(Server server) {
        List<EventLog> eventLogs = new ArrayList<EventLog>();
        if (server != null) {
            EventLog eventLog = checkCpuUsage(server);
            if (eventLog != null) {
                eventLogs.add(eventLog);
                addLogToForm(eventLog);
                eventLog = null;
            }
            eventLog = checkMemoryUsage(server);
            if (eventLog != null) {
                eventLogs.add(eventLog);
                addLogToForm(eventLog);
                eventLog = null;
            }
            eventLog = checkDiskUsage(server);
            if (eventLog != null) {
                eventLogs.add(eventLog);
                addLogToForm(eventLog);
                eventLog = null;
            }
        }
//        notifyEvents(eventLogs);
    }

    private EventLog checkCpuUsage(Server server) {
        EventLog eventLog = null;
        Integer cpuUsage = server.getServerInfo().getCpuUsage();
        System.out.println("CPU: " + server.getServerInfo().getCpuUsage());
        if (cpuUsage >= 50 && cpuUsage < 70) {
            eventLog = createEventLog(EventType.INFO,
                    String.format(GENERAL_USAGE_MESSAGE, "Cpu", cpuUsage), server.getServerName());
        } else if (cpuUsage >= 80 && cpuUsage < 90) {
            eventLog = createEventLog(EventType.WARNING,
                    String.format(GENERAL_USAGE_MESSAGE, "Cpu", cpuUsage), server.getServerName());
        } else if (cpuUsage >= 90) {
            eventLog = createEventLog(EventType.CRITICAL,
                    String.format(GENERAL_USAGE_MESSAGE, "Cpu", cpuUsage), server.getServerName());
        }
        return eventLog;
    }

    private EventLog checkMemoryUsage(Server server) {
        EventLog eventLog = null;
        Integer memoryUsage = server.getServerInfo().getMemoryUsage();

        if (memoryUsage >= 50 && memoryUsage < 70) {
            eventLog = createEventLog(EventType.INFO,
                    String.format(GENERAL_USAGE_MESSAGE, "Memory", memoryUsage), server.getServerName());
        } else if (memoryUsage >= 80 && memoryUsage < 90) {
            eventLog = createEventLog(EventType.WARNING,
                    String.format(GENERAL_USAGE_MESSAGE, "Memory", memoryUsage), server.getServerName());
        } else if (memoryUsage >= 90) {
            eventLog = createEventLog(EventType.CRITICAL,
                    String.format(GENERAL_USAGE_MESSAGE, "Memory", memoryUsage), server.getServerName());
        }
        return eventLog;
    }

    private EventLog checkDiskUsage(Server server) {
        EventLog eventLog = null;

        for (DriveUsage driveUsage : server.getServerInfo().getDiskUsage().getDriveUsages()) {
            try {
                Integer usageInMb = (int) Double.parseDouble(driveUsage.getCapacity()) - (int) Double.parseDouble(driveUsage.getFreeSpace());
                Integer capacity = (int) Double.parseDouble(driveUsage.getCapacity());
                Integer partitionUsage = (usageInMb * 100) / capacity;

                if (partitionUsage >= 50 && partitionUsage < 70) {
                    eventLog = createEventLog(EventType.INFO,
                            String.format(GENERAL_USAGE_MESSAGE, "Partition " + driveUsage.getDriveLetter(), partitionUsage), server.getServerName());
                } else if (partitionUsage >= 80 && partitionUsage < 90) {
                    eventLog = createEventLog(EventType.WARNING,
                            String.format(GENERAL_USAGE_MESSAGE, "Partition " + driveUsage.getDriveLetter(), partitionUsage), server.getServerName());
                } else if (partitionUsage >= 90) {
                    eventLog = createEventLog(EventType.CRITICAL,
                            String.format(GENERAL_USAGE_MESSAGE, "Partition " + driveUsage.getDriveLetter(), partitionUsage), server.getServerName());
                }
            } catch (Exception ex) {
            }

        }

        return eventLog;
    }

    private EventLog createEventLog(EventType eventType, String message, String serverName) {
        EventLog eventLog = new EventLog();
        eventLog.setEventTime(new Timestamp(System.currentTimeMillis()));
        eventLog.setEventType(eventType);
        eventLog.setMessage(message);
        eventLog.setServerName(serverName);

        return eventLog;
    }

    private void notifyEvents(List<EventLog> eventLogs) {
        if (!eventLogs.isEmpty()) {
            notificationController.notifyContacts(eventLogs);
        }
    }

    private void addLogToForm(EventLog eventLog) {
        mainForm.addEvent(eventLog.getEventTime().toString(), eventLog.getServerName(), eventLog.getMessage(),
                eventLog.getEventType().getMessage(), "Email&SMS");
    }
}

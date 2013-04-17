/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.network.monitor.controller;

import com.network.monitor.domain.Contact;
import com.network.monitor.domain.EmailSettings;
import com.network.monitor.domain.EventLog;
import com.network.monitor.domain.Setting;
import com.network.monitor.service.EmailNotificationService;
import com.network.monitor.service.SMSNotificationService;
import java.util.List;

/**
 *
 * @author Ovi
 */
public class NotificationController {

    ContactController contactController = new ContactController();
    EmailNotificationService emailNotificationService = new EmailNotificationService();
    SMSNotificationService smsNotificationService = new SMSNotificationService();
    SettingsController settingsController = new SettingsController();

    public void notifyContacts(List<EventLog> eventLogs) {
        String subject = "Monitoring: " + eventLogs.get(0).getEventType().getMessage();
        String message = eventLogs.get(0).getServerName() + ": \n";
        for (EventLog eventLog : eventLogs) {
            message = message + eventLog.getEventType().getMessage() + ": "
                    + eventLog.getMessage();
        }

        Setting setting = settingsController.getSettings();
        List<Contact> contacts = contactController.getContactsWithSMSNumberSet();
        contactController.sendSmsToContacts(setting, contacts, message);
        
        List<EmailSettings> emailSettings = settingsController.getSettings().getEmailSettings();
        contacts = contactController.getContactsWithEmailAddressSet();
        contactController.sendEmailToContacts(emailSettings, contacts, subject, message);
    }
}

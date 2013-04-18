/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.network.monitor.controller;

import com.network.monitor.domain.Contact;
import com.network.monitor.domain.EmailSettings;
import com.network.monitor.domain.Server;
import com.network.monitor.domain.Setting;
import com.network.monitor.service.ContactService;
import com.network.monitor.service.DiskMonitorService;
import com.network.monitor.service.EmailNotificationService;
import com.network.monitor.service.SMSNotificationService;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Ovi
 */
public class ContactController {

    private static final Logger LOGGER = Logger.getLogger(ContactController.class);
    ContactService contactService = new ContactService();
    SMSNotificationService smsNotificationService = new SMSNotificationService();
    EmailNotificationService emailNotificationService = new EmailNotificationService();
    DiskMonitorService diskMonitorService = new DiskMonitorService();

    public void saveContacts(List<Contact> contacts) {
        contactService.saveContacts(contacts);
    }

    public List<Contact> getContacts() {
        return contactService.getContacts();
    }

    public List<Contact> getContactsWithSMSNumberSet() {
        return contactService.getContactsWithSMSNumberSet();
    }

    public List<Contact> getContactsWithEmailAddressSet() {
        return contactService.getContactsWithEmailAddressSet();
    }

    public void sendSmsToContacts(Setting setting, List<Contact> contacts, String message) {
        smsNotificationService.sendSMSToContacts(setting, contacts, message);
    }

    public void sendEmailToContacts(List<EmailSettings> emailSettings, List<Contact> contacts,
            String subject, String message) {
        List<Server> servers = diskMonitorService.getDiskUsageForAllServers();
        boolean success = false;
        for (EmailSettings emailSetting : emailSettings) {
            if (emailSetting.isDefaultEmail()) {
                try {
                    emailNotificationService.sendEmail(emailSetting.getServerHost(),
                            emailSetting.getServerPort(), emailSetting.getUserName(),
                            emailSetting.getPassword(), subject, message, contacts);
                    success = true;
                    break;
                } catch (Exception e) {
                    LOGGER.error("Error sending mail");
                    LOGGER.error(e.getMessage(), e);
                    success = false;
                }
            }
        }

        if (!success) {
            for (EmailSettings emailSetting : emailSettings) {
                if (!success) {
                    try {
                        emailNotificationService.sendEmail(emailSetting.getServerHost(),
                                emailSetting.getServerPort(), emailSetting.getUserName(),
                                emailSetting.getPassword(), subject, message, contacts);
                        success = true;
                        break;
                    } catch (Exception e) {
                        LOGGER.error("Error sending mail");
                        LOGGER.error(e.getMessage(), e);
                        success = false;
                    }
                }
            }
        }

    }
}

package com.network.monitor.controller;

import com.network.monitor.domain.Setting;
import com.network.monitor.service.ContactService;
import com.network.monitor.service.SMSNotificationService;
import org.apache.log4j.Logger;


/**
 *
 * @author 
 */
public class TestController {

    private static final Logger LOGGER = Logger.getLogger(TestController.class);
    ContactService contactService = new ContactService();
    SMSNotificationService smsNotificationService = new SMSNotificationService();

    public void sendStarhubTestSMS(Setting setting, String phoneNumber, String senderName, String testMessage) {
        try {
            smsNotificationService.sendStarhubTestSMS(setting, phoneNumber, senderName, testMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public void sendSingtelTestSMS(Setting setting, String phoneNumber, String senderName, String testMessage) {
        try {
            smsNotificationService.sendSingtelTestSMS(setting, phoneNumber, senderName, testMessage);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}

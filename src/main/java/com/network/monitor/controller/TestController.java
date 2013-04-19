package com.network.monitor.controller;

import com.network.monitor.domain.Setting;
import com.network.monitor.service.ContactService;
import com.network.monitor.service.SMSNotificationService;

/**
 *
 * @author 
 */
public class TestController {

    ContactService contactService = new ContactService();
    SMSNotificationService smsNotificationService = new SMSNotificationService();

    public void sendStarhubTestSMS(Setting setting, String phoneNumber, String senderName, String testMessage) {
        smsNotificationService.sendStarhubTestSMS(setting, phoneNumber, senderName, testMessage);
    }

    public void sendSingtelTestSMS(Setting setting, String phoneNumber, String senderName, String testMessage) {
        smsNotificationService.sendSingtelTestSMS(setting, phoneNumber, senderName, testMessage);
    }
}

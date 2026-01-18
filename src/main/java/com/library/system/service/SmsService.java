package com.library.system.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service responsible for sending SMS messages using the Twilio API.
 * Supports sending OTPs and custom messages to users.
 */
@Service
public class SmsService {

     // Twilio Account SID, injected from application.properties.
     
    @Value("${twilio.account.sid}")
    private String accountSid;

     // Twilio Auth Token, injected from application.properties.
     
    @Value("${twilio.auth.token}")
    private String authToken;

     // Twilio phone number used to send outgoing SMS.
     
    @Value("${twilio.phone.number}")
    private String fromNumber;

    
     // Sends an OTP to the specified mobile number.
  
    public void sendOtp(String to, String otp) {
        Twilio.init(accountSid, authToken);

        if (!to.startsWith("+")) {
            to = "+91" + to;
        }

        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(fromNumber),
                "Your Library OTP is: " + otp
        ).create();

        System.out.println("Sent OTP to: " + to); //printing on console
    }

    
     // Sends a custom message to the specified mobile number.
  
    public void sendMessage(String to, String body) {
        Twilio.init(accountSid, authToken);

        if (!to.startsWith("+")) {
            to = "+91" + to;
        }

        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(fromNumber),
                body
        ).create();

        System.out.println("Sent message to: " + to);
    }
}

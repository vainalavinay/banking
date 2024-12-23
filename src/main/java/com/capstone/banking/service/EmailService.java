package com.capstone.banking.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService 
{
	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}
	
	public void sendNotification(String toEmail, String subject, String body)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom("testing@gmail.com");
		mailSender.send(message);
	}
	
}

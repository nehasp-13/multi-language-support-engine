package com.internship.tool.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendDailyReminder(String toEmail, String userName) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("message", "Don't forget to check your translations today!");
            String html = templateEngine.process("daily-reminder", context);
            sendHtmlEmail(toEmail, "Daily Reminder — Multi-Language Support Engine", html);
            log.info("Daily reminder sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send daily reminder to {}: {}", toEmail, e.getMessage());
        }
    }

    @Async
    public void sendDeadlineAlert(String toEmail, String userName, String deadline) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("deadline", deadline);
            context.setVariable("message", "Your translation deadline is approaching!");
            String html = templateEngine.process("deadline-alert", context);
            sendHtmlEmail(toEmail, "Deadline Alert — Multi-Language Support Engine", html);
            log.info("Deadline alert sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send deadline alert to {}: {}", toEmail, e.getMessage());
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }
}
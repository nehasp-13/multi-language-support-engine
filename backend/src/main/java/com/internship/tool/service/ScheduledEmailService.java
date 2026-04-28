package com.internship.tool.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledEmailService {

    private final EmailService emailService;

    // Daily reminder — runs every day at 9:00 AM
    @Scheduled(cron = "0 0 9 * * MON-FRI")
    public void sendDailyReminders() {
        log.info("Running daily reminder job...");
        // In real app, fetch all users from DB and send to each
        // For now sending to demo email
        emailService.sendDailyReminder(
            "demo@example.com",
            "Team Member"
        );
        log.info("Daily reminder job completed");
    }

    // Deadline alert — runs every day at 5:00 PM
    @Scheduled(cron = "0 0 17 * * MON-FRI")
    public void sendDeadlineAlerts() {
        log.info("Running deadline alert job...");
        emailService.sendDeadlineAlert(
            "demo@example.com",
            "Team Member",
            "Tomorrow 6:00 PM"
        );
        log.info("Deadline alert job completed");
    }
}
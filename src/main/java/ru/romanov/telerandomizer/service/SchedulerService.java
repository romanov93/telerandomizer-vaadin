package ru.romanov.telerandomizer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.romanov.telerandomizer.csvwork.PhoneRangeUpdater;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final PhoneRangeUpdater phoneRangeUpdater;

    @Scheduled(cron = "@weekly")
    @EventListener(ApplicationReadyEvent.class)
    public void searchNewPhoneRanges() {
        phoneRangeUpdater.update();
    }
}
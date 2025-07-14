package com.akash.journalApp.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.akash.journalApp.cache.AppCache;
import com.akash.journalApp.entity.JournalEntry;
import com.akash.journalApp.entity.User;
import com.akash.journalApp.enums.Sentiment;
import com.akash.journalApp.repository.UserRepositoryImpl;
import com.akash.journalApp.service.EmailService;
import com.akash.journalApp.service.SentimentAnalysisService;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "* * * ? * *")
    public void fetchUserAndSendMails() {
        List<User> users = userRepositoryImpl.getUserForSA();

        for(User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntryList();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();

            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
                }
            }

            Sentiment mostFrequent = null;
            int maxCount = 0;
            
            for(Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequent = entry.getKey();
                }
            }
            if(mostFrequent != null) {
                emailService.sendMail(user.getEmail(), "Sentiment Analysis for last 7 days.", mostFrequent.toString());
            }
        }
    }

    @Scheduled(cron = "0 5 * ? * *")
    public void clearCache() {
        appCache.init();
    }
}

package com.akash.journalApp.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akash.journalApp.entity.ConfigJournalAppEntity;
import com.akash.journalApp.repository.ConfigJournalAppRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API;
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> list = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity entry : list) {
            appCache.put(entry.getKey(), entry.getValue());
        }
    }
    
}

package com.akash.journalApp.controller;

import com.akash.journalApp.entity.JournalEntry;
import com.akash.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @GetMapping("id/{id}")
    public JournalEntry getById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id).orElse(null);
    }

    @PostMapping
    public JournalEntry addJournals(@RequestBody JournalEntry journal) {
        journal.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journal);
        return journal;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournal(@PathVariable ObjectId id, @RequestBody JournalEntry journal) {
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null) {
            old.setContent(journal.getContent() != null && ! journal.getContent().equals("") ? journal.getContent() : old.getContent());
            old.setName(journal.getName() != null && ! journal.getName().equals("") ? journal.getName() : old.getName());
        }
        journalEntryService.saveEntry(old);
        return old;
    }

    @DeleteMapping("id/{id}")
    public boolean deleteJournalById(@PathVariable ObjectId id) {
        journalEntryService.deleteById(id);
        return true;
    }
}

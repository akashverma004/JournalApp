package com.akash.journalApp.controller;

import com.akash.journalApp.entity.JournalEntry;
import com.akash.journalApp.entity.User;
import com.akash.journalApp.service.JournalEntryService;
import com.akash.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllofUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntryList();
        if( all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.findById(id);
        if(entry.isPresent()) {
            return new ResponseEntity<>(entry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName]")
    public ResponseEntity<JournalEntry> addJournals(@RequestBody JournalEntry journal, @PathVariable String userName) {

        try {
            journalEntryService.saveEntry(journal, userName);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("id/{userName}/{id}")
    public ResponseEntity<?> updateJournal(@PathVariable ObjectId id, String userName, @RequestBody JournalEntry journal) {
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null) {
            old.setContent(journal.getContent() != null && ! journal.getContent().equals("") ? journal.getContent() : old.getContent());
            old.setName(journal.getName() != null && ! journal.getName().equals("") ? journal.getName() : old.getName());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("id/{userName}/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id, String userName) {
        journalEntryService.deleteById(id, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

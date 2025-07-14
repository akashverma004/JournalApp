package com.akash.journalApp.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.akash.journalApp.enums.Sentiment;

import java.time.LocalDateTime;

@Document(collection = "journal")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    private ObjectId id;

    @NonNull
    private String name;

    private String content;

    private LocalDateTime date;

    private Sentiment sentiment;
}

package com.akash.journalApp.repository;

import com.akash.journalApp.entity.JournalEntry;
import com.akash.journalApp.entity.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUserName(String username);
}



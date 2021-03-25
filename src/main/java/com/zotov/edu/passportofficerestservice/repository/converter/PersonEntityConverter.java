package com.zotov.edu.passportofficerestservice.repository.converter;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class PersonEntityConverter {

    public Person convertToEntity(String name, LocalDate birthday, String country) {
        return convertToEntity(
                UUID.randomUUID().toString(),
                name,
                birthday,
                country);
    }

    public Person convertToEntity(String id, String name, LocalDate birthday, String country) {
        return new Person(
                id,
                name,
                birthday,
                country);
    }
}

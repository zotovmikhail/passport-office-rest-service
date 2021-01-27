package com.zotov.edu.passportofficerestservice.converter;

import com.zotov.edu.passportofficerestservice.model.entity.Person;
import com.zotov.edu.passportofficerestservice.model.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.response.PersonResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PersonConverter {
    Page<PersonResponse> convertToDto(Page<Person> personPage);

    PersonResponse convertToDto(Person person);

    List<PersonResponse> convertToDto(List<Person> persons);

    Person convertToEntity(PersonRequest personRequest);

    Person convertToEntity(String id, PersonRequest person);
}

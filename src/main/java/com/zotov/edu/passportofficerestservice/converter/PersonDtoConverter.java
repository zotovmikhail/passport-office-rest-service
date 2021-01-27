package com.zotov.edu.passportofficerestservice.converter;

import com.zotov.edu.passportofficerestservice.model.entity.Person;
import com.zotov.edu.passportofficerestservice.model.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.response.PersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PersonDtoConverter implements PersonConverter {

    @Override
    public Page<PersonResponse> convertToDto(Page<Person> personPage) {
        List<PersonResponse> personResponses = convertToDto(personPage.getContent());
        return new PageImpl<>(personResponses);
    }

    @Override
    public PersonResponse convertToDto(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getName(),
                person.getBirthday(),
                person.getCountry());
    }

    @Override
    public List<PersonResponse> convertToDto(List<Person> persons) {
        return persons.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Person convertToEntity(PersonRequest personRequest) {
        return new Person(
                UUID.randomUUID().toString(),
                personRequest.getName(),
                personRequest.getBirthday(),
                personRequest.getCountry().toUpperCase());
    }

    @Override
    public Person convertToEntity(String id, PersonRequest person) {
        return new Person(
                id,
                person.getName(),
                person.getBirthday(),
                person.getCountry().toUpperCase());
    }
}

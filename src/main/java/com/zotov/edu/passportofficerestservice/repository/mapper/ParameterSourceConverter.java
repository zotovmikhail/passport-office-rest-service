package com.zotov.edu.passportofficerestservice.repository.mapper;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParameterSourceConverter {

    public MapSqlParameterSource convertPerson(Person person) {
        return new MapSqlParameterSource()
                .addValue("id", person.getId())
                .addValue("name", person.getName())
                .addValue("birthday", person.getBirthday())
                .addValue("country", person.getCountry());
    }

    public SqlParameterSource[] convertPersons(List<Person> personsToAdd) {
        return personsToAdd
                .stream()
                .map(this::convertPerson)
                .toArray(SqlParameterSource[]::new);
    }

    public MapSqlParameterSource convertPassport(Passport passport) {
        return new MapSqlParameterSource()
                .addValue("number", passport.getNumber())
                .addValue("given_date", passport.getGivenDate())
                .addValue("department_code", passport.getDepartmentCode())
                .addValue("state", passport.getState().getDatabaseName())
                .addValue("owner_id", passport.getOwnerId());
    }

}

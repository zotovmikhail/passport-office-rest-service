package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.repository.mapper.PersonRowMapper;
import com.zotov.edu.passportofficerestservice.service.exception.PersonNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "jdbc")
public class PersonsRepositoryJdbc implements PersonsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PersonRowMapper personRowMapper;

    @Override
    public Page<Person> findAll(Pageable pageable) {
        List<Person> personsFromData = jdbcTemplate.query("select * from persons limit ? offset ?", personRowMapper,
                pageable.getPageSize(), pageable.getOffset());

        long numberOfPersons = Optional.ofNullable(
                jdbcTemplate.queryForObject("select count(*) from persons", Long.class))
                .orElse(0L);

        return new PageImpl<>(personsFromData, pageable, numberOfPersons);
    }

    @Override
    public Person save(Person person) {
        jdbcTemplate.update("update persons set name =?, birthday =?, country =? where id =?",
                person.getName(), person.getBirthday(), person.getCountry(), person.getId());

        return person;
    }

    @Override
    public Person create(Person person) {
        jdbcTemplate.update("insert into persons(id, name, birthday, country) values(?, ?, ?, ?)",
                person.getId(), person.getName(), person.getBirthday(), person.getCountry());

        return person;
    }

    @Override
    public Optional<Person> findById(String id) {
        List<Person> foundPersons = jdbcTemplate.query("select * from persons where id =?", personRowMapper, id);

        return foundPersons.stream().findFirst();
    }

    @Override
    public Boolean existsById(String id) {
        return jdbcTemplate.queryForObject("select exists(select * from persons where id=?)", Boolean.class, id);
    }

    @Override
    public void deleteById(String id) {
        int numberOfDeletedEntities = jdbcTemplate.update("delete from persons where id=?", id);

        if (numberOfDeletedEntities == 0) {
            throw new PersonNotFoundException(id);
        }
    }

    @Override
    public void saveAll(List<Person> personsToAdd) {
        List<Object[]> parametersToUpdate = personsToAdd
                .stream()
                .map(person -> new Object[]{person.getId(), person.getName(), person.getBirthday(), person.getCountry()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into persons values(?, ? ,? ,?)", parametersToUpdate);
    }
}

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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "jdbc")
public class PersonsRepositoryJdbc implements PersonsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final PersonRowMapper personRowMapper;

    @Override
    public Page<Person> findAll(Pageable pageable) {
        List<Person> personsFromData = namedParameterJdbcTemplate.query("select * from persons limit :size offset :offset",
                Map.of("size", pageable.getPageSize(), "offset", pageable.getPageNumber()), personRowMapper);

        long numberOfPersons = Optional.ofNullable(
                jdbcTemplate.queryForObject("select count(*) from persons", Long.class))
                .orElse(0L);

        return new PageImpl<>(personsFromData, pageable, numberOfPersons);
    }

    @Override
    public Person save(Person person) {
        namedParameterJdbcTemplate.update("update persons set name = :name, birthday = :birthday, country = :country where id = :id",
                Map.of("id", person.getId(), "name", person.getName(), "birthday", person.getBirthday(), "country", person.getCountry()));

        return person;
    }

    @Override
    public Person create(Person person) {
        namedParameterJdbcTemplate.update("insert into persons(id, name, birthday, country) values(:id, :name, :birthday, :country)",
                Map.of(
                        "id", person.getId(),
                        "name", person.getName(),
                        "birthday", person.getBirthday(),
                        "country", person.getCountry())
        );

        return person;
    }

    @Override
    public Optional<Person> findById(String id) {
        List<Person> foundPersons = jdbcTemplate.query("select * from persons where id =?", personRowMapper, id);

        return foundPersons.stream().findFirst();
    }

    @Override
    public boolean existsById(String id) {
        return Optional
                .ofNullable(jdbcTemplate.queryForObject("select exists(select * from persons where id=?)", Boolean.class, id))
                .orElse(Boolean.FALSE);
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
        SqlParameterSource[] sqlParameters = personsToAdd
                .stream()
                .map(person -> new MapSqlParameterSource()
                        .addValue("id", person.getId())
                        .addValue("name", person.getName())
                        .addValue("birthday", person.getBirthday())
                        .addValue("country", person.getCountry()))
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate
                .batchUpdate("insert into persons(id, name, birthday, country) values(:id, :name, :birthday, :country)", sqlParameters);
    }
}

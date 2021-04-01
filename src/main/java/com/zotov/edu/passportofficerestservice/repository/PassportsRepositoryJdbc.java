package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.exception.PassportAlreadyExistsException;
import com.zotov.edu.passportofficerestservice.repository.mapper.PassportRowMapper;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "jdbc")
public class PassportsRepositoryJdbc implements PassportsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final PassportRowMapper passportRowMapper;

    @Override
    public Passport create(Passport passport) {
        try {
            jdbcTemplate.update("insert into passports(number, given_date, department_code, state, owner_id) values (?, ?, ?, ?, ?)",
                    passport.getNumber(), passport.getGivenDate(), passport.getDepartmentCode(), passport.getState().getDatabaseName(), passport.getOwnerId());
        } catch (DuplicateKeyException exception) {
            throw new PassportAlreadyExistsException(passport.getNumber());
        }

        return passport;
    }

    @Override
    public Passport save(Passport passport) {
        jdbcTemplate.update("update passports set given_date =?, department_code =?, state =?, owner_id =? where number =?",
                passport.getGivenDate(), passport.getDepartmentCode(), passport.getState().getDatabaseName(), passport.getOwnerId(), passport.getNumber());

        return passport;
    }

    @Override
    public Optional<Passport> findByPassportNumber(String passportNumber) {
        List<Passport> foundPassports = jdbcTemplate.query("select * from passports where number =?",
                passportRowMapper, passportNumber);
        return foundPassports.stream().findFirst();
    }

    @Override
    public List<Passport> findByOwnerIdAndStateAndGivenDateBetween(
            String personId, PassportState state, LocalDate minGivenDate, LocalDate maxGivenDate) {
        return jdbcTemplate.query("select * from passports where state =? and owner_id =? " +
                        "and (?::date is null or given_date >?) " +
                        "and (?::date is null or given_date <?)", passportRowMapper,
                state.getDatabaseName(), personId, minGivenDate, minGivenDate, maxGivenDate, maxGivenDate);
    }

    @Override
    public void deleteById(String passportNumber) {
        int numberOfDeletedEntities = jdbcTemplate.update("delete from passports where number =?", passportNumber);
        if (numberOfDeletedEntities == 0) {
            throw new PassportNotFoundException(passportNumber);
        }
    }

    @Override
    public void deleteAllByOwnerId(String ownerId) {
        jdbcTemplate.update("delete from passports where owner_id =?", ownerId);
    }

    @Override
    public Boolean existsByPassportNumber(String passportNumber) {
        return jdbcTemplate.queryForObject("select exists(select * from passports where number =?)",
                Boolean.class, passportNumber);
    }

}

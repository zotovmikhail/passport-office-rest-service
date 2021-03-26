package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.exception.PassportAlreadyExistsException;
import com.zotov.edu.passportofficerestservice.repository.mapper.PassportRowMapper;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "jdbc")
public class PassportsRepositoryJdbc implements PassportsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Passport create(Passport passport) {
        findByPassportNumber(passport.getNumber())
                .ifPresent(foundPassport -> {
                    throw new PassportAlreadyExistsException(foundPassport.getNumber());
                });

        jdbcTemplate.update("insert into passports(number, given_date, department_code, state, owner_id) values (?, ?, ?, ?, ?)",
                passport.getNumber(), passport.getGivenDate(), passport.getDepartmentCode(), passport.getState().toString(), passport.getOwnerId());

        return passport;
    }

    @Override
    public Passport save(Passport passport) {
        jdbcTemplate.update("update passports set given_date =?, department_code =?, state =?, owner_id =? where number =?",
                passport.getGivenDate(), passport.getDepartmentCode(), passport.getState().toString(), passport.getOwnerId(), passport.getNumber());

        return passport;
    }

    @Override
    public Optional<Passport> findByPassportNumber(String passportNumber) {
        List<Passport> foundPassports = jdbcTemplate.query("select * from passports where number =?",
                new PassportRowMapper(), passportNumber);
        return foundPassports.stream().findFirst();
    }

    @Override
    public List<Passport> findByOwnerIdAndStateAndGivenDateBetween(
            String personId, PassportState state, LocalDate minGivenDate, LocalDate maxGivenDate) {

        return jdbcTemplate.query("select * from passports where state =? and owner_id =?",
                new PassportRowMapper(), state.toString(), personId);
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
    public boolean existsByPassportNumber(String passportNumber) {
        return jdbcTemplate.queryForObject("select exists(select * from passports where number =?)",
                Boolean.class, passportNumber);
    }

    @Override
    public void saveAll(List<Passport> passportsToAdd) {
        List<Object[]> parametersToUpdate = passportsToAdd
                .stream()
                .map(passport -> new Object[]{
                        passport.getNumber(), passport.getGivenDate(), passport.getDepartmentCode(), passport.getState(), passport.getOwnerId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("insert into passports values (?, ?, ?, ?, ?)", parametersToUpdate);
    }
}

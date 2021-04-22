package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.exception.PassportAlreadyExistsException;
import com.zotov.edu.passportofficerestservice.repository.mapper.ParameterSourceConverter;
import com.zotov.edu.passportofficerestservice.repository.mapper.PassportRowMapper;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
@ConditionalOnProperty(value = "application.repository", havingValue = "jdbc")
public class PassportsRepositoryJdbc implements PassportsRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final PassportRowMapper passportRowMapper;

    private final ParameterSourceConverter parameterSourceConverter;

    @Override
    public Passport create(Passport passport) {
        try {
            namedParameterJdbcTemplate.update("insert into passports(number, given_date, department_code, state, owner_id) " +
                            "values(:number, :given_date, :department_code, :state, :owner_id)",
                    parameterSourceConverter.convertPassport(passport));
        } catch (DuplicateKeyException exception) {
            throw new PassportAlreadyExistsException(passport.getNumber(), exception);
        }

        return passport;
    }

    @Override
    public Passport save(Passport passport) {
        namedParameterJdbcTemplate.update("update passports set given_date = :given_date, department_code = :department_code, " +
                        "state = :state, owner_id = :owner_id where number = :number",
                parameterSourceConverter.convertPassport(passport)
        );

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
    public boolean existsByPassportNumber(String passportNumber) {
        return Optional
                .ofNullable(jdbcTemplate.queryForObject("select exists(select * from passports where number =?)", Boolean.class, passportNumber))
                .orElse(Boolean.FALSE);
    }

}

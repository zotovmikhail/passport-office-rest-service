package com.zotov.edu.passportofficerestservice.repository.mapper;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PassportRowMapper implements RowMapper<Passport> {
    @Override
    public Passport mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Passport(
                resultSet.getString("number"),
                resultSet.getDate("given_date").toLocalDate(),
                resultSet.getString("department_code"),
                PassportState.fromDatabaseName(resultSet.getString("state")),
                resultSet.getString("owner_id")
        );
    }
}

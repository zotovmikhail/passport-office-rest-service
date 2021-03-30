package com.zotov.edu.passportofficerestservice.repository.mapper;

import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Person(
                resultSet.getString("id"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate(),
                resultSet.getString("country")
        );
    }
}

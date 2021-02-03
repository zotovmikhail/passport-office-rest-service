package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PassportsRepository {
    Passport save(Passport passport);

    Optional<Passport> findById(String passportNumber);

    List<Passport> findByOwnerIdAndState(String id, PassportState state);

    Collection<Passport> findAllByOwnerIdAndStateAndGivenDateBetween(
            String id, PassportState state, LocalDate localDate, LocalDate localDate1);

    Collection<Passport> findAllByOwnerIdAndStateAndGivenDateGreaterThan(
            String id, PassportState state, LocalDate localDate);

    Collection<Passport> findAllByOwnerIdAndStateAndGivenDateLessThan(
            String id, PassportState state, LocalDate localDate);

    boolean existsById(String id);

    void deleteById(String passportNumber);
}

package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PassportsRepository {
    Passport save(Passport passport);

    Passport merge(Passport passport);

    Optional<Passport> findById(String passportNumber);

    List<Passport> findByOwnerIdAndStateAndGivenDateBetween(
            String personId, PassportState state, LocalDate minGivenDate, LocalDate maxGivenDate);

    boolean existsById(String passportNumber);

    void deleteById(String passportNumber);

    void deleteAllByOwnerId(String ownerId);
}

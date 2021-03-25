package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PassportsRepository {
    Passport create(Passport passport);

    Passport save(Passport passport);

    Optional<Passport> findByPassportNumber(String passportNumber);

    List<Passport> findByOwnerIdAndStateAndGivenDateBetween(
            String personId, PassportState state, LocalDate minGivenDate, LocalDate maxGivenDate);

    void deleteById(String passportNumber);

    void deleteAllByOwnerId(String ownerId);

    void saveAll(List<Passport> convertToPassportEntities);

    boolean existsByPassportNumber(String passportNumber);
}

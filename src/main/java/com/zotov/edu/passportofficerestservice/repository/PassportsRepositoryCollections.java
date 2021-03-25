package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.exception.PassportAlreadyExistsException;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PassportsRepositoryCollections implements PassportsRepository {

    Map<String, Passport> passports = new HashMap<>();

    @Override
    public Passport create(Passport passport) {
        findByPassportNumber(passport.getNumber())
                .ifPresent(foundPassport -> {
                    throw new PassportAlreadyExistsException(foundPassport.getNumber());
                });
        return save(passport);
    }

    @Override
    public Passport save(Passport passport) {
        passports.put(passport.getNumber(), passport);
        return passport;
    }

    @Override
    public Optional<Passport> findByPassportNumber(String passportNumber) {
        return Optional.ofNullable(passports.get(passportNumber));
    }

    @Override
    public List<Passport> findByOwnerIdAndStateAndGivenDateBetween(
            String personId, PassportState state, LocalDate minGivenDate, LocalDate maxGivenDate) {
        return passports
                .values()
                .stream()
                .filter(passport -> passport.getOwnerId().equals(personId) && passport.getState().equals(state))
                .filter(minGivenDate != null ? passport -> passport.getGivenDate().isAfter(minGivenDate) : passport -> true)
                .filter(maxGivenDate != null ? passport -> passport.getGivenDate().isBefore(maxGivenDate) : passport -> true)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String passportNumber) {
        if (passports.remove(passportNumber) == null) {
            throw new PassportNotFoundException(passportNumber);
        }
    }

    @Override
    public void deleteAllByOwnerId(String ownerId) {
        passports.values()
                .stream()
                .filter(passport -> passport.getOwnerId().equals(ownerId))
                .collect(Collectors.toList())
                .forEach(passport -> passports.remove(passport.getNumber()));
    }

    @Override
    public boolean existsByPassportNumber(String passportNumber) {
        return passports.containsKey(passportNumber);
    }

    @Override
    public void saveAll(List<Passport> passportsToAdd) {
        Map<String, Passport> personsMap = passportsToAdd.stream()
                .collect(Collectors.toMap(Passport::getNumber, person -> person));
        passports.putAll(personsMap);
    }
}

package com.zotov.edu.passportofficerestservice.repository;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PassportsRepositoryCollections implements PassportsRepository {

    Map<String, Passport> passports = new HashMap<>();

    @Override
    public Passport save(Passport passport) {
        passports.put(passport.getNumber(), passport);
        return passports.get(passport.getNumber());
    }

    @Override
    public Optional<Passport> findById(String passportNumber) {
        return Optional.ofNullable(passports.get(passportNumber));
    }

    @Override
    public List<Passport> findByOwnerIdAndState(String ownerId, PassportState state) {
        return passports
                .values()
                .stream()
                .filter(passport -> passport.getOwnerId().equals(ownerId) && passport.getState().equals(state))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Passport> findAllByOwnerIdAndStateAndGivenDateBetween(String ownerId,
                                                                            PassportState state,
                                                                            LocalDate minGivenDate,
                                                                            LocalDate maxGivenDate) {
        return passports
                .values()
                .stream()
                .filter(passport ->
                        passport.getOwnerId().equals(ownerId)
                                && passport.getState().equals(state)
                                && passport.getGivenDate().isAfter(minGivenDate)
                                && passport.getGivenDate().isBefore(maxGivenDate))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Passport> findAllByOwnerIdAndStateAndGivenDateGreaterThan(String ownerId,
                                                                                PassportState state,
                                                                                LocalDate minGivenDate) {
        return passports
                .values()
                .stream()
                .filter(passport ->
                        passport.getOwnerId().equals(ownerId)
                                && passport.getState().equals(state)
                                && passport.getGivenDate().isAfter(minGivenDate))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Passport> findAllByOwnerIdAndStateAndGivenDateLessThan(
            String ownerId, PassportState state, LocalDate maxGivenDate) {
        return passports
                .values()
                .stream()
                .filter(passport ->
                        passport.getOwnerId().equals(ownerId)
                                && passport.getState().equals(state)
                                && passport.getGivenDate().isBefore(maxGivenDate))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return passports.containsKey(id);
    }

    @Override
    public void deleteById(String passportNumber) {
        passports.remove(passportNumber);
    }
}

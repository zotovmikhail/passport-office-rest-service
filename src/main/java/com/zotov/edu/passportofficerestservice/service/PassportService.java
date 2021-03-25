package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.converter.PassportEntityConverter;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.service.exception.PassportIsAlreadyLostException;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class PassportService {

    private final PassportEntityConverter passportConverter;

    private final PassportsRepository passportsRepository;

    public Passport createPassport(String personId, String number, LocalDate givenDate, String departmentCode) {
        Passport passport = passportConverter.convertToEntity(number, givenDate, departmentCode, personId);
        return passportsRepository.create(passport);
    }

    public List<Passport> getPassports(String personId, PassportState state,
                                       LocalDate minGivenDate, LocalDate maxGivenDate) {
        return passportsRepository.findByOwnerIdAndStateAndGivenDateBetween(personId, state, minGivenDate, maxGivenDate);
    }

    public Passport getPassportByOwnerIdAndNumber(String passportNumber) {
        return passportsRepository
                .findByPassportNumber(passportNumber)
                .orElseThrow(() -> new PassportNotFoundException(passportNumber));
    }

    public Passport updatePassport(String passportNumber, LocalDate givenDate, String departmentCode) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        Passport updatedPassport = passportConverter
                .updateEntityFromDto(givenDate, departmentCode, passport);
        return passportsRepository.save(updatedPassport);
    }

    public void deletePassport(String passportNumber) {
        passportsRepository.deleteById(passportNumber);
    }

    public Passport losePassport(String passportNumber) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        checkIfPassportAlreadyLost(passport);
        return passportsRepository.save(passport.withState(PassportState.LOST));
    }

    private Passport getPassportByPassportNumber(String passportNumber) {
        return passportsRepository
                .findByPassportNumber(passportNumber)
                .orElseThrow(() -> new PassportNotFoundException(passportNumber));
    }

    private void checkIfPassportAlreadyLost(Passport passport) {
        if (!passport.isActive()) {
            throw new PassportIsAlreadyLostException(passport.getNumber());
        }
    }
}

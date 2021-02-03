package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.converter.PassportEntityConverter;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.service.exception.EntityNotFoundException;
import com.zotov.edu.passportofficerestservice.service.exception.PassportIsAlreadyLostException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PassportService {

    private final PassportEntityConverter passportConverter;

    private final PassportsRepository passportsRepository;

    public Passport createPassport(String id, String number, LocalDate givenDate, String departmentCode) {
        Passport passport = passportConverter.convertToEntity(id, number, givenDate, departmentCode);
        return passportsRepository.save(passport);
    }

    public List<Passport> getPassports(String id, PassportState state) {
        return passportsRepository.findByOwnerIdAndState(id, state);
    }

    public List<Passport> getPassportsByGivenDateRange(String ownerId, PassportState state,
                                                       LocalDate minGivenDate, LocalDate maxGivenDate) {
        List<Passport> passports = new ArrayList<>();
        if (minGivenDate != null && maxGivenDate != null) {
            passports.addAll(passportsRepository
                    .findAllByOwnerIdAndStateAndGivenDateBetween(ownerId, state, minGivenDate, maxGivenDate));
        } else if (minGivenDate != null) {
            passports.addAll(passportsRepository
                    .findAllByOwnerIdAndStateAndGivenDateGreaterThan(ownerId, state, minGivenDate));
        } else {
            passports.addAll(passportsRepository
                    .findAllByOwnerIdAndStateAndGivenDateLessThan(ownerId, state, maxGivenDate));
        }

        return passports;
    }

    public Passport getPassportByOwnerIdAndNumber(String passportNumber) {
        return passportsRepository
                .findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
    }

    public Passport updatePassport(String passportNumber, LocalDate givenDate, String departmentCode) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        Passport updatedPassport = passportConverter
                .updateEntityFromDto(givenDate, departmentCode, passport);
        return passportsRepository.save(updatedPassport);
    }

    public void deletePassport(String passportNumber) {
        checkIfPassportExists(passportNumber);
        passportsRepository.deleteById(passportNumber);
    }

    public Passport losePassport(String passportNumber) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        checkIfPassportAlreadyLost(passport);
        Passport lostPassport = passportConverter.makePassportLost(passport);
        return passportsRepository.save(lostPassport);
    }

    private Passport getPassportByPassportNumber(String passportNumber) {
        return passportsRepository
                .findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
    }

    private void checkIfPassportAlreadyLost(Passport passport) {
        if (!passport.isActive()) {
            throw new PassportIsAlreadyLostException(passport.getNumber());
        }
    }

    private void checkIfPassportExists(String id) {
        if (!passportsRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
    }
}

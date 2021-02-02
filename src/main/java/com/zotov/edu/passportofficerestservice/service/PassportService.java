package com.zotov.edu.passportofficerestservice.service;

import com.zotov.edu.passportofficerestservice.converter.PassportConverter;
import com.zotov.edu.passportofficerestservice.exception.EntityNotFoundException;
import com.zotov.edu.passportofficerestservice.exception.PassportIsAlreadyLostException;
import com.zotov.edu.passportofficerestservice.model.entity.Passport;
import com.zotov.edu.passportofficerestservice.model.entity.PassportState;
import com.zotov.edu.passportofficerestservice.model.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.response.PassportResponse;
import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PassportService {

    private final PassportConverter passportConverter;

    private final PassportsRepository passportsRepository;

    public PassportResponse createPassport(String id, PassportRequest passportRequest) {
        Passport passport = passportConverter.convertToEntity(id, passportRequest);
        return passportConverter.convertToDto(passportsRepository.save(passport));
    }

    public List<PassportResponse> getPassports(String id, PassportState state) {
        List<Passport> passports = passportsRepository.findByOwnerIdAndState(id, state);
        return passportConverter.convertToDto(passports);
    }

    public List<PassportResponse> getPassportsByGivenDateRange(String ownerId,
                                                               PassportState state,
                                                               LocalDate minGivenDate,
                                                               LocalDate maxGivenDate) {
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

        return passportConverter.convertToDto(passports);
    }

    public PassportResponse getPassportByOwnerIdAndNumber(String passportNumber) {
        Passport passport = passportsRepository
                .findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
        return passportConverter.convertToDto(passport);
    }

    public PassportResponse updatePassport(String passportNumber, PassportPutRequest passportRequest) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        Passport updatedPassport = passportConverter.updateEntityFromDto(passportRequest, passport);
        return passportConverter.convertToDto(passportsRepository.save(updatedPassport));
    }

    public void deletePassport(String passportNumber) {
        checkIfPassportExists(passportNumber);
        passportsRepository.deleteById(passportNumber);
    }

    public PassportResponse losePassport(String passportNumber) {
        Passport passport = getPassportByPassportNumber(passportNumber);
        checkIfPassportAlreadyLost(passport);
        Passport lostPassport = passportConverter.makePassportLost(passport);
        return passportConverter.convertToDto(passportsRepository.save(lostPassport));
    }

    private Passport getPassportByPassportNumber(String passportNumber) {
        return passportsRepository
                .findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
    }

    private PassportService checkIfPassportAlreadyLost(Passport passport) {
        if (!passport.isActive()) {
            throw new PassportIsAlreadyLostException(passport.getNumber());
        }
        return this;
    }

    private PassportService checkIfPassportExists(String id) {
        if (!passportsRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        return this;
    }
}

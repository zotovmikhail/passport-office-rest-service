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
import java.util.Optional;

@Service
@AllArgsConstructor
public class PassportService {


    private final PassportConverter passportConverter;

    private final PassportsRepository passportsRepository;

    public PassportResponse createPassport(String id, PassportRequest passportRequest) {
        Passport passport = passportConverter.covertToEntity(id, passportRequest);
        return passportConverter.convertToDto(passportsRepository.save(passport));
    }

    public List<PassportResponse> getPassports(String id, PassportState state) {
        List<Passport> passports = passportsRepository.findByOwnerIdAndState(id, state);
        return passportConverter.convertToDto(passports);
    }

    public List<PassportResponse> getPassportsByGivenDateRange(String ownerId,
                                                               PassportState state,
                                                               Optional<LocalDate> minGivenDate,
                                                               Optional<LocalDate> maxGivenDate) {
        List<Passport> passports = new ArrayList<>();
        if (minGivenDate.isPresent() && maxGivenDate.isPresent()) {
            passports.addAll(passportsRepository
                    .findAllByOwnerIdAndStateAndGivenDateBetween(ownerId, state, minGivenDate.get(), maxGivenDate.get()));
        } else if (minGivenDate.isPresent()) {
            passports.addAll(passportsRepository
                    .findAllByOwnerIdAndStateAndGivenDateGreaterThan(ownerId, state, minGivenDate.get()));
        } else maxGivenDate.ifPresent(localDate -> passports.addAll(passportsRepository
                .findAllByOwnerIdAndStateAndGivenDateLessThan(ownerId, state, localDate)));

        return passportConverter.convertToDto(passports);
    }

    public PassportResponse getPassportByOwnerIdAndNumber(String passportNumber) {
        Passport passport = passportsRepository.findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
        return passportConverter.convertToDto(passport);
    }

    public PassportResponse updatePassport(String passportNumber, PassportPutRequest passportRequest) {
        Passport passport = passportsRepository.findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
        Passport updatedPassport = passportConverter.updateEntityFromDto(passportRequest, passport);
        return passportConverter.convertToDto(passportsRepository.save(updatedPassport));
    }

    public void deletePassport(String passportNumber) {
        passportsRepository.deleteById(passportNumber);
    }

    public PassportService checkIfPassportExists(String id) {
        if (!passportsRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        return this;
    }

    public PassportResponse losePassport(String passportNumber) {
        Passport passport = passportsRepository.findById(passportNumber)
                .orElseThrow(() -> new EntityNotFoundException(passportNumber));
        if (!passport.isActive()) {
            throw new PassportIsAlreadyLostException(passportNumber);
        }
        Passport lostPassport = passportConverter.makePassportLost(passport);
        return passportConverter.convertToDto(passportsRepository.save(lostPassport));
    }
}

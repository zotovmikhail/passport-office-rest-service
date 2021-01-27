package com.zotov.edu.passportofficerestservice.controller;

import com.zotov.edu.passportofficerestservice.model.entity.PassportState;
import com.zotov.edu.passportofficerestservice.model.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.model.response.PassportResponse;
import com.zotov.edu.passportofficerestservice.model.response.PersonResponse;
import com.zotov.edu.passportofficerestservice.service.PassportService;
import com.zotov.edu.passportofficerestservice.service.PersonService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/persons")
public class PersonApiController {

    private final PersonService personsService;

    private final PassportService passportService;

    @GetMapping
    public Page<PersonResponse> getPersons(@RequestParam(required = false) Optional<String> passportNumber,
                                           @ParameterObject @PageableDefault(size = 100) Pageable pageable) {
        if (passportNumber.isPresent()) {
            return personsService.getPersonByPassportNumber(passportNumber.get());
        }
        return personsService.getAllPersons(pageable);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse createPerson(@Valid @RequestBody PersonRequest person) {
        return personsService.createPerson(person);
    }

    @GetMapping("/{id}")
    public PersonResponse getPerson(@PathVariable String id) {
        return personsService.getPerson(id);
    }

    @PutMapping("/{id}")
    public PersonResponse updatePerson(@PathVariable String id, @Valid @RequestBody PersonRequest person) {
        return personsService.updatePerson(id, person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String id) {
        personsService.deletePersonById(id);
    }

    @GetMapping("/{id}/passports")
    public List<PassportResponse> getPassports(@PathVariable String id,
                                               @RequestParam(required = false, defaultValue = "ACTIVE")
                                                       PassportState state,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       Optional<LocalDate> minGivenDate,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       Optional<LocalDate> maxGivenDate) {
        personsService.checkIfPersonExists(id);
        if (minGivenDate.isPresent() || maxGivenDate.isPresent()) {
            return passportService.getPassportsByGivenDateRange(id, state, minGivenDate, maxGivenDate);
        }
        return passportService.getPassports(id, state);
    }

    @PostMapping("/{personId}/passports")
    @ResponseStatus(HttpStatus.CREATED)
    public PassportResponse createPassport(@PathVariable String personId,
                                           @Valid @RequestBody PassportRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        return passportService.createPassport(personId, passportRequest);
    }

    @GetMapping("/{personId}/passports/{passportNumber}")
    public PassportResponse getPassport(@PathVariable String personId,
                                        @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        return passportService.getPassportByOwnerIdAndNumber(passportNumber);
    }

    @PutMapping("/{personId}/passports/{passportNumber}")
    public PassportResponse updatePerson(@PathVariable String personId,
                                         @PathVariable String passportNumber,
                                         @RequestBody PassportPutRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        return passportService.updatePassport(passportNumber, passportRequest);
    }

    @DeleteMapping("/{personId}/passports/{passportNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassport(@PathVariable String personId,
                               @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        passportService
                .checkIfPassportExists(passportNumber)
                .deletePassport(passportNumber);
    }

    @PostMapping("/{personId}/passports/{passportNumber}/loss")
    public PassportResponse losePassport(@PathVariable String personId,
                                         @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        return passportService
                .checkIfPassportExists(passportNumber)
                .losePassport(passportNumber);
    }


}

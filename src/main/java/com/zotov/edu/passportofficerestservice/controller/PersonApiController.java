package com.zotov.edu.passportofficerestservice.controller;

import com.zotov.edu.passportofficerestservice.controller.converter.PassportControllerDtoConverter;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PassportResponse;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.service.PassportService;
import com.zotov.edu.passportofficerestservice.service.PersonService;
import lombok.AllArgsConstructor;
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

    private final PassportControllerDtoConverter passportControllerDtoConverter;

    @GetMapping
    public Page<PersonResponse> getPersons(@RequestParam(required = false) Optional<String> passportNumber,
                                           @PageableDefault(size = 100) Pageable pageable) {
        Page<Person> foundPersons = passportNumber
                .map(personsService::getPersonByPassportNumber)
                .orElseGet(() -> personsService.getAllPersons(pageable));
        return passportControllerDtoConverter.convertPersonToDto(foundPersons);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse createPerson(@Valid @RequestBody PersonRequest person) {
        Person createdPerson = personsService.createPerson(person.getName(),
                person.getBirthday(), person.getCountry());
        return passportControllerDtoConverter.convertPersonToDto(createdPerson);
    }

    @GetMapping("/{id}")
    public PersonResponse getPerson(@PathVariable String id) {
        return passportControllerDtoConverter.convertPersonToDto(personsService.getPerson(id));
    }

    @PutMapping("/{id}")
    public PersonResponse updatePerson(@PathVariable String id,
                                       @Valid @RequestBody PersonRequest person) {
        Person updatedPerson = personsService.updatePerson(
                id, person.getName(), person.getBirthday(), person.getCountry());
        return passportControllerDtoConverter.convertPersonToDto(updatedPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String id) {
        personsService.deletePersonById(id);
    }

    @GetMapping("/{id}/passports")
    public List<PassportResponse> getPassportsByGivenDatesRange(@PathVariable String id,
                                                                @RequestParam(required = false, defaultValue = "ACTIVE")
                                                                        PassportState state,
                                                                @RequestParam(required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minGivenDate,
                                                                @RequestParam(required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxGivenDate) {
        personsService.checkIfPersonExists(id);
        List<Passport> foundPassports;
        if (minGivenDate != null || maxGivenDate != null) {
            foundPassports = passportService.getPassportsByGivenDateRange(id, state, minGivenDate, maxGivenDate);
        } else {
            foundPassports = passportService.getPassports(id, state);
        }
        return passportControllerDtoConverter.convertPassportListToDto(foundPassports);
    }

    @PostMapping("/{personId}/passports")
    @ResponseStatus(HttpStatus.CREATED)
    public PassportResponse createPassport(@PathVariable String personId,
                                           @Valid @RequestBody PassportRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        Passport createdPassport = passportService.createPassport(
                personId, passportRequest.getNumber(),
                passportRequest.getGivenDate(), passportRequest.getDepartmentCode());
        return passportControllerDtoConverter.convertPassportToDto(createdPassport);
    }

    @GetMapping("/{personId}/passports/{passportNumber}")
    public PassportResponse getPassport(@PathVariable String personId,
                                        @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        Passport foundPassport = passportService.getPassportByOwnerIdAndNumber(passportNumber);
        return passportControllerDtoConverter.convertPassportToDto(foundPassport);
    }

    @PutMapping("/{personId}/passports/{passportNumber}")
    public PassportResponse updatePerson(@PathVariable String personId,
                                         @PathVariable String passportNumber,
                                         @RequestBody PassportPutRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        Passport updatedPassport = passportService.updatePassport(
                passportNumber, passportRequest.getGivenDate(), passportRequest.getDepartmentCode());
        return passportControllerDtoConverter.convertPassportToDto(updatedPassport);
    }

    @DeleteMapping("/{personId}/passports/{passportNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassport(@PathVariable String personId,
                               @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        passportService.deletePassport(passportNumber);
    }

    @PostMapping("/{personId}/passports/{passportNumber}/loss")
    public PassportResponse losePassport(@PathVariable String personId,
                                         @PathVariable String passportNumber) {
        personsService.checkIfPersonExists(personId);
        Passport lostPassport = passportService.losePassport(passportNumber);
        return passportControllerDtoConverter.convertPassportToDto(lostPassport);
    }


}

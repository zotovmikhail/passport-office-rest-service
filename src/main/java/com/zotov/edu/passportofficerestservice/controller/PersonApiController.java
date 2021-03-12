package com.zotov.edu.passportofficerestservice.controller;

import com.zotov.edu.passportofficerestservice.controller.converter.PassportControllerDtoConverter;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.request.PersonRequest;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PageResponse;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PassportResponse;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import com.zotov.edu.passportofficerestservice.service.PassportService;
import com.zotov.edu.passportofficerestservice.service.PersonService;
import lombok.AllArgsConstructor;
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
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<PersonResponse> getPersons(@RequestParam(required = false, name = "passportNumber")
                                                   Optional<String> passportNumber,
                                           @PageableDefault(size = 100) Pageable pageable) {
        return new PageResponse<>(passportNumber
                .map(existentPassportNumber -> personsService.getPersonByPassportNumber(existentPassportNumber, pageable))
                .orElseGet(() -> personsService.getAllPersons(pageable))
                .map(passportControllerDtoConverter::convertPersonToDto));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResponse createPerson(@Valid @RequestBody PersonRequest person) {
        Person createdPerson = personsService.createPerson(person.getName(),
                person.getBirthday(), person.getCountry());
        return passportControllerDtoConverter.convertPersonToDto(createdPerson);
    }

    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse getPerson(@PathVariable("personId") String personId) {
        return passportControllerDtoConverter.convertPersonToDto(personsService.getPerson(personId));
    }

    @PutMapping("/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse updatePerson(@PathVariable("personId") String personId,
                                       @Valid @RequestBody PersonRequest person) {
        Person updatedPerson = personsService.updatePerson(
                personId, person.getName(), person.getBirthday(), person.getCountry());
        return passportControllerDtoConverter.convertPersonToDto(updatedPerson);
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable("personId") String personId) {
        personsService.deletePersonById(personId);
    }

    @GetMapping("/{personId}/passports")
    @ResponseStatus(HttpStatus.OK)
    public List<PassportResponse> getPassportsByGivenDatesRange(@PathVariable("personId") String personId,
                                                                @RequestParam(required = false, defaultValue = "ACTIVE", name = "state")
                                                                        PassportState state,
                                                                @RequestParam(required = false, name = "minGivenDate")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minGivenDate,
                                                                @RequestParam(required = false, name = "maxGivenDate")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxGivenDate) {
        personsService.checkIfPersonExists(personId);
        List<Passport> foundPassports = passportService.getPassports(personId, state, minGivenDate, maxGivenDate);
        return passportControllerDtoConverter.convertPassportListToDto(foundPassports);
    }

    @PostMapping("/{personId}/passports")
    @ResponseStatus(HttpStatus.CREATED)
    public PassportResponse createPassport(@PathVariable("personId") String personId,
                                           @Valid @RequestBody PassportRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        Passport createdPassport = passportService.createPassport(
                personId, passportRequest.getNumber(),
                passportRequest.getGivenDate(), passportRequest.getDepartmentCode());
        return passportControllerDtoConverter.convertPassportToDto(createdPassport);
    }

    @GetMapping("/{personId}/passports/{passportNumber}")
    @ResponseStatus(HttpStatus.OK)
    public PassportResponse getPassport(@PathVariable("personId") String personId,
                                        @PathVariable("passportNumber") String passportNumber) {
        personsService.checkIfPersonExists(personId);
        Passport foundPassport = passportService.getPassportByOwnerIdAndNumber(passportNumber);
        return passportControllerDtoConverter.convertPassportToDto(foundPassport);
    }

    @PutMapping("/{personId}/passports/{passportNumber}")
    @ResponseStatus(HttpStatus.OK)
    public PassportResponse updatePassport(@PathVariable("personId") String personId,
                                         @PathVariable("passportNumber") String passportNumber,
                                         @Valid @RequestBody PassportPutRequest passportRequest) {
        personsService.checkIfPersonExists(personId);
        Passport updatedPassport = passportService.updatePassport(
                passportNumber, passportRequest.getGivenDate(), passportRequest.getDepartmentCode());
        return passportControllerDtoConverter.convertPassportToDto(updatedPassport);
    }

    @DeleteMapping("/{personId}/passports/{passportNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassport(@PathVariable("personId") String personId,
                               @PathVariable("passportNumber") String passportNumber) {
        personsService.checkIfPersonExists(personId);
        passportService.deletePassport(passportNumber);
    }

    @PostMapping("/{personId}/passports/{passportNumber}/loss")
    @ResponseStatus(HttpStatus.OK)
    public PassportResponse losePassport(@PathVariable("personId") String personId,
                                         @PathVariable("passportNumber") String passportNumber) {
        personsService.checkIfPersonExists(personId);
        Passport lostPassport = passportService.losePassport(passportNumber);
        return passportControllerDtoConverter.convertPassportToDto(lostPassport);
    }


}

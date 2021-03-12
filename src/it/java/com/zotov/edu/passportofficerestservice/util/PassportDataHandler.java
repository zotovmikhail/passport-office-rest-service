package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generatePassport;

@Component
public class PassportDataHandler {

    @Getter
    private final PassportsRepository passportsRepository;

    public PassportDataHandler(PassportsRepository passportsRepository) {
        this.passportsRepository = passportsRepository;
    }

    public Passport generatePassportData(String ownerId) {
        return passportsRepository.save(generatePassport(ownerId));
    }

    public Passport generatePassportData(Passport passport) {
        return passportsRepository.save(passport);
    }

}

package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.repository.PassportsRepository;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;

@Component
@AllArgsConstructor
public class PassportDataHandler {

    private final PassportsRepository passportsRepository;

    public Passport generatePassportData(String ownerId) {
        return passportsRepository.save(generatePassport(ownerId));
    }

    public Passport generatePassportData(Passport passport) {
        return passportsRepository.save(passport);
    }

}

package pl.sda.poznan.spring.petclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.poznan.spring.petclinic.dto.ApplicationUserDto;
import pl.sda.poznan.spring.petclinic.exception.ApplicationUserNotFoundException;
import pl.sda.poznan.spring.petclinic.model.ApplicationUser;
import pl.sda.poznan.spring.petclinic.repository.ApplicationUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(ApplicationUser applicationUser) {
        String encodedPassword = passwordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(encodedPassword);
        this.applicationUserRepository.save(applicationUser);
    }

    public ApplicationUserDto getUserData(String email){
        Optional<ApplicationUser> optionalUser = applicationUserRepository.findByEmail(email);
        ApplicationUser applicationUser = optionalUser.orElseThrow(ApplicationUserNotFoundException::new);
        return conversionService.convert(applicationUser, ApplicationUserDto.class);
    }
}

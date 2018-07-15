package pl.sda.poznan.spring.petclinic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sda.poznan.spring.petclinic.dto.ApplicationUserDto;
import pl.sda.poznan.spring.petclinic.model.ApplicationUser;
import pl.sda.poznan.spring.petclinic.service.AuthenticationService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity registerUser(
            @RequestBody
            @Valid
                    ApplicationUser applicationUser,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        authenticationService.saveUser(applicationUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ResponseEntity<ApplicationUserDto> currentUserName(Principal principal) {
        String email = principal.getName();
        ApplicationUserDto userDto = authenticationService.getUserData(email);
        return ResponseEntity.ok(userDto);
    }
}

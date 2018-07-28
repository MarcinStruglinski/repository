package pl.sda.poznan.spring.petclinic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sda.poznan.spring.petclinic.model.Owner;
import pl.sda.poznan.spring.petclinic.service.OwnerService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(exposedHeaders = "errors, content-type")
public class OwnerController {

  private final OwnerService ownerService;

  public OwnerController(OwnerService ownerService) {
    this.ownerService = ownerService;
  }


  @GetMapping(path = "/owner/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
    Owner ownerById = ownerService.findOwnerById(id);
    return ResponseEntity.ok(ownerById);
  }

  @GetMapping(path = "/owners", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Collection<Owner>> getAllOwners() {
    return ResponseEntity.ok(ownerService.findAllOwners());
  }

  @PostMapping(path = "/owner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
    this.ownerService.saveOwner(owner);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  //todo: Zaimplementuj aktualizacje ownera i jego kasowanie
  @PutMapping(path = "/owner", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Owner> updateOwner(@RequestBody @Valid Owner owner,
      BindingResult bindingResult) {
    // moduł walidacji springa umieści błędy w obiekcie binding result
    if (bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }
    ownerService.update(owner);
    return ResponseEntity.ok(owner);
  }

}

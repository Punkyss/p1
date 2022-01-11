package compu.p1;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class UserController {

    private final UserRepository userRepository;
    private final UserModelAssembler assembler;

    UserController(UserRepository userRepository, UserModelAssembler assembler) {

        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    CollectionModel<EntityModel<User>> all() {

        List<EntityModel<User>> users = userRepository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(users, //
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/users/{id}")
    EntityModel<User> one(@PathVariable Long id) {

        User user = userRepository.findById(id) //
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }


    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }
    /*
    @PostMapping("/users")
    ResponseEntity<EntityModel<User>> newUser(@RequestBody User user) {

        User newUser = userRepository.save(user);

        return ResponseEntity //
                .created(linkTo(methodOn(UserController.class).one(newUser.getId())).toUri()) //
                .body(assembler.toModel(newUser));
    }*/
}
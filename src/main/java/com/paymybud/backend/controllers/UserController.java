package com.paymybud.backend.controllers;


import com.paymybud.backend.dto.AccountDto;
import com.paymybud.backend.entities.Account;
import com.paymybud.backend.entities.User;
import com.paymybud.backend.service.AccountService;
import com.paymybud.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;


    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.add(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    @GetMapping("/allUser/{id}")
    public ResponseEntity<Iterable<User>> getAllUserExceptCurrentUser(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUserExceptFriends(id));
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Integer id) {
        userService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.get());
    }

    @PostMapping("/{id}/addFriend/{friendEmail}")
    public void addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendEmail") String friendEmail) throws Exception {
         userService.addFriend(userId,friendEmail);
    }

    @PostMapping("/{id}/createAccount")
    public void addAccount(@PathVariable("id") Integer userId,
                           @RequestBody @Valid AccountDto account) {
        userService.addAccount(userId,account);
    }

    @PostMapping("/{id}/chargerAccount/{balance}")
    public void chargerAccount(@PathVariable("id") Integer userId,
                               @PathVariable("balance") Integer balance) {
        userService.chargerAccount(userId,balance);
    }
    @PostMapping("/{id}/envoyer/{friendEmail}")
    public void envoyerArgent(@PathVariable("id") Integer userId,
                              @RequestBody String friendEmail,
                              @RequestBody Integer balance) throws Exception {
        userService.envoyerArgentU(userId,friendEmail, balance);
    }

}

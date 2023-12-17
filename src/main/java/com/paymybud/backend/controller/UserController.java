package com.paymybud.backend.controller;


import com.paymybud.backend.dto.AccountDTO;
import com.paymybud.backend.dto.TransfertDTO;
import com.paymybud.backend.dto.UserDTO;
import com.paymybud.backend.entity.Transaction;
import com.paymybud.backend.service.AccountService;
import com.paymybud.backend.service.UserService;
import jakarta.transaction.Transactional;
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


    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> get(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    @GetMapping("/allUser/{id}")
    public ResponseEntity<Iterable<UserDTO>> getAllUserExceptCurrentUser(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUserExceptFriends(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        userService.deleteById(id);
    }


    @PostMapping("/{id}/addFriend/{friendEmail}")
    public ResponseEntity addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendEmail") String friendEmail) throws Exception {
         userService.addFriend(userId,friendEmail);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/{id}/createAccount")
    public ResponseEntity addAccount(@PathVariable("id") Integer userId,
                           @RequestBody @Valid AccountDTO account){
        userService.addAccount(userId,account);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/{id}/transfert/{balance}")
    public ResponseEntity transfererArgent(@PathVariable("id") Integer userId,
                               @PathVariable("balance") Integer balance)
            throws Exception {
        userService.chargerAccount(userId,balance, "transfert");
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{id}/charger/{balance}")
    public ResponseEntity envoyerArgent(@PathVariable("id") Integer userId,
                               @PathVariable("balance") Integer balance)
            throws Exception {
        userService.chargerAccount(userId,balance, "charger");
        return ResponseEntity.ok().build();

    }

    @PostMapping("/{id}/send")
    public ResponseEntity send(@PathVariable("id") Integer userId,
                     @RequestBody @Valid TransfertDTO transfertDto) throws Exception {
        userService.send(userId, transfertDto);
        return ResponseEntity.ok().build();

    }
    @GetMapping("/{id}/friends")
    public void getAllFriendsByUser(@PathVariable("id") Integer id) {
       // return ResponseEntity.status(HttpStatus.OK)
        //    .body(userService.getAllFriendsByUser(id));
    }

}

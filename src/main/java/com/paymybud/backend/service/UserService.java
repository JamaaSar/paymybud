package com.paymybud.backend.service;

import com.paymybud.backend.dto.AccountDto;
import com.paymybud.backend.dto.CredentialsDto;
import com.paymybud.backend.dto.SignUpDto;
import com.paymybud.backend.dto.UserDto;
import com.paymybud.backend.entities.Account;
import com.paymybud.backend.entities.User;
import com.paymybud.backend.exceptions.AppException;
import com.paymybud.backend.mappers.UserMapper;
import com.paymybud.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
    public User findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return user;
    }
    public void deleteById(Integer id) {
        userRepository.deleteById(id);

    }

    public void addAccount(Integer userId, AccountDto account) {
        User user = userRepository.findById(userId).orElseThrow();
        Account acc = new Account();
        System.out.println(account.iban());
        acc.setBalance(account.balance());
        acc.setIban(account.iban());

        user.setAccount(acc);
        userRepository.save(user);
    }
    public void addFriend(Integer userId, String friendEmail) throws Exception {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));


        if(user.getFriends().contains(friend)){
            throw new Exception("you cant add one person twice");
        }
        if(!user.getEmail().equals(friendEmail)){
            List<User> friendsList = user.getFriends();
            friendsList.add(friend);
            user.setFriends(friendsList);
            userRepository.save(user);
        }else{
            throw new Exception("you cant add yourself as friend");


        }}

    public User add(User user) {
        return userRepository.save(user);
    }
    public Iterable<User> get() {
        return userRepository.findAll();
    }
    public Iterable<User> getAllUserExceptCurrentUser(Integer id) {
        return userRepository.getAllUserExceptCurrentUser(id);
    }

    public Iterable<User> getAllUserExceptFriends(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        List<User> allUser = userRepository.getAllUserExceptCurrentUser(id);
        System.out.println(user.getFriends());


        List<User> differences = allUser.stream()
                .filter(element -> !user.getFriends().contains(element))
                .collect(Collectors.toList());

        return differences;
    }


    public void chargerAccount(Integer userId, Integer balance) {

        User user = userRepository.findById(userId).orElseThrow();
        Account acc = user.getAccount();
        acc.setBalance(balance);
        userRepository.save(user);
    }

    public void envoyerArgentU(Integer userId, String friendEmail, Integer balance) throws Exception {
        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));


        if(user.getFriends().contains(friend)){
            throw new Exception("you cant add one person twice");
        }

        chargerAccount(friend.getId(), balance);
        chargerAccount(userId, user.getAccount().getBalance()-balance);

        }

}

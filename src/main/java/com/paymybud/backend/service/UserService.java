package com.paymybud.backend.service;

import com.paymybud.backend.dto.*;
import com.paymybud.backend.entity.*;
import com.paymybud.backend.exceptions.BadRequestException;
import com.paymybud.backend.mappers.UserMapper;
import com.paymybud.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.*;
import java.util.stream.Collectors;

import static com.paymybud.backend.entity.TypeOfTransaction.TO_FRIEND;
import static com.paymybud.backend.entity.TypeOfTransaction.TO_OWN_CART;

@RequiredArgsConstructor
@Service
public class  UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final UserMapper userMapper;
    private static final int tax = (int) 0.005;


    public UserDTO login(CredentialsDTO credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(() -> new BadRequestException("Unknown user"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new BadRequestException("Incorrect password");
    }

    public UserDTO signup(SignUpDTO signUpDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.email());

        if (optionalUser.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        User user = userMapper.signUpToUser(signUpDTO);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDTO.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public void addAccount(Integer userId, AccountDTO accountDTO)  {
        User user = userRepository.findById(userId).orElseThrow();
        if(accountDTO.iban().isEmpty()){
            throw new BadRequestException("Please add an iban");
        }

        Account account = accountService.add(accountDTO);
        user.setAccount(account);
        userRepository.save(user);
    }


    public void addFriend(Integer userId, String friendEmail) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Unknown user"));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new BadRequestException("Unknown user"));


        if(user.getFriendsList().contains(friend)){
            throw new BadRequestException("This person is already in your friends list");
        }
        if(!user.getEmail().equals(friendEmail)){
            user.getFriendsList().add(friend);

            userRepository.save(user);

        }else{
            throw new BadRequestException("You can't add yourself as a friend");
        }
    }

    public void chargerAccount(Integer userId, Integer balance,String type) throws Exception{

        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Unknown user"));
        Account acc = user.getAccount();
        if(acc == null){
            throw new BadRequestException("Add an iban");

        }
        if(type == "transfert" && acc.getBalance() < balance){
            throw new BadRequestException("Your balance is not enough ");
        }

        if(type == "transfert"){
            accountService.update(acc, acc.getBalance() - balance);
            transactionService.add(new TransactionDTO(balance,null, TO_OWN_CART, user,user));
        }else{
            accountService.update(acc, acc.getBalance() + balance);
        }
    }
    @Transactional
    public void send(Integer userId, TransfertDTO transfertDto) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("Unknown user"));
        User friend = userRepository.findByEmail(transfertDto.receiverEmail())
                .orElseThrow(() -> new BadRequestException( "cant find " + transfertDto.receiverEmail() + " "));


        if(!user.getFriendsList().contains(friend)){
            throw new BadRequestException("Please add this person to your friend list");
        }

        Account acc = user.getAccount();
        if(acc == null){
            throw new BadRequestException("Please add an iban ");
        }

        Account acc1 = friend.getAccount();
        if(acc1 == null){
            throw new BadRequestException("Your friend doesn't have account yet");

        }

        if(acc.getBalance() < transfertDto.amount()){
            throw new BadRequestException("Your balance is not enough");

        }

        accountService.update(acc,
                acc.getBalance() - calculateTax(transfertDto.amount()));
        accountService.update(acc1, acc1.getBalance() + transfertDto.amount());
        transactionService.add(new TransactionDTO(transfertDto.amount(), transfertDto.message(),
                TO_FRIEND, user,
                friend));

        }
    public Integer calculateTax(Integer amount){
        return amount + amount*tax;
    }


    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Unknown user"));
        return userMapper.toUserDto(user);
    }
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Unknown user"));
        UserDTO userDto = userMapper.toUserDto(user);
        return userDto;
    }
    public void deleteById(Integer id) {
        userRepository.deleteById(id);

    }
    public UserDTO add(User user) {
        userRepository.save(user);
        UserDTO userDto = new UserDTO();
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setToken("");
        userDto.setAccount(user.getAccount());

        userDto.setFriendsList(userMapper.toFriendDto(user.getFriendsList()));
        return userDto;
    }
    public Iterable<UserDTO> getAll() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public Iterable<UserDTO> getAllUserExceptFriends(Integer id) {
        User user = userRepository.findById(id).orElseThrow();
        List<User> allUser = userRepository.getAllUserExceptCurrentUser(id);

        List<User> differences = allUser.stream()
                .filter(element -> !user.getFriendsList().contains(element))
                .collect(Collectors.toList());


        return userMapper.toUserDtoList(differences);
    }


    public FriendDTO getAllFriendsByUser(Integer id) {
        return null;

    }
}

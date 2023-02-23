package com.casestudy.services;

import com.casestudy.entities.User;
import com.casestudy.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUser(Long userId, User newUser) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User foundUser = user.get();
            foundUser.setLevel(newUser.getLevel());
            foundUser.setCoin(newUser.getCoin());
            foundUser.setTeam(newUser.getTeam());
            userRepository.save(foundUser);
            return foundUser;
        }else
            return null;
    }

    public User levelUpUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User foundUser = user.get();
            foundUser.setLevel(foundUser.getLevel() + 1);
            foundUser.setCoin(foundUser.getCoin() + 25);
            userRepository.save(foundUser);
            return foundUser;
        }else
            return null;
    }

}

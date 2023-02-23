package com.casestudy.api;

import com.casestudy.entities.User;
import com.casestudy.exceptions.user.UserNotFoundException;
import com.casestudy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser() {
        User user = new User();
        return userService.saveOneUser(user);
    }

    @PutMapping(path = "/{id}")
    public User levelUpUser(@PathVariable("id") Long id) {
        User updatedUser = userService.levelUpUser(id);

        if(updatedUser == null) {
            throw new UserNotFoundException();
        }

        return updatedUser;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String handleUserNotFoundException() {
        return "User not found!";
    }

}

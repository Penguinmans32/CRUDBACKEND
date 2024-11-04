package com.example.crud.savorspace.controller;

import com.example.crud.savorspace.entity.UserEntity;
import com.example.crud.savorspace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    // Test endpoint
    @GetMapping("/print")
    public String print() {
        return "Hello World!";
    }

    // Sign-up a new user
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> signUp(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.saveUser(user);

        // Hide the password in the response
        savedUser.setPassword(null);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody UserEntity user) {
        UserEntity existingUser = userService.findByEmail(user.getEmail());

        if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(null);
            return ResponseEntity.ok(existingUser);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    // Get all users (for admin or debugging purposes)
    @GetMapping
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();

        // Hide passwords from the response
        users.forEach(user -> user.setPassword(null));

        return users;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        UserEntity updatedUser = userService.updateUser(id, user);

        updatedUser.setPassword(null);

        return ResponseEntity.ok(updatedUser);
    }
}
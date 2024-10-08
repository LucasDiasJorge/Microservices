package com.api.user.resources;

import com.api.user.entities.User;
import com.api.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping(value = "/users")
public class UserResources {

    private static final Logger logger = Logger.getLogger(UserResources.class.getName());

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<User>> findAll(){
        List<User> list = userRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) throws InterruptedException {
        User worker = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(worker);
    }

    @GetMapping("/search")
    public ResponseEntity<User> findByEmail(@RequestParam String email) throws InterruptedException {
        User worker = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(worker);
    }

}

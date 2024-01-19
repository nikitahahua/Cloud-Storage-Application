package com.CloudStore.service.impl;

import com.CloudStore.exceptions.EntityAlreadyExistsException;
import com.CloudStore.exceptions.EntityNotFoundException;
import com.CloudStore.exceptions.NullEntityReferenceException;
import com.CloudStore.model.User;
import com.CloudStore.repository.UserRepository;
import com.CloudStore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        List<User> users = getAll();
        User userByEmail = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(() -> new NullEntityReferenceException("User not found with email: " + user.getEmail()));
        if (user.getEmail().isEmpty() || user.getUsername().isEmpty() || user.getPassword().isEmpty()){
            throw new NullEntityReferenceException("some user fields might be empty");
        }
        if (users.contains(userByEmail)) {
            throw new EntityAlreadyExistsException("User is already exist!");
        }
        return userRepository.save(user);
    }

    @Override
    public User readById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "id", id));
    }

    @Override
    public User readByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "email", email));
    }

    @Override
    public User update(User user) {
        if (user.getEmail().isEmpty() || user.getUsername().isEmpty() || user.getPassword().isEmpty()){
            throw new NullEntityReferenceException("some user fields might be empty");
        }
        User oldUser = userRepository.findUserByEmail
                (user.getEmail()).orElseThrow(() -> new NullEntityReferenceException("User not found with email: " + user.getEmail()));

        oldUser.setEmail(user.getEmail());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setRole(user.getRole());

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = readById(id);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

}
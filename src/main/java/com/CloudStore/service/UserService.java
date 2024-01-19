package com.CloudStore.service;

import com.CloudStore.model.User;

import java.util.List;

public interface UserService {

    User create(User user);
    User readById(Long id);
    User readByEmail(String email);
    User update(User user);
    void delete(Long id);
    List<User> getAll();

}
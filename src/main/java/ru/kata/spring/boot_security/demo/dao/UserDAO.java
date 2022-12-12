package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {

    void add(User user);
    List<User> getList();

    User findById(Long id);

    User findUserByUsername (String username);

    void delete(Long id);

    void change(Long id, User user);
}

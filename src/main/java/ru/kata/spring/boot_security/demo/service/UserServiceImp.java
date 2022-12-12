package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService, UserDetailsService {
//
    private final UserDAO userDAO;


    @Autowired
    public UserServiceImp(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDAO.add(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getList() {
        return userDAO.getList();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user = Optional.ofNullable(userDAO.findById(id));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Пользователь с id '%d' не найден!", id));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername (String username) {
        Optional<User> user = Optional.ofNullable(userDAO.findUserByUsername(username));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Пользователь с username '%s' не найден!", username));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (Optional.ofNullable(userDAO.findById(id)).isPresent()) {
            userDAO.delete(id);
        } else {
            throw new UsernameNotFoundException(String.format("Пользователь с id '%d' не найден!", id));
        }
    }

    @Override
    @Transactional
    public void change(Long id, User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDAO.change(id, user);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userDAO.findUserByUsername(username));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден!", username));
        }
        return user.get();
    }
}

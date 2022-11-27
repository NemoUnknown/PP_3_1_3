package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.security.UserDetails;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getList() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(String.format("Пользователь с id '%d' не найден!", id));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByUsername (String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UsernameNotFoundException(String.format("Пользователь с id '%d' не найден!", id));
        }
    }

    @Override
    @Transactional
    public void change(Long id, User user) {

        User changedUser = userRepository.getReferenceById(id);
        changedUser.setUsername(user.getUsername());
        changedUser.setPassword(user.getPassword());
        changedUser.setConfirmPassword(user.getConfirmPassword());
        changedUser.setFirstName(user.getFirstName());
        changedUser.setLastName(user.getLastName());
        changedUser.setEmail(user.getEmail());
        changedUser.setRoles(user.getRoles());

        userRepository.save(changedUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByUsername(username));

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден!", username));
        }
        return new ru.kata.spring.boot_security.demo.security.UserDetails(user.get());
    }
}

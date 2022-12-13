package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface RoleService {

    void addRole(Role role);
    Role getRoleByName(String name);
    List<Role> getRoles();

    List<Role> getCurrentRoles(User user);
    @Transactional
    List<Role> listByRole(List<String> name);
}

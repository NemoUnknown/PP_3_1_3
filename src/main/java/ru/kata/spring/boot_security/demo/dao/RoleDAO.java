package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleDAO {

    void addRole(Role role);
    Role getRoleByName(String name);

    List<Role> getRoles();

    List<Role> listByName(List<String> name);
}

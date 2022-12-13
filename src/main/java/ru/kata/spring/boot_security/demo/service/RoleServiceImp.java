package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    private final RoleDAO roleDAO;//

    @Autowired
    public RoleServiceImp(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleDAO.getRoles();
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        roleDAO.addRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleDAO.getRoleByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> listByRole(List<String> name) {
        return roleDAO.listByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getCurrentRoles(User user) {
        List<Role> roleList = user.getRoles();
        Optional<Role> roleUser = Optional.ofNullable(roleDAO.getRoleByName("ROLE_USER"));

        if ((roleList == null) || (roleList.isEmpty())) {
            roleList = new ArrayList<>();
            roleList.add(roleUser.orElse(new Role("ROLE_USER")));
        } else if ((roleList.size() == 1) && (roleList.get(0).getRoleName().equals("ROLE_ADMIN"))) {
            roleList.add(roleUser.orElse(new Role("ROLE_USER")));
        }
        return roleList;
    }
}

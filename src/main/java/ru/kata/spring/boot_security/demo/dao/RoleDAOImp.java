package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RoleDAOImp implements RoleDAO {
    private final EntityManager entityManager;

    @Autowired
    public RoleDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role getRoleByName(String name) {
        Role role = entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName=:role", Role.class)
                .setParameter("role", name)
                .getSingleResult();
        return role;
    }

    @Override
    public List<Role> getRoles() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public List<Role> listByName(List<String> name) {
        return entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName in (:id)", Role.class)
                .setParameter("id", name)
                .getResultList();
    }
}

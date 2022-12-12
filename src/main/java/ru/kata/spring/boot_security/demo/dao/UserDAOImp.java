package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImp implements UserDAO {
    private final EntityManager entityManager;

    @Autowired
    public UserDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getList() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id=:id", User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public User findUserByUsername(String username) {
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        return user;
    }

    @Override
    public void delete(Long id) {
        entityManager.joinTransaction();
        try {
            entityManager.remove(findById(id));
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void change(Long id, User user) {
        entityManager.merge(user);
    }
}

package com.via.dao;

import com.via.entity.UserAccount;
import com.via.entity.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component("userDetailDao")
public class UserDetailsDao  {

    @PersistenceContext
    @Qualifier(value="entityManagerFactory")
    private EntityManager em;

    @Transactional
    public void  create(UserAccount user){
        em.persist(user);
    }

    @Transactional
    public void  create(UserRole role){
        em.persist(role);
    }

    @Transactional
    public UserAccount update(UserAccount user){
        return em.merge(user);
    }

    @Transactional
    public List<String> getAllAccountEmail(){
        return em.createQuery("select u.email from UserAccount u", String.class).getResultList();
    }

    /**
     * Get the user detail by name for the spring security login authentication
     * @param name
     * @return
     */
    @Transactional
    public UserAccount findByName(String name){
        UserAccount user = em.createQuery("select u from UserAccount u where u.username = :username", UserAccount.class)
                .setParameter("username", name)
                .getSingleResult();
        if(user.getAuthorities() != null)
            user.getAuthorities().size();
        return user;
    }

}

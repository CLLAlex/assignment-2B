package com.via.dao;

import com.via.entity.Company;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Component("companyDao")
public class CompanyDao {

    @PersistenceContext
    @Qualifier(value="entityManagerFactory")
    private EntityManager em;

    @Transactional
    public void create(Company company){
        em.persist(company);
    }

    @Transactional
    public void update(Company company){
        em.merge(company);
    }

    @Transactional
    public Company find(int id){
        return em.find(Company.class, id);
    }


    @Transactional
    public List<Company> findAll(){
        List<Company> itemList = em.createQuery("Select f from Form f", Company.class)
                .setMaxResults(10)
                .getResultList();
        return itemList;
    }
}

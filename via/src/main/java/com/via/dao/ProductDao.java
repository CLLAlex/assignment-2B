package com.via.dao;

import com.via.entity.Product;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("productDao")
public class ProductDao implements Serializable {

    @PersistenceContext
    @Qualifier(value="entityManagerFactory")
    private EntityManager em;

    @Transactional
    public void create(Product product){
        em.persist(product);
    }

    @Transactional
    public void update(Product product){
        em.merge(product);
    }

    @Transactional
    public Product find(int id){
        return em.find(Product.class, id);
    }

    @Transactional
    public List<Product> find(Date startDate, Date endDate){
        String sql = "Select f from Product f where f.createTime between :startDate and :endDate";
        List<Product> itemList = em.createQuery(sql, Product.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        return itemList;
    }

    @Transactional
    public List<Product> findAll(){
        List<Product> itemList = em.createQuery("Select f from Product f", Product.class)
                 .setMaxResults(10)
                .getResultList();
        return itemList;
    }

    @Transactional
    public Map<Long, List<Product>> simpleSearch(int startPos, int size){

        Map<Long, List<Product>> resultMap = new HashMap<>();

        List<Product> itemList = em.createQuery("Select f from Product f order by createTime DESC", Product.class)
                .setFirstResult(startPos)
                .setMaxResults(size)
                .getResultList();

        Long count = Long.valueOf(getCount());

        resultMap.put(count, itemList);
        return resultMap;
    }

    @Transactional
    public Map<Long, List<Product>> fullTextSearch(Map<String, Object>searchCriteria, int startPos, int size){
            FullTextEntityManager fullTextEntityManager
                    = Search.getFullTextEntityManager(em);

            Map<Long, List<Product>> resultMap = new HashMap<>();
            Long count;
            List<Product> productList;



            QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(Product.class)
                    .get();
            Query query = null;
            Query queryById = null;
            Query queryByEnum = null;
            Query maxQuery = null;

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            CriteriaQuery<Long> criteriaQueryCount = criteriaBuilder.createQuery(Long.class);
            Root<Product> from = criteriaQuery.from(Product.class);


        List<Predicate> predicates = new ArrayList<>();

            if(searchCriteria.get("id") != null)
                predicates.add(criteriaBuilder.equal(from.get("id"), (int)searchCriteria.get("id")));
            if(searchCriteria.get("borrowerName") != null && !searchCriteria.get("borrowerName").toString().isEmpty())
                predicates.add(criteriaBuilder.like(from.get("borrowerName"), "%" + searchCriteria.get("borrowerName").toString() + "%" ));
            if(searchCriteria.get("email") != null && !searchCriteria.get("email").toString().isEmpty())
                predicates.add(criteriaBuilder.like(from.get("email"), "%" + searchCriteria.get("email").toString() + "%"));
            if(searchCriteria.get("status") != null && !searchCriteria.get("status").toString().isEmpty())
                predicates.add(criteriaBuilder.equal(from.get("status"), Product.FormStatus.valueOf(searchCriteria.get("status").toString())));

            if(searchCriteria.get("createTime") != null && !searchCriteria.get("createTime").toString().isEmpty()){

                String dateRange = searchCriteria.get("createTime").toString();
                String[] rangeSplit = null;
                if(dateRange.indexOf('-') > 0)
                    rangeSplit = dateRange.split("-");

                Date startDate = new Date();
                Date endDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                if(rangeSplit != null) {
                    try{
                        startDate = formatter.parse(rangeSplit[0].trim());
                        endDate = formatter.parse(rangeSplit[1].trim());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    predicates.add(criteriaBuilder.between(from.get("createTime"), startDate, endDate));
                }
            }

            //Add criteria into the where cause
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            criteriaQueryCount.where(predicates.toArray(new Predicate[predicates.size()]));
            TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);

            criteriaQueryCount.select(criteriaBuilder.count(criteriaQueryCount.from(Product.class)));
            count = em.createQuery(criteriaQueryCount).getSingleResult();

            productList = typedQuery.setFirstResult(startPos)
                .setMaxResults(size)
                .getResultList();



            resultMap.put(count, productList);

            return resultMap;
    }

    @Transactional
    public Map<Long, List<Product>> search(Map<String, Object> searchCriteria, int startPos, int size){

        boolean isAllNull = true;

        for(Map.Entry<String, Object> entry : searchCriteria.entrySet()){
            if(entry.getValue() != null && !entry.getValue().toString().isEmpty())
                isAllNull = false;
        }


        if(isAllNull)
            return simpleSearch(startPos, size);
        else
            return fullTextSearch(searchCriteria, startPos, size);
    }

    @Transactional
    public Integer getCount() {
        //Calculate the total num of item, total page and the reminder of item
        return em.createQuery("Select COUNT(f) from Product f", Long.class).getSingleResult().intValue();
    }

    @Transactional
    public Integer getCountByStatus(Product.FormStatus status) {
        return em.createQuery("Select COUNT(f) from Product f where f.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult().intValue();
    }

    @Transactional
    public Integer getCountYestday(Product.FormStatus status, Date startDate, Date endDate){
        return em.createQuery("Select COUNT(f) from Product f where f.status = :status and " +
                "f.createTime >= :startDate and " +
                "f.createTime < :endDate", Long.class)
                .setParameter("status", status)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult().intValue();
    }
}

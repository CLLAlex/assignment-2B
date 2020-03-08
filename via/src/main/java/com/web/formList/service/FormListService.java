package com.web.formList.service;

import com.via.dao.ProductDao;

import com.via.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

@Component
public class FormListService implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(FormListService.class);

    @Autowired
    private ProductDao productDao;

    public List<Product> findAll(){
        List<Product> productList = productDao.findAll();
        return productList;
    }

    @Transactional
    public Page<Product> findPaginated(Pageable pageable, Map<String, Object> searchCriteria) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        //List<Form> form = formDao.search(startItem, pageSize);
        Map<Long, List<Product>> result = productDao.search(searchCriteria, startItem, pageSize);
        Map.Entry<Long, List<Product>>  entry = result.entrySet().iterator().next();
        Page<Product> bookPage
                = new PageImpl<>(entry.getValue(), PageRequest.of(currentPage, pageSize), entry.getKey());

        return bookPage;
    }

    @Transactional
    public List<Product> getFormList(Date startDate, Date endDate){
        Calendar c = Calendar.getInstance();
        c.setTime(endDate);
        c.add(Calendar.DATE, 1);
        endDate = c.getTime();
        return productDao.find(startDate, endDate);

    }


    @Transactional
    public int getCount(){
       return productDao.getCount();
    }


}

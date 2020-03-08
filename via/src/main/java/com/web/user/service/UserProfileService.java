package com.web.user.service;

import com.via.dao.UserDetailsDao;
import com.via.entity.UserAccount;
import com.web.user.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class UserProfileService  {

    private final static Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    @Autowired
    private UserDetailsDao userDetailsDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public UserAccount getUserAccount(String name){
        logger.info("GetUserAccount, name: {}", name);
        return userDetailsDao.findByName(name);
    }

    @Transactional
    public void updateUserAccount(UserModel userModel){

        logger.info("updateUserAccount - START, update user: {}", userModel.getUser().getUsername());

        UserAccount user = userDetailsDao.findByName(userModel.getUser().getUsername());

        if(user == null)
            return;


        String newPw = userModel.getFirstPassword();
        newPw = bCryptPasswordEncoder.encode(newPw);

        user.setEmail(userModel.getUser().getEmail());
        user.setPassword(newPw);

        userDetailsDao.update(user);

        logger.info("updarteUserAccount - END");
    }


}

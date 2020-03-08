package com.web.index.controller.service;

import com.via.dao.CompanyDao;
import com.via.dao.UserDetailsDao;
import com.via.entity.Company;
import com.via.entity.UserAccount;
import com.via.entity.UserRole;
import com.web.user.model.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class RegistrationService {

    @Resource
    private CompanyDao companyDao;

    @Resource
    private UserDetailsDao userDetailsDao;

    @Resource
    private BCryptPasswordEncoder encoder;

    @Transactional
    public boolean register(UserModel userModel){
        try {
            UserAccount user = userModel.getUser();
            Company company = userModel.getUser().getCompany();

            companyDao.create(company);
            user.setCompany(company);
            user.setPassword(encoder.encode(userModel.getSecondPassword()));

            userDetailsDao.create(user);

            String role = "";

            switch (userModel.getRole()){
                case AGENT:
                    role = "ROLE_AGENT";
                    break;
                case BROKER:
                    role = "ROLE_BROKER";
                    break;
                case LLOYDS:
                    role = "ROLE_LLOYDS";
                    break;
                case INSERER:
                    role = "ROLE_INSERER";
                    break;
                case MPF_INTER:
                    role = "ROLE_MPF_INTER";
                    break;
                case USER:
                    role = "ROLE_USER";
                    break;
            }

            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(user);

            userDetailsDao.create(userRole);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;


    }

}

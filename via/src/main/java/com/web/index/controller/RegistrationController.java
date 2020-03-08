package com.web.index.controller;

import com.via.dao.CompanyDao;
import com.via.dao.UserDetailsDao;
import com.via.entity.Company;
import com.via.entity.UserAccount;
import com.via.entity.UserRole;
import com.web.form.model.CollectionForm;
import com.web.index.controller.service.RegistrationService;
import com.web.user.controller.ProfileController;
import com.web.user.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class RegistrationController {

    private final static Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Resource
    private RegistrationService registrationService;


    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public String registration(Model model, HttpServletRequest request){

        logger.info("registration request - START");
        UserModel userModel = new UserModel();
        model.addAttribute("userAccount", userModel);


        return "/registration";
    }

    @PostMapping("/register")
    public String formSubmit(@ModelAttribute UserModel userModel,
                             Model model, HttpSession session) {

        registrationService.register(userModel);




        return "form/updateSuccess";
    }

}

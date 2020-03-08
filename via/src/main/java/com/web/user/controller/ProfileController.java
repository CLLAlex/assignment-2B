package com.web.user.controller;

import com.via.entity.UserAccount;
import com.web.user.model.UserModel;
import com.web.user.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    private final static Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(value = {"/user/userProfile"}, method = RequestMethod.GET)
    public String userProfile(Model model, HttpServletRequest request){

        logger.info("userProfile request - START");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        UserAccount user = userProfileService.getUserAccount(name);
        UserModel userModel = new UserModel();
        userModel.setUser(user);
        model.addAttribute("userAccount", userModel);

        logger.info("userProfile request - END");
        return "/user/userProfile";
    }

    @RequestMapping(value = {"/user/userProfile/update"}, method = RequestMethod.POST)
    public String updateProfile(@ModelAttribute UserModel user){
        logger.info("updateProfile request - START");
        userProfileService.updateUserAccount(user);
        logger.info("updateProfile request - END");
        return "/user/updateSuccess";
    }
}

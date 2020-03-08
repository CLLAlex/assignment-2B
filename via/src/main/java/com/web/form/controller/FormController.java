package com.web.form.controller;


import com.web.form.model.CollectionForm;
import com.web.form.model.ImageSession;
import com.web.form.service.CollectionFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class FormController {

    private final static Logger logger = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private CollectionFormService service;

    @Autowired
    private ImageSession imageSession;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        /**Confirm the imageSession is empty when g*/
        imageSession.clear();
        return "form/form";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        /**Confirm the imageSession is empty when g*/
        imageSession.clear();
        return "form/form";
    }

    @PostMapping("/form")
    public String formSubmit(@ModelAttribute CollectionForm form, Model model, HttpSession session) {
        ImageSession imageSession1 = imageSession;

        Map<String, String> imageList = imageSession.getImagePath();
        List<Integer> deleteList = imageSession.getDeletionFiles();
        int currentFileSize = 0;

        if(imageList != null){
            if(deleteList != null){
                currentFileSize = imageList.size() - deleteList.size();
            }else{
                currentFileSize = imageList.size();
            }
        }

        if(currentFileSize == 0){
            if(imageSession.getImagePath() != null)
                imageSession.clear();
            return "redirect:form/error_photo";
        }
        /**Store images to the collectionForm DTO*/
        form.setFiles(imageSession.getImagePath());
        service.saveOrUpdateForm(form);

        /**When create form successfully, clear the image session.*/
        imageSession.clear();


        return "redirect:form/success";
    }

    @GetMapping("/form/success")
    public String formSubmit() {
        return "form/success";
    }

    @GetMapping("/form/error_photo")
    public String formSubmitFailed() {
        return "form/error_photo";
    }

    @PostMapping("/form/test")
    public String test(){
        return "test";
    }

    public FormController() {

    }

    @PostMapping("/form/file")
    public ResponseEntity uploadFile(MultipartHttpServletRequest request, HttpSession session) {


        logger.info("uploadFile");

        try {

            Iterator<String> itr = request.getFileNames();
            while (itr.hasNext()) {
                String uploadedFile = itr.next();
                MultipartFile file = request.getFile(uploadedFile);

                String newFilename = file.getOriginalFilename();// + sdf.format(new Date());

                try{
                    String path = "";
                    InputStream inputStream = null;
                    OutputStream outputStream = null;
                    if (file.getSize() > 0) {
                        inputStream = file.getInputStream();
                        path = service.createTempFolder() + "/" + newFilename;
                        outputStream = new FileOutputStream(path);

                        logger.info("Write File: {}", path);
                        int readBytes = 0;
                        byte[] buffer = new byte[8192];
                        while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
                            outputStream.write(buffer, 0, readBytes);
                        }

                        outputStream.close();
                        inputStream.close();

                        imageSession.addPath(newFilename, path);
                    }
                }catch(Exception ex){
                    logger.error(ex.getMessage());
                }

            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @RequestMapping("/form/file/remove")
    public ResponseEntity removeFile(@RequestParam(value = "name") String params) {
        logger.info("remove file");
        try {
            imageSession.delPath(params);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Removed photo", HttpStatus.OK);
    }

}

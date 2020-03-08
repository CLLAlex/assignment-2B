package com.web.form.controller;

import com.web.form.model.CollectionForm;
import com.web.form.model.ImageSession;
import com.web.form.service.CollectionFormService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;

@Controller
public class EditFormController implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(EditFormController.class);

    @Autowired
    private CollectionFormService service;

    @Autowired
    private ImageSession imageSession;

    @RequestMapping(value = {"/edit-form/edit-form"}, method = RequestMethod.GET)
    public String editForm(@RequestParam(value = "id", required = true) int id, Model model, HttpServletRequest request){
        imageSession.clear();
        model.addAttribute("collectionForm", service.findForm(id));
        return "/edit-form/edit-form";
    }

    @PostMapping("/edit-form")
    public String formSubmit(@ModelAttribute CollectionForm form,
                             Model model, HttpSession session) {

        form.setFiles(imageSession.getImagePath());
        service.updateForm(form);
        service.deletePhoto(form.getProduct(), imageSession.getDeletionFiles());

        /**When create form successfully, clear the image session.*/
        imageSession.clear();

        return "form/updateSuccess";
    }

    /**
     * The request mapping for edit page remove photo. If
     * @param name
     * @param id
     * @return
     */
    @RequestMapping("/edit-form/file/remove")
    public ResponseEntity removeFile(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "id", required = false) Integer id) {

        logger.info("removefile - START");
        try {
            if(id != null){
                logger.info("Remove file with id: {}", id);
                imageSession.addDeletionImages(id);
            }
            else if(name != null){
                logger.info("Delete Temp file with name: {}", name);
                imageSession.delPath(name);
            }else{
                logger.error("No input params");
                return new ResponseEntity<>("No input params", HttpStatus.UNPROCESSABLE_ENTITY);
            }

        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.info("RemoveFile - END");
            return new ResponseEntity<>("Remove failed" + e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        logger.info("RemoveFile - END");
        return new ResponseEntity<>("Remove successfully", HttpStatus.OK);
    }


    @RequestMapping(value = "/edit-form/export", method = RequestMethod.GET)
    public void exportForm(@RequestParam("id") int id, Workbook workbook, HttpServletResponse response){

    }
}

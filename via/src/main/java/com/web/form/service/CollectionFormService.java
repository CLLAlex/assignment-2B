package com.web.form.service;

import com.via.dao.ProductDao;
import com.via.entity.Product;
import com.web.form.model.CollectionForm;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("collectionFormService")
public class CollectionFormService implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(CollectionFormService.class);


    @Autowired
    private ProductDao productDao;

    @Value("${file.basePath}")
    private String parentPath;

    @Value("${file.resourceHeader}")
    private String resourceHeader;

    @Value("${self.address}")
    private String serverAddress;

    @Value("${self.port}")
    private String port;

    @Value("${self.header}")
    private String header;



    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @Transactional
    public CollectionForm findForm(int id){
        Product product = productDao.find(id);
        List<String> imagesPath = new ArrayList<>();

        CollectionForm cform = new CollectionForm();
        cform.setProduct(product);


        if(imagesPath != null)
            cform.setImagesPath(imagesPath);

        return cform;
    }

    @Transactional
    public String saveOrUpdateForm(CollectionForm cform){
        logger.info("CreateForm - START");

        Product product = cform.getProduct();
        product.setStatus(Product.FormStatus.PENDING);
        product.setRejectReason("");
        productDao.create(product);


        //Move the temp file to form folder*

        String utf16 = "\\u5df2\\u6210\\u529f\\u905e\\u4ea4\\u9928\\u85cf\\u8868\\u683c";
        String chinese = null;
        try{
            //byte[] utf16Byte = utf16.getBytes("UTF-16");
            //chinese = new String(utf16Byte, "UTF-16");
            chinese = StringEscapeUtils.unescapeJava(utf16);
        }catch (Exception e){
            e.printStackTrace();
        }

        uploadPhoto(product, cform.getFiles());


        return "Success";
    }

    @Transactional
    public String updateForm(CollectionForm cform){
        logger.info("updateForm - START");


        Product product = productDao.find(cform.getProduct().getId());
        product.setBorrowerName(cform.getProduct().getBorrowerName());
        product.setEmail(cform.getProduct().getEmail());
        product.setPhoneNum(cform.getProduct().getPhoneNum());
        product.setStatus(cform.getProduct().getStatus());
        product.setRejectReason(cform.getProduct().getRejectReason());

        productDao.update(product);

        //TODO move the template function to email-service

        /*Move the temp file to form folder*/
        uploadPhoto(product, cform.getFiles());

        return "Success";
    }

    @Transactional
    public String deletePhoto(Product product, List<Integer> ids){
        logger.info("delete photo - START");
        if(ids.size() == 0){
            logger.info("DeletePhoto - no items");
            return "no items";
        }

        return "Success";
    }

    public String uploadPhoto(Product product, Map<String, String> files){
        String newBasePath = createFormFolder(product.getId());
        for(Map.Entry<String, String> entry: files.entrySet()){

            String filename = entry.getKey();// + sdf.format(new Date());
            int pos = filename.lastIndexOf(".");

            //Append timestamp after the file name, to make sure that the image name is unique.
            String newFilename = filename.substring(0, pos - 1)
                    + sdf.format(new Date())
                    + filename.substring(pos);

            /*Get the temp file*/
            File targetFile = new File(entry.getValue());

            /*Move the file to new dir*/
            String newFullPath = newBasePath + "/" + newFilename;
            String contextPath = "/via" + resourceHeader + "/" + product.getId() + "/" + newFilename;
            targetFile.renameTo(new File(newFullPath));

        }
        return "END";
    }

    /**Create temp folder for image*/
    public String createTempFolder(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        logger.info("CreateTempFolder - START");
        String parentPath = "/Users/iotteam/Pictures/image/temp";
        Timestamp childPathDate = new Timestamp(System.currentTimeMillis());
        String childPath = parentPath.concat( "/" + sdf.format(childPathDate));

        File parentDirectory = new File(parentPath);;
        if (! parentDirectory.exists()){
            try{
                parentDirectory.mkdirs();
            }catch (Exception e){
                logger.error(e.getMessage());
            }
            logger.info("Parent folder created: {}", parentPath);
        }

        File childDirectory = new File(childPath);
        if (! childDirectory.exists()){
            childDirectory.mkdirs();
            logger.info("Child folder created: {}", childPath);
        }

        logger.info("CreateTempFolder - END");
        return childPath;
    }

    public String createFormFolder(int formId){
        logger.info("CreateFormFolder - START");

        //ToDo: Set the path to porperty file
        String childPath = parentPath + "/" + formId;

        File parentDir = new File(parentPath);
        if(!parentDir.exists()){
            parentDir.mkdirs();
            logger.info("Parent folder created: {}", parentPath);
        }

        File childDir = new File(childPath);
        if(!childDir.exists()){
            childDir.mkdirs();
            logger.info("Child folder created: {}", childDir);
        }

        logger.info("CreateFormFolder - END");
        return childPath;
    }

    public String createBaseUrl(){
        return header + "://" + serverAddress + ":" + port;
    }

}

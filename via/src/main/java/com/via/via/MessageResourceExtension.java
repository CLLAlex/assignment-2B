package com.via.via;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("messageSource")
public class MessageResourceExtension extends ResourceBundleMessageSource {

    private final static Logger logger = LoggerFactory.getLogger(MessageResourceExtension.class);

    /**
     * base name of the resource bundle folder
     */
    @Value(value = "${spring.messages.baseFolder:i18n}")
    private String baseFolder;

    /**
     * base name of the message file
     */
    @Value(value = "${spring.messages.basename:message}")
    private String basename;

    @PostConstruct
    public void init() {
        logger.info("init MessageResourceExtension...");
        if (!StringUtils.isEmpty(baseFolder)) {
            try {
                this.setBasenames(getAllBaseNames(baseFolder));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename(basename);
        this.setParentMessageSource(parent);
    }

    /**
     * Get all the i18n property file from/in the directory
     *
     * @param folderName
     * @return
     * @throws IOException
     */
    private String[] getAllBaseNames(String folderName) throws IOException {
        Resource resource = new ClassPathResource(folderName);
        File file = resource.getFile();
        List<String> baseNames = new ArrayList<>();
        if (file.exists() && file.isDirectory()) {
            this.getAllFile(baseNames, file, "");
        } else {
            logger.error("Base file is not exist");
        }
        return baseNames.toArray(new String[baseNames.size()]);
    }

    /**
     * Get all the file from i18n folder
     *
     * @param basenames
     * @param folder
     * @param path
     */
    private void getAllFile(List<String> basenames, File folder, String path) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                this.getAllFile(basenames, file, path + folder.getName() + File.separator);
            }
        } else {
            String i18Name = this.getI18FileName(path + folder.getName());
            if (!basenames.contains(i18Name)) {
                basenames.add(i18Name);
            }

        }
    }

    /**
     * convert folder name to basename
     *
     * @param filename
     * @return
     */
    private String getI18FileName(String filename) {
        filename = filename.replace(".properties", "");
        for (int i = 0; i < 2; i++) {
            int index = filename.lastIndexOf("_");
            if (index != -1) {
                filename = filename.substring(0, index);
            }
        }
        return filename;
    }
}
package com.web.form.model;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ImageSession {

    private Map<String, String> imagePath = new HashMap<>();

    private List<Integer> deletionFiles = new ArrayList<>();

    public Map<String, String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(Map<String, String> imagePath) {
        this.imagePath = imagePath;
    }

    public List<Integer> getDeletionFiles() {
        return deletionFiles;
    }

    public void setDeletionFiles(List<Integer> deletionFiles) {
        this.deletionFiles = deletionFiles;
    }

    public void clear(){
        imagePath.clear();
        deletionFiles.clear();
    }

    public void addPath(String filename, String path){
        imagePath.put(filename, path);
    }

    public void delPath(String filename){
        imagePath.remove(filename);
    }

    public void addDeletionImages(Integer id){
        deletionFiles.add(id);
    }

    public void clearDeletionImages(){
        deletionFiles.clear();
    }
}

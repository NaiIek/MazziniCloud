package com.mazzini.entity;

import java.io.InputStream;

public class FileEntity {
    private int id;
    private int authorid;
    private String filename;
    private InputStream filedata;
    private String filepath = "/";
    private long storageSize;

    public FileEntity(){
        // Ignored
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getAuthorId(){
        return authorid;
    }

    public void setAuthorId(int authorid){
        this.authorid = authorid;
    }

    public String getFileName(){
        return filename;
    }

    public void setFileName(String name){
        this.filename = name;
    }

    public InputStream getFileData(){
        return filedata;
    }

    public void setFileData(InputStream f){
        this.filedata = f;
    }

    public long getStorageSize(){
        return storageSize;
    }

    public void setStorageSize(long storageSize){
        this.storageSize = storageSize;
    }

    public String getFilePath(){
        return filepath;
    }

    public void setFilePath(String filepath){
        this.filepath = filepath;
    }
}
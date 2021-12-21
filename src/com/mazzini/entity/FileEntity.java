package com.mazzini.entity;

import java.io.InputStream;

public class FileEntity {
    private int id;
    private int authorid;
    private String filename;
    private InputStream filedata;

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
}
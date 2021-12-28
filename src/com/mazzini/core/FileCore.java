package com.mazzini.core;

import com.mazzini.dao.FileDAO;
import com.mazzini.entity.FileEntity;

import java.util.ArrayList;
import java.io.InputStream;

public class FileCore {

    public static ArrayList<FileEntity> getAllFiles(int authorid) {
        return new FileDAO().getAllFiles(authorid);
    }

    public static FileEntity create(int authorid, String filename, InputStream filedata){
        FileEntity f = new FileEntity();
        f.setAuthorId(authorid);
        f.setFileName(filename);
        f.setFileData(filedata);
        return new FileDAO().create(f);
    }

    public static FileEntity download(int fileId){
        return new FileDAO().getFileDataById(fileId);
    }

    public static void delete(int fileId){
        FileEntity f = new FileEntity();
        f.setId(fileId);
        new FileDAO().delete(f);
    }
}
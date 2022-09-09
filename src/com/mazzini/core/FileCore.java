package com.mazzini.core;

import com.mazzini.dao.FileDAO;
import com.mazzini.entity.FileEntity;

import java.util.ArrayList;
import java.io.InputStream;

public class FileCore {

    public static ArrayList<FileEntity> getAllFiles(int authorid){
        return new FileDAO().getAllFiles(authorid);
    }

    public static FileEntity create(int authorid, String filename, InputStream filedata, long inputSize){
        FileEntity f = new FileEntity(authorid,filename,filedata,inputSize+2048); // 2048 bytes added in count for security and other stored values in db
        return new FileDAO().create(f);
    }

    public static FileEntity download(int fileId){
        return new FileDAO().getFileDataById(fileId);
    }

    public static void rename(int fileId, String newname){
        FileEntity f = new FileEntity();
        f.setId(fileId);
        String oldName = new FileDAO().getFileById(fileId).getFileName();
        String[] nameSplit = oldName.split("\\.");
        String oldExtension = "."+nameSplit[nameSplit.length-1];
        String[] newNameSplit = newname.split("\\.");
        String newExtension = "."+newNameSplit[newNameSplit.length-1];
        if(newExtension.equals(oldExtension)){
            f.setFileName(newname);
        }
        else{
            f.setFileName(newname+oldExtension);
        } 
        new FileDAO().rename(f);
    }

    public static void delete(int fileId){
        FileEntity f = new FileEntity();
        f.setId(fileId);
        new FileDAO().delete(f);
    }

    public static Long getAllFilesSize(ArrayList<FileEntity> fileList){
        Long filesSize = Long.valueOf(0);
        for (FileEntity f : fileList){
            filesSize += f.getStorageSize();
        }
        return filesSize;
    }
}
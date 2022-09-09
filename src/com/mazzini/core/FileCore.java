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
        FileEntity f = new FileEntity(authorid,filename,filedata,inputSize);
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

    public static long getAllFilesSize(ArrayList<FileEntity> fileList){
        long filesSize = 0L;
        for (FileEntity f : fileList){
            filesSize += f.getStorageSize();
        }
        return filesSize;
    }

    public static int getAuthorId(int fileId){
        return new FileDAO().getOwnerId(fileId);
    }

    public static long getFileSize(int fileId){
        return new FileDAO().getFileSize(fileId);
    }
}
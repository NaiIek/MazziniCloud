package com.mazzini.dao;

import com.mazzini.entity.FileEntity;

import java.sql.*;
import java.util.ArrayList;
import java.io.InputStream;

public class FileDAO extends _Generic<FileEntity> {

    public ArrayList<FileEntity> getAllFiles(int authorid) {
        ArrayList<FileEntity> entities = new ArrayList<>();
        if (authorid != -1) {
            try {
                PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM attachment WHERE authorid = ? ORDER BY id DESC;");
                preparedStatement.setInt(1, authorid);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    FileEntity entity = new FileEntity();
                    entity.setId(resultSet.getInt("id"));
                    entity.setAuthorId(resultSet.getInt("authorid"));
                    entity.setFileName(resultSet.getString("filename"));
                    entity.setFilePath(resultSet.getString("filepath"));
                    entity.setStorageSize(resultSet.getLong("storageSize"));
                    entities.add(entity);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entities;
    }

    public FileEntity getFileById(int fileId){
        FileEntity f = new FileEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM attachment WHERE id = ?;");
            preparedStatement.setInt(1, fileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                f.setId(resultSet.getInt("id"));
                f.setAuthorId(resultSet.getInt("authorid"));
                f.setFileName(resultSet.getString("filename"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    public FileEntity getFileDataById(int fileId){
        FileEntity f = new FileEntity();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM attachment WHERE id = ?;");
            preparedStatement.setInt(1, fileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                f.setId(resultSet.getInt("id"));
                f.setAuthorId(resultSet.getInt("authorid"));
                f.setFileName(resultSet.getString("filename"));
                Blob b = resultSet.getBlob("filedata");
                InputStream i = asInputStream(b);
                f.setFileData(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return f;
    }

    public int getOwnerId(int fileId){
        int authorid = -1;
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT authorid FROM attachment WHERE id = ?;");
            preparedStatement.setInt(1, fileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                authorid = resultSet.getInt("authorid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorid;
    }

    public long getFileSize(int fileId){
        long filesize = -1L;
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT storageSize FROM attachment WHERE id = ?;");
            preparedStatement.setInt(1, fileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                filesize = resultSet.getLong("storageSize");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filesize;
    }

    public Boolean isOwner(int userId, int fileId){
        Boolean res = false;
        FileEntity f = getFileById(fileId);
        if(f.getAuthorId() == userId){
            res = true;
        }
        return res;
    }

    public void rename(FileEntity file){
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("UPDATE attachment SET filename = ? WHERE id = ?;");
            preparedStatement.setString(1, file.getFileName());
            preparedStatement.setInt(2, file.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FileEntity create(FileEntity obj) {
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("INSERT INTO attachment(authorid, filename, filedata, filepath, storageSize) " + "VALUES (?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, obj.getAuthorId());
            preparedStatement.setString(2, obj.getFileName());
            preparedStatement.setBlob(3, obj.getFileData());
            preparedStatement.setString(4, obj.getFilePath());
            preparedStatement.setLong(5, obj.getStorageSize());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return null;
    }

    @Override
    public void delete(FileEntity file) {
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("DELETE FROM attachment WHERE id = ?;");
            preparedStatement.setInt(1, file.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }        
    }

    private static InputStream asInputStream(Object o) throws SQLException {
        if (o == null) {
          return null;
        } else if (o instanceof Blob) {
          return ((Blob) o).getBinaryStream();
        }
        return (InputStream) o;
    }
}
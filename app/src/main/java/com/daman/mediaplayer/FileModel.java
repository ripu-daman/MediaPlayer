package com.daman.mediaplayer;

/**
 * Created by Daman on 29-03-2017.
 */

public class FileModel {
    int image;
    String name;
    String path;

    public FileModel() {
    }

    public FileModel(int image, String name,String path) {
        this.image = image;
        this.name = name;
        this.path=path;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

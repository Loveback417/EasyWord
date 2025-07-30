package com.example.easyword.enity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookimages")
public class BookImage {
    @Ignore
    public BookImage( @NonNull String name, @NonNull String image_path) {
        this.name = name;
        this.image_path = image_path;
    }

    public BookImage() {
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "name",   typeAffinity = ColumnInfo.TEXT )
    private String name;
    @NonNull
    @ColumnInfo(name = "image_path",   typeAffinity = ColumnInfo.TEXT )
    private String image_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(@NonNull String image_path) {
        this.image_path = image_path;
    }
}

package com.kayali_developer.objectsrecognition.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "objects")
public class Object {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String word;

    private String translation;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Object(String word, String translation, byte[] image) {
        this.word = word;
        this.translation = translation;
        this.image = image;
    }

    @Ignore
    public Object(long id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    @Ignore
    public Object() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

package com.kayali_developer.objectsrecognition.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.kayali_developer.objectsrecognition.data.model.Object;

import java.util.List;

@Dao
public interface ObjectDao {
    @Query("SELECT * FROM objects")
    List<Object> loadAllObjects();

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM objects WHERE word = :word) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END")
    boolean isExist(String word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertObject(Object object);

    @Query("DELETE FROM objects WHERE id = :id")
    int deleteObjectById(long id);

    @Query("DELETE FROM objects")
    int deleteAllObjects();

}

package com.dmitrijoss.geolock.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dmitrijoss.geolock.db.entity.SavedPackagesEntity;

import java.util.List;

@Dao
public interface SavedPackagesDao {

    @Query("SELECT * FROM packages")
    List<SavedPackagesEntity> getAll();

    @Query("SELECT * FROM packages")
    LiveData<List<SavedPackagesEntity>> getLiveData();

    @Insert
    void insert(SavedPackagesEntity savedPackagesEntity);

    @Delete
    int delete(SavedPackagesEntity savedPackagesEntity);
}

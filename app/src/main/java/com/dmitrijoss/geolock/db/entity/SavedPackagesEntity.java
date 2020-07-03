package com.dmitrijoss.geolock.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "packages")
public class SavedPackagesEntity {
    @PrimaryKey
    @NonNull
    public String packageName;

    public SavedPackagesEntity(@NonNull String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    public String getPackageName()   {
        return packageName;
    }

}

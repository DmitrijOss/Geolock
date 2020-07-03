package com.dmitrijoss.geolock.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dmitrijoss.geolock.db.dao.GeofenceDao;
import com.dmitrijoss.geolock.db.dao.SavedPackagesDao;
import com.dmitrijoss.geolock.db.entity.GeofenceEntities;
import com.dmitrijoss.geolock.db.entity.SavedPackagesEntity;


@Database(entities = {GeofenceEntities.class, SavedPackagesEntity.class}, version = 2, exportSchema = false)
public abstract class GeofenceDatabase extends RoomDatabase {

    private static volatile GeofenceDatabase sInstance;

    public abstract GeofenceDao geofenceDao();

    public abstract SavedPackagesDao savedPackagesDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `packages` (`packageName` TEXT NOT NULL, " +
                    "PRIMARY KEY(`packageName`))");
        }
    };

    public static GeofenceDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (GeofenceDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            GeofenceDatabase.class, "geofence_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return sInstance;
    }
}

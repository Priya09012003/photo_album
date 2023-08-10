package com.example.photo_album;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Myimages.class,version = 1)

public abstract class MyImagesDatabase extends RoomDatabase {

    private static MyImagesDatabase instance;
    public abstract MyimagesDao myimagesDao();

    public  static  synchronized MyImagesDatabase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MyImagesDatabase.class,"my_images_databse")
                    .fallbackToDestructiveMigration()
                  //  .allowMainThreadQueries() //-->not recommended
                    .build();
        }

        return instance;
    }
}


package com.example.photo_album;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyimagesDao
{
    @Insert
    void insert(Myimages myimages);

    @Delete
    void delete(Myimages myimages);

    @Update
    void update(Myimages myimages);

    @Query("SELECT * FROM my_images ORDER BY image_id ASC")
    LiveData<List<Myimages>>getAllimages();

}

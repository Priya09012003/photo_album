package com.example.photo_album;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyimagesViewModel extends AndroidViewModel {

    private MyImagesRepository repository;
    private LiveData<List<Myimages>>imagesList;
    public MyimagesViewModel(@NonNull Application application) {
        super(application);

        repository = new MyImagesRepository(application);
        imagesList =repository.getAllImages();
    }

    public void insert(Myimages myimages){
        repository.insert(myimages);
    }

    public void delete(Myimages myimages){
        repository.delete(myimages);
    }

    public void update(Myimages myimages){
        repository.update(myimages);
    }

    public LiveData<List<Myimages>>getAllImages(){
        return imagesList;
    }
}

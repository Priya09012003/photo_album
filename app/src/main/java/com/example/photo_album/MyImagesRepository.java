package com.example.photo_album;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyimagesDao myimagesDao;
    private LiveData<List<Myimages>> imagesList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyImagesRepository(Application application){
        MyImagesDatabase database =MyImagesDatabase.getInstance(application);
        myimagesDao=database.myimagesDao();
        imagesList=myimagesDao.getAllimages();
    }

    public void insert(Myimages myimages){
      //  new InsertImageAsyncTask(myimagesDao).execute(myimages);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.insert(myimages);
            }
        });

    }
    public void delete(Myimages myimages){
       // new DeleteImageAsyncTask(myimagesDao).execute(myimages);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.delete(myimages);
            }
        });

    }
    public void update(Myimages myimages){
       // new UpdateImageAsyncTask(myimagesDao).execute(myimages);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myimagesDao.update(myimages);
            }
        });

    }

    public LiveData<List<Myimages>> getAllImages(){

        return imagesList;
    }

    private static class InsertImageAsyncTask extends AsyncTask<Myimages,Void,Void>{

        MyimagesDao imagesDao;

        public  InsertImageAsyncTask(MyimagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(Myimages... myimages) {

            imagesDao.insert(myimages[0]);
            return null;
        }
    }

/*
    private static class DeleteImageAsyncTask extends AsyncTask<Myimages,Void,Void>{

        MyimagesDao imagesDao;

        public  DeleteImageAsyncTask(MyimagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(Myimages... myimages) {

            imagesDao.delete(myimages[0]);
            return null;
        }
    }


    private static class UpdateImageAsyncTask extends AsyncTask<Myimages,Void,Void>{

        MyimagesDao imagesDao;

        public  UpdateImageAsyncTask(MyimagesDao imagesDao){
            this.imagesDao = imagesDao;
        }
        @Override
        protected Void doInBackground(Myimages... myimages) {

            imagesDao.update(myimages[0]);
            return null;
        }
    }*/

}

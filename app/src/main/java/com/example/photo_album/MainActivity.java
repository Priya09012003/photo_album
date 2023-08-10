package com.example.photo_album;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab;


    private MyimagesViewModel myimagesViewModel;

    private ActivityResultLauncher<Intent>activityResultLauncherForAddImage;
    private ActivityResultLauncher<Intent>activityResultLauncherForUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerActivityForAddImage();
        registerActivityForUpdateImage();


        rv =findViewById(R.id.tv);
        fab=findViewById(R.id.fab);

        rv.setLayoutManager(new LinearLayoutManager(this));
       MyImagesAdapter adapter = new MyImagesAdapter();
       rv.setAdapter(adapter );

        myimagesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyimagesViewModel.class);

        myimagesViewModel.getAllImages().observe(MainActivity.this, new Observer<List<Myimages>>() {
            @Override
            public void onChanged(List<Myimages> myimages) {

                adapter.setImageList(myimages);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(MainActivity.this,AddImage.class);
                activityResultLauncherForAddImage.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                myimagesViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(rv);

        adapter.setListner(new MyImagesAdapter.OnImageClickListner() {
            @Override
            public void onImageClick(Myimages myimages) {

                Intent intent =new Intent(MainActivity.this,updateimg.class);
                intent.putExtra("id",myimages.getImage_id());
                intent.putExtra("title",myimages.getImage_title());
                intent.putExtra("description",myimages.getImage_description());
                intent.putExtra("image",myimages.getImage());

                activityResultLauncherForUpdateImage.launch(intent);


            }
        });
    }

    public void registerActivityForUpdateImage(){

        activityResultLauncherForUpdateImage
                =registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data=result.getData();

                        if(resultCode==RESULT_OK && data !=null){
                            String title=data.getStringExtra("updateTitle");
                            String description = data.getStringExtra("updatedescription");
                            byte[] image =data.getByteArrayExtra("image");
                            int id =data.getIntExtra("id",-1);

                            Myimages myimages=new Myimages(title,description,image);
                            myimages.setImage_id(id);
                            myimagesViewModel.update(myimages);
                        }

                    }
                });

    }

    public void  registerActivityForAddImage(){
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data =result.getData();

                        if(resultCode==RESULT_OK && data != null){

                            String title =data.getStringExtra("title");
                            String description=data.getStringExtra("description");
                            byte[] image=data.getByteArrayExtra("image");

                            Myimages myimages =new Myimages(title,description,image);
                            myimagesViewModel.insert(myimages);
                        }

                    }
                });

    }
}
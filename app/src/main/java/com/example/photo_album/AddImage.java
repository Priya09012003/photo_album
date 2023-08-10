package com.example.photo_album;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photo_album.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImage extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 1;

    private ImageView imageViewAddImage;
    private EditText editTextAddTitle,EditTextAddDescription;
    private Button buttonSave;

    ActivityResultLauncher<Intent>activityResultLauncherForSelectImage;

    private Bitmap selctedImage;
    private Bitmap scaledImage;

    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_image);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Your Image");
        registerActivityForSelectImage();

        imageViewAddImage=findViewById(R.id.img);
        editTextAddTitle=findViewById(R.id.edtitle);
        EditTextAddDescription=findViewById(R.id.edDescription);
        buttonSave=findViewById(R.id.btnsave);

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(AddImage.this
                        ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(AddImage.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                }
                else {
                    Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    activityResultLauncherForSelectImage.launch(intent);
                }

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selctedImage ==null){
                    Toast.makeText(AddImage.this,
                            "please select an image!",Toast.LENGTH_SHORT).show();
                }
                else {
                    String title =editTextAddTitle.getText().toString();
                    String description = EditTextAddDescription.getText().toString();
                    ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
                    selctedImage = makesmall(selctedImage,300);
                    selctedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    byte[] imageByteArray = outputStream.toByteArray();

                    Myimages myimage =new Myimages(title,description,imageByteArray);


                    Intent intent =new Intent();
                    intent.putExtra("title",title);
                    intent.putExtra("description",description);
                    intent.putExtra("image",imageByteArray);
                    setResult(RESULT_OK,intent);
                    finish();
                }




            }
        });
    }

    public void  registerActivityForSelectImage(){

        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode =result.getResultCode();
                        Intent data=result.getData();

                        if(resultCode ==RESULT_OK && data != null){
                            try {
                                selctedImage =MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());

                                imageViewAddImage.setImageBitmap(selctedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1  && grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activityResultLauncherForSelectImage.launch(intent);

        }
    }

    public Bitmap makesmall(Bitmap image,int maxsize){

        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width /(float) height;

        if (ratio >1){

            width =maxsize;
            height =(int) (width/ratio);
        }
        else {
            height =maxsize;
            width=(int) (height *ratio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }
}
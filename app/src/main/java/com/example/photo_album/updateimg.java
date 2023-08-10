package com.example.photo_album;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class updateimg extends AppCompatActivity {

    private ImageView imageViewUpdateImage;
    private EditText editTextUpdateTitle, editTextUpdateDescription;
    private Button buttonUpdate;

    private String title, description;
    private int id;
    private byte[] image;
    Toolbar toolbar1;

    ActivityResultLauncher<Intent> activityResultLauncherSelectImage;

    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateimg);

        // Set up the toolbar
        toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Your Image");

        // Register activity result launcher for selecting an image
        registerActivityForSelectImage();

        // Initialize views
        imageViewUpdateImage = findViewById(R.id.updateimg);
        editTextUpdateTitle = findViewById(R.id.edtitleupdate);
        editTextUpdateDescription = findViewById(R.id.edDescriptionupdate);
        buttonUpdate = findViewById(R.id.btnupdate);

        // Get data from the intent
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("updateTitle");  // Use "updateTitle" key
        description = getIntent().getStringExtra("updatedescription");  // Use "updatedescription" key
        image = getIntent().getByteArrayExtra("images");

        // Check if the image byte array is not null before setting the image
        if (image != null && image.length > 0) {
            imageViewUpdateImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        } else {
            // Handle the case when image is null or empty byte array
            // For example, show a default image or a placeholder
            // imageViewUpdateImage.setImageResource(R.drawable.default_image);
        }

        // Set click listener for selecting image
        imageViewUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherSelectImage.launch(intent);
            }
        });

        // Set click listener for the update button
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    // Method to handle updating data
    public void updateData() {
        if (id == -1) {
            Toast.makeText(updateimg.this, "There is a problem!", Toast.LENGTH_SHORT).show();
        } else {
            String updateTitle = editTextUpdateTitle.getText().toString();
            String updatedescription = editTextUpdateDescription.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updatedescription", updatedescription);

            if (selectedImage == null) {
                intent.putExtra("images", image);
            } else {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                selectedImage = makesmall(selectedImage, 300);
                selectedImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                byte[] imageByteArray = outputStream.toByteArray();
                intent.putExtra("images", imageByteArray);
            }

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // Register activity result launcher for selecting an image
    public void registerActivityForSelectImage() {
        activityResultLauncherSelectImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data != null) {
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), data.getData());

                                imageViewUpdateImage.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activityResultLauncherSelectImage.launch(intent);
        }
    }

    // Method to resize the image
    public Bitmap makesmall(Bitmap image, int maxsize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width / (float) height;

        if (ratio > 1) {
            width = maxsize;
            height = (int) (width / ratio);
        } else {
            height = maxsize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}

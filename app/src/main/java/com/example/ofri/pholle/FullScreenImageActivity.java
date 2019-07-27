package com.example.ofri.pholle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FullScreenImageActivity extends AppCompatActivity {

    ImageView fullScreen;

 //   FirebaseDatabase database;
 //   DatabaseReference myRef;
 //   FirebaseAuth mAuth;
    File file2;
//    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            file2 = (File) extras.get("pic");
        }
        fullScreen = findViewById(R.id.fullScreenImageView);
        fullScreen.setImageURI(Uri.fromFile(file2));
    }
}

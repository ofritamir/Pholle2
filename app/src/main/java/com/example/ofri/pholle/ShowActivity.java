package com.example.ofri.pholle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.service.autofill.RegexValidator;
import android.support.annotation.RequiresApi;
import android.support.design.internal.ParcelableSparseArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.Util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    TextView textView;
    public static File fileImage;
    StorageReference mStorageRef;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getText = findViewById(R.id.getTextButton);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Receipt");
        textView = findViewById(R.id.textView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Receipt");
        imageView = findViewById(R.id.imageView2);

        readDataFromDatabase();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowActivity.this, FullScreenImageActivity.class));
            }
        });


    }


    public void readDataFromDatabase(){
        textView.setText("");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.child(mAuth.getUid()).getChildren())
                {
                    Receipt r = new Receipt();
                    r.setCategory(ds.child("category").getValue().toString());
                    r.setEndDate(ds.child("endDate").getValue().toString());
                    r.setProductName(ds.child("productName").getValue().toString());
                    r.setReceiptID(ds.child("receiptID").getValue().toString());
                    r.setStartDate(ds.child("startDate").getValue().toString());
                    try {
                        fileImage = File.createTempFile("Receipt","jpg");
                        StorageReference pictureRef = mStorageRef.child(ds.child("receiptID").getValue().toString());
                        pictureRef.getFile(fileImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                imageView.setImageURI(Uri.fromFile(fileImage));
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    textView.append(r.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.v("DataTestError", "Failed to read value.", error.toException());
            }
        });
    }

    private TextView testTextView2;
    Button getText;


    public void getTextFromImage(View view) {
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
    //    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), imageView.getDrawable());

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(getApplicationContext(),"Couldent read text", Toast.LENGTH_LONG).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            List<Text> textLines = new ArrayList<>();
            for (int i=0;i<items.size();i++) {
                TextBlock textBlock = items.valueAt(i);

                List<? extends Text> textComper = textBlock.getComponents();
                for (Text currentText : textComper){
                    textLines.add(currentText);
                }
            }
            Collections.sort(textLines, new Comparator<Text>() {
                @Override
                public int compare(Text o1, Text o2) {
                    return o1.getBoundingBox().top - o2.getBoundingBox().top;
                }
            });
            ArrayList<String> test = new ArrayList<String>();
            for (Text text : textLines) {
                if (text != null && text.getValue() != null) {
                    test.add(text.getValue().toString());
                    sb.append(text.getValue() + "\n");
                }
            }
            String dateRegex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
            Pattern pattern = Pattern.compile(dateRegex);
            for (String string:test){
                if (string.matches(dateRegex)) {
                    Log.v("Date test", string);
                }
                else {
                    Log.v("Date test", "Didnt work");
                }
            }

            textView.setText(sb.toString());
            Log.v("text", textView.getText().toString());
        }
    }
}


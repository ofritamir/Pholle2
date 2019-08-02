package com.example.ofri.pholle;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.ofri.pholle.Search.test;

public class MainPageActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    private ImageButton logOutBtn;
    private Spinner spinnerDropDown;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView welcomeText;
    private Button buttonTest;
    CardView amitBtn;
    CardView receiptBtn;
    CardView warrantyBtn;

            public void adding(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "click BACK again to exit Pholle", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Receipt");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainPageActivity.this, LoginActivity.class));

                }
            }
        };

        amitBtn = findViewById(R.id.search);
        amitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this,Search.class));
            }
        });

        receiptBtn = findViewById(R.id.receipt);
        receiptBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                test = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child(mAuth.getUid()).getChildren()) {
                            WarrantyObj w = new WarrantyObj();


                            w.setType(ds.child("type").getValue().toString());
                            if(w.getType().equals("Receipt")) {
                                w.setReceiptID(ds.child("receiptID").getValue().toString());
                                w.setCategory(ds.child("category").getValue().toString());
                                w.setStartDate(ds.child("startDate").getValue().toString());
                                w.setStoreName(ds.child("storeName").getValue().toString());
                                w.setEndDate(ds.child("endDate").getValue().toString());
                                test.add(w);
                            }
                            else {
                                continue;
                            }
                        }

                    }                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(MainPageActivity.this,SearchList.class);
                intent.putExtra("type", "Receipt");
                startActivity(intent);
            }
        });

        warrantyBtn = findViewById(R.id.warranty);
        warrantyBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                test = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child(mAuth.getUid()).getChildren()) {
                            WarrantyObj w = new WarrantyObj();
                            w.setType(ds.child("type").getValue().toString());
                            if(w.getType().equals("Warranty")) {
                                w.setReceiptID(ds.child("receiptID").getValue().toString());
                                w.setCategory(ds.child("category").getValue().toString());
                                w.setStartDate(ds.child("startDate").getValue().toString());
                                w.setStoreName(ds.child("storeName").getValue().toString());
                                w.setEndDate(ds.child("endDate").getValue().toString());
                                test.add(w);
                            }
                            else{
                                continue;
                            }


                        }

                    }                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(MainPageActivity.this,SearchList.class);
                intent.putExtra("type", "Warranty");
                startActivity(intent);
            }
        });



        welcomeText = (TextView) findViewById(R.id.welcomeTextView);
        FirebaseUser user = mAuth.getCurrentUser();
        int num = user.getEmail().indexOf("@");
        String name = user.getEmail().substring(0,num);
        welcomeText.setText("Welcome "+name);

        logOutBtn = (ImageButton) findViewById(R.id.logOut);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.addItem:
                startActivity(new Intent(MainPageActivity.this, AddActivity.class));
                break;
            case R.id.searchItem:
                startActivity(new Intent(MainPageActivity.this, Search.class));
                break;
            case R.id.warrantyItem:
                test = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child(mAuth.getUid()).getChildren()) {
                            WarrantyObj w = new WarrantyObj();
                            w.setType(ds.child("type").getValue().toString());
                            if(w.getType().equals("Warranty")) {
                                w.setReceiptID(ds.child("receiptID").getValue().toString());
                                w.setCategory(ds.child("category").getValue().toString());
                                w.setStartDate(ds.child("startDate").getValue().toString());
                                w.setStoreName(ds.child("storeName").getValue().toString());
                                w.setEndDate(ds.child("endDate").getValue().toString());
                                test.add(w);
                            }
                            else{
                                continue;
                            }


                        }

                    }                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(MainPageActivity.this,SearchList.class);
                intent.putExtra("type", "Warranty");
                startActivity(intent);
                break;

            case R.id.receiptItem:
                test = new ArrayList<>();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child(mAuth.getUid()).getChildren()) {
                            WarrantyObj w = new WarrantyObj();


                            w.setType(ds.child("type").getValue().toString());
                            if(w.getType().equals("Receipt")) {
                                w.setReceiptID(ds.child("receiptID").getValue().toString());
                                w.setCategory(ds.child("category").getValue().toString());
                                w.setStartDate(ds.child("startDate").getValue().toString());
                                w.setStoreName(ds.child("storeName").getValue().toString());
                                w.setEndDate(ds.child("endDate").getValue().toString());
                                test.add(w);
                            }
                            else {
                                continue;
                            }
                        }

                    }                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent2 = new Intent(MainPageActivity.this,SearchList.class);
                intent2.putExtra("type", "Receipt");
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

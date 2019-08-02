package com.example.ofri.pholle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import static com.example.ofri.pholle.Search.test;

public class SearchList extends AppCompatActivity {
    RecyclerView rv;
    String type;
    TextView typeText;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Receipt");
        typeText = findViewById(R.id.typeTextView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = (String) extras.get("type");
        }
        typeText.setText(type);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        SearchAdapter adapter = new SearchAdapter(test);
        rv.setAdapter(adapter);
    }

}
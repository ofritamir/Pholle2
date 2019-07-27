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

public class MainPageActivity extends AppCompatActivity {

    private ImageButton logOutBtn;
    private Spinner spinnerDropDown;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView welcomeText;
    private Button buttonTest;

    CardView amitBtn;

   /* public void fillMenu(){
        String[] menu = new String[] {"", "Home", "Receipt", "Warranty", "Add New"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, menu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDropDown.setAdapter(adapter);
        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Add New")) {
                    startActivity(new Intent(MainPageActivity.this, AddActivity.class));
                } else if (parent.getItemAtPosition(position).equals("Receipt")) {
                    startActivity(new Intent(MainPageActivity.this, Search.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/



            public void adding(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);

         //   super.onBackPressed();
         //   return;
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




 /*   public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            System.exit(0);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    } */

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


      /* buttonTest = findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, ShowActivity.class));
            }
        });*/




       // spinnerDropDown = (Spinner) findViewById(R.id.spinnerMenu);
       // fillMenu();



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
        }


        return super.onOptionsItemSelected(item);
    }
}

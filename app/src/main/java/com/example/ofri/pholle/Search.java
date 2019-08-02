package com.example.ofri.pholle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Search extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    static List<WarrantyObj> test;



    private ImageButton calendar;
    static EditText storeName;
    public Spinner category;
    ArrayAdapter<CharSequence> adpter;
    public CheckBox checkBoxExpire;
    public CheckBox checkBoxValid;
    public CheckBox checkBoxRec;
    public CheckBox checkBoxWarr;
    public Button btnSearch;
    static TextView editTextDate;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextDate = findViewById(R.id.textViewDate);
        btnSearch = findViewById(R.id.SearchWarrantyButton);

        storeName = findViewById(R.id.email_input);
        category= findViewById(R.id.spinner);
        adpter = ArrayAdapter.createFromResource(this,R.array.category,android.R.layout.simple_spinner_item);
        adpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adpter);
        calendar = findViewById(R.id. calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });
        checkBoxExpire = findViewById(R.id.expired);
        checkBoxValid = findViewById(R.id.valid);
        checkBoxRec = findViewById(R.id.receipt);
        checkBoxWarr = findViewById(R.id.warranty);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Receipt");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test = new ArrayList<>();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.child(mAuth.getUid()).getChildren()) {
                            WarrantyObj w = new WarrantyObj();
                            w.setReceiptID(ds.child("receiptID").getValue().toString());
                            w.setCategory(ds.child("category").getValue().toString());
                            w.setStartDate(ds.child("startDate").getValue().toString());
                            w.setStoreName(ds.child("storeName").getValue().toString());
                            w.setEndDate(ds.child("endDate").getValue().toString());
                            w.setType(ds.child("type").getValue().toString());


                            if(category.getSelectedItem().toString().equals("")&&editTextDate.getText().toString().equals("")
                                    &&storeName.getText().toString().equals("")&&!checkBoxExpire.isChecked()&&!checkBoxValid.isChecked()
                                    &&!checkBoxWarr.isChecked()&&!checkBoxRec.isChecked())
                            {
                                test.add(w);
                            }

                           else if(w.getCategory().equals(category.getSelectedItem().toString())||category.getSelectedItem().toString().equals(""))
                            {
                                if(isPackageExpired(editTextDate.getText().toString(),w.getStartDate())||editTextDate.getText().toString().equals(""))
                                {
                                    if(w.getStoreName().equals(storeName.getText().toString())||storeName.getText().toString().equals(""))
                                    {
                                        if(checkBoxValid.isChecked()&&checkBoxExpire.isChecked())
                                        {
                                            checkType(w);
                                        }
                                        else if(checkBoxValid.isChecked()&&!isPackageExpired(w.getEndDate()))
                                        {
                                            checkType(w);
                                        }
                                        else if(checkBoxExpire.isChecked()&&isPackageExpired(w.getEndDate()))
                                        {
                                            checkType(w);
                                        }
                                        else if(!checkBoxValid.isChecked()&&!checkBoxExpire.isChecked()){
                                            checkType(w);
                                        }
                                    }
                                    else{
                                        continue;
                                    }
                                }
                                else {
                                    continue;
                                }
                            }
                            else {
                                continue;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                Intent intent = new Intent(Search.this,SearchList.class);
                if ((checkBoxWarr.isChecked() && checkBoxRec.isChecked()) || (!checkBoxWarr.isChecked() && !checkBoxRec.isChecked())) {
                    intent.putExtra("type", "Receipt and Warranty");
                }
                else if (checkBoxWarr.isChecked()) {
                    intent.putExtra("type", "Warranty");
                }
                else {
                    intent.putExtra("type", "Receipt");
                }
                startActivity(intent);

            }
        });
    }

    private boolean isPackageExpired(String date){
        boolean isExpired=false;
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = date;

        try {

            Date date2 = formatter.parse(dateInString);
            if (today.after(date2)) isExpired=true;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isExpired;
    }


    private boolean isPackageExpired(String date,String checkdate){
        boolean isExpired=false;
        if(date.equals("")||checkdate.equals("")){
            return isExpired;
        }
        else {
            Date checkDate = stringToDate(checkdate, "dd/mm/yyyy");
            Date expiredDate = stringToDate(date, "dd/mm/yyyy");
            if (checkDate.after(expiredDate)) isExpired = true;

            return isExpired;
        }
    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    private void checkType(WarrantyObj w)
    {
        if(w.getType().equals("Receipt")&&checkBoxRec.isChecked()){
            test.add(w);
        }
        else if(w.getType().equals("Warranty")&&checkBoxWarr.isChecked()){
            test.add(w);
        }
        else  if (!checkBoxWarr.isChecked()&&!checkBoxRec.isChecked())
        {
            test.add(w);
        }
    }



    public void showTruitonDatePickerDialog(View v) {

        DialogFragment newFragment = new Search.SearchDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class SearchDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            editTextDate.setText(day + "/" + (month + 1) + "/" + year);
        }

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
                startActivity(new Intent(Search.this, AddActivity.class));
                break;
            case R.id.homeItem:
                startActivity(new Intent(Search.this, MainPageActivity.class));
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
                Intent intent = new Intent(Search.this,SearchList.class);
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
                Intent intent2 = new Intent(Search.this,SearchList.class);
                intent2.putExtra("type", "Receipt");
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

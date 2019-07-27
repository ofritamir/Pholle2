package com.example.ofri.pholle;

import android.Manifest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AddActivity extends FragmentActivity {
    static EditText StartDateEdit;
    static EditText EndDateEdit;
    private TextView storeName;
    private ImageButton takePicButton;
    private ImageView imageView;
    static Uri file;
    Button smartScan;
    Uri testFile;
    private String fullScreenInd;
    private Button addBtn;
    Spinner spinner;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;


    public void addReceipt() {
        String startDate = StartDateEdit.getText().toString();
        String endDate = EndDateEdit.getText().toString();
        String prouctName = storeName.getText().toString();
        String category = spinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) ||
                TextUtils.isEmpty(prouctName) || TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Empty!", Toast.LENGTH_SHORT).show();
        } else {

            try {
                String id = databaseReference.push().getKey();
                String id2 = firebaseAuth.getUid();
                Receipt receipt = new Receipt(id, startDate, endDate, prouctName, category);
                firebaseDatabase.getReference().child("Receipt").child(id2).child(id).setValue(receipt);
                StorageReference pictureRef = mStorageRef.child(id);
                pictureRef.putFile(testFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
                Toast.makeText(this, "Receipt added successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddActivity.this, MainPageActivity.class));

                //  StartDateEdit.setText("");
                //  EndDateEdit.setText("");
                //  Name.setText("");
                // spinner.setSelection(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // show the current date on start! show worng month!
 /*   public String currentDate(){
        String day = "";
        String month = "";
        String year = "";
        String date = "";
        Calendar calendar = Calendar.getInstance();
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = calendar.get(Calendar.MONTH);

        day = Integer.toString(day1);
        month = Integer.toString(mon);

        year = Integer.toString(calendar.get(Calendar.YEAR));
        date = day+"/"+month+"/"+year;
        return date;

    } */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        takePicButton = findViewById(R.id.imageButton);
        smartScan = findViewById(R.id.smartScan);
        imageView = findViewById(R.id.imageViewTest);
        storeName = findViewById(R.id.productName);
        addBtn = findViewById(R.id.addButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("receipt");
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance(); // testing
        // spouse to show the current date. problem with month!
        //   StartDateEdit = (EditText) findViewById(R.id.startDate);
        //StartDateEdit.setText(currentDate());

        mStorageRef = FirebaseStorage.getInstance().getReference("Receipt");


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceipt();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePicButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        fullScreenInd = getIntent().getStringExtra("fullScreenIndicator");
        if ("y".equals(fullScreenInd)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });
        smartScan.setOnClickListener(v -> {
            try {
                setSmartScan();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




        StartDateEdit = (EditText) findViewById(R.id.startDate);
        StartDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });

        EndDateEdit = (EditText) findViewById(R.id.endDate);
        EndDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });

        //בניית spinner קטגוריות דרך קובץ strings.xml

        spinner = findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePicButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
        imageView.getLayoutParams().width = 800;
        imageView.getLayoutParams().height = 800;
        takePicButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
                testFile = file;
            }
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }


    //קריאה להפעלת בחירת תאריך מוצר לפי התחלה או סוף

    public void showTruitonDatePickerDialog(View v) {
        if (v.equals(StartDateEdit)) {
            DialogFragment newFragment = new StartDatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } else if (v.equals(EndDateEdit)) {
            DialogFragment newFragment = new EndDatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }


    //הפעלת בחירת תאריך התחלה של הקבלה

    public static class StartDatePickerFragment extends DialogFragment implements
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

            StartDateEdit.setText(day + "/" + (month + 1) + "/" + year);

        }

    }

    //הפעלת בחירת תאריך סיום של הקבלה

    public static class EndDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH) + 1;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            EndDateEdit.setText(day + "/" + (month + 1) + "/" + year);

        }

    }

    public void setSmartScan() throws IOException {
        imageView.buildDrawingCache();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), file);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(getApplicationContext(), "Couldent read text", Toast.LENGTH_LONG).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            List<Text> textLines = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                TextBlock textBlock = items.valueAt(i);
                List<? extends Text> textComper = textBlock.getComponents();
                for (Text currentText : textComper) {
                    textLines.add(currentText);
                }
            }
            Collections.sort(textLines, new Comparator<Text>() {
                @Override
                public int compare(Text o1, Text o2) {
                    return o1.getBoundingBox().top - o2.getBoundingBox().top;
                }
            });
            ArrayList<String> stringArrayList = new ArrayList<String>();
            for (Text text : textLines) {
                if (text != null && text.getValue() != null) {
                    stringArrayList.add(text.getValue());
                }
            }
            String dateRegex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
            for (String string : stringArrayList) {
                for (int i = 0; i < string.length() - 8; i++) {
                    String date = string.substring(i, i + 8);
                    if (date.matches(dateRegex)) {
                        StartDateEdit.setText(date);
                        break;
                    }
                }
            }
            for (String string : stringArrayList) {
                for (int i=0;i<string.length()-9;i++) {
                    String stringName = string.substring(i,i+9);
                    if (stringName.equals("DECATHLON")) {
                        storeName.setText(stringName);
                        break;
                    }
                }
            }
        }
    }

}

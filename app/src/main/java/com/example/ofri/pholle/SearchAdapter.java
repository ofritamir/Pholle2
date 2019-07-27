package com.example.ofri.pholle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import static com.example.ofri.pholle.Search.test;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    public static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Receipt");

    public Context ctx;
    public File localFile;


    private List<WarrantyObj> dataList;
    public SearchAdapter(List<WarrantyObj> dataList){
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ctx =viewGroup.getContext();
        View v=LayoutInflater.from(ctx)
                .inflate(R.layout.search_cardview,viewGroup,false);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder myViewHolder, int i) {
        WarrantyObj w= dataList.get(i);

        myViewHolder.setData(w);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView endDate;
        private TextView storeName;
        private TextView startDate;
        private ImageButton pic;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName=itemView.findViewById(R.id.storeNameEmpty);
            startDate= itemView.findViewById(R.id.startDateEmpty);
            endDate=itemView.findViewById(R.id.endDateEmpty);
            pic=itemView.findViewById(R.id.imageButtonPic);
        }

        public void setData(WarrantyObj data) {
            storeName.setText(data.getStoreName());
            startDate.setText(data.getStartDate());
            endDate.setText(data.getEndDate());

            try {
                File localFile2 = File.createTempFile("Receipt","jpg");
                localFile = File.createTempFile("Receipt","jpg");
                StorageReference mStorageRef2 = mStorageRef.child(data.getReceiptID());
                mStorageRef2.getFile(localFile2).addOnSuccessListener(taskSnapshot -> pic.setImageURI(Uri.fromFile(localFile2)));
                pic.setOnClickListener(v -> {
                    Intent intent = new Intent(ctx,FullScreenImageActivity.class);
                    intent.putExtra("pic",localFile2);
                    v.getContext().startActivity(intent);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

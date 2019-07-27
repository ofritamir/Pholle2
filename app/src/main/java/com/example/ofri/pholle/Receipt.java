package com.example.ofri.pholle;

import android.graphics.Bitmap;
import android.opengl.Matrix;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

public class Receipt {
    String receiptID;
    String startDate;
    String endDate;
    String productName;
    String category;


    public Receipt(){}

    public Receipt(String receiptID, String startDate, String endDate, String productName, String category) {
        this.receiptID = receiptID;
            this.startDate = startDate;
        this.endDate = endDate;
        this.productName = productName;
        if (category !=null) {
            this.category = category;
        } else {this.category = "";}
    }


    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        this.receiptID = receiptID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return ("Product Name: "+productName+"\nStart Date: "+startDate+"\n" +
                "End Date: "+endDate+"\n"+"Category: "+category+"\n"+"\n");
    }
}

package com.example.ofri.pholle;

import android.net.Uri;

import java.io.File;

public class WarrantyObj {

   // String name;
   // String category;
   // int date;

    ///////////////////////////////////////////////////////////////

    String receiptID;
    String startDate;
    String endDate;
    String storeName;
    String category;
    String type;



    public WarrantyObj(){}

    public WarrantyObj(String receiptID, String startDate, String endDate, String storeName, String category, String type) {
        this.receiptID = receiptID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.storeName = storeName;
        this.category = category;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String productName) {
        this.storeName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}

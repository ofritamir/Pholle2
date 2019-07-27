package com.example.ofri.pholle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class PholleApp extends android.app.Application {

        @Override
        public void onCreate() {
            super.onCreate();

            //Newer version of Firebase
            if(!FirebaseApp.getApps(this).isEmpty()) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
        }

}

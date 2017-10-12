package com.example.hp.delete_contact;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
     Button button;
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

            } else {

                //  Request fot permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                /*
                 MY_PERMISSIONS_REQUEST_READ_CONTACTS is an app-defined int constant.
                 The callback method gets the result of the request.
                  */
            }
        }else{

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteContact();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    /*
                    Permission is granted and execute the contacts-related task .
                     Since reading contacts takes more time, let's run it on a separate thread.
                     */
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteContact();
                        }
                    });
                } else {

                    //Permission is denied,disable the functionality that depends on this permission.
                    Toast.makeText(this, "You've denied the required permission.", Toast.LENGTH_LONG);
                }
                return;
            }

        }
    }

    private void deleteContact() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Creating layout inflater object
        LayoutInflater inflater = getLayoutInflater();

        /*
         Inflate and set the layout for the dialog Pass null as the parent view because its going
           in the dialog layout.
          */
        View view = inflater.inflate(R.layout.contact, null);

        final EditText phone = view.findViewById(R.id.input_contact_phone);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.delete_contact, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactPhone = phone.getText().toString();

                        int deleteStatus = Content_Provider.deleteContact(getContentResolver(), contactPhone);

                        if (deleteStatus == 1) {
                            Toast.makeText(getApplicationContext(), "Deleted successfully.",
                                    Toast.LENGTH_LONG).show();
                        } else if(deleteStatus == 0){
                            Toast.makeText(getApplicationContext(), "Number doesn't exist.",
                                    Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Failed to delete.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}




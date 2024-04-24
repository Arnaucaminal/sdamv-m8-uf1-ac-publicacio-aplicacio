package com.acaminal.sdamv_m8_uf1_ac_publicacio_aplicacio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private SharedPreferences eulaPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        eulaPreferences = getSharedPreferences("eula_preferences", MODE_PRIVATE);

        if (!eulaPreferences.getBoolean("acceptedEULA", false)) {
            mostrarEULA();
        } else {
            requestContactsPermission();
        }
    }

    public void mostrarDialogEULA(View view){
        mostrarEULA();
    }

    public void mostrarEULA() {
        AlertDialog alertDialogEULA = new AlertDialog.Builder(MainActivity.this).create();
        alertDialogEULA.setTitle("EULA");
        alertDialogEULA.setMessage(getString(R.string.EULA));

        // Accept button
        alertDialogEULA.setButton(AlertDialog.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Set the EULA to accepted
                SharedPreferences.Editor editor = eulaPreferences.edit();
                editor.putBoolean("acceptedEULA", true);
                editor.apply();

                // Request permissions after EULA has been accepted
                requestContactsPermission();
            }
        });

        // Decline button
        alertDialogEULA.setButton(AlertDialog.BUTTON_NEGATIVE, "Decline", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Close the app if EULA is not accepted
                Toast.makeText(MainActivity.this, "You must accept the EULA to use the application.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        alertDialogEULA.show();
    }

    private void requestContactsPermission() {
        // Check if permission is already granted
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            Toast.makeText(this, "Contacts permission already granted.", Toast.LENGTH_SHORT).show();
        } else {
            // Permission is not yet granted, request it
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(this, "Contacts permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                // Permission was denied
                Toast.makeText(this, "Contacts permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
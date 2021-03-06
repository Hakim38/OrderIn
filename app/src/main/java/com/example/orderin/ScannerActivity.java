package com.example.orderin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {

    public static final String INTENT_KEY = "result";

    private CodeScanner mCodeScanner;
    private TextView tvResult;
    private boolean CameraAllowed;
    private final int CAMERA_PERM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        tvResult = findViewById(R.id.textView);

        CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);

        CameraAllowed = false;

        //asks user for permission to use camera
        cameraPermission();
        if (CameraAllowed) {
            //get internet permissions
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    //decodes QR code into Result variable
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("testLog", "run: decoding");
                           if (isNumeric(result.getText().toString())) {
                                //number of tables from 1 to 4
                                if (Integer.parseInt(result.getText().toString()) > 0 && Integer.parseInt(result.getText().toString()) < 5) {
                                    int num = Integer.parseInt(result.getText().toString());
                                    toMenu(num);
                                }
                            }
                            else {
                                //QR code is not a recognised value
                                Toast.makeText(ScannerActivity.this, result.getText() + " is not a recognised table.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //resets camera when clicked
                    mCodeScanner.startPreview();
                }
            });
        }
    }

    private void toMenu(int table){
        //takes user to menu
        Intent intent = new Intent(ScannerActivity.this, MenuActivity.class);
        intent.putExtra(INTENT_KEY, table);
        startActivity(intent);
        finish();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void cameraPermission(){
        //zistenie verzie Android
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);
            }
            else {
                mCodeScanner.startPreview();
                CameraAllowed = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM) {
            //ak u?? bola kamera povolena, sp??????ame skener
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mCodeScanner.startPreview();
                CameraAllowed = true;
            }
            else {
                //ak kamera nie je povolen??, otv??rame dial??gov?? okno
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("This app requires permission to use Camera")
                            .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                @Override
                                //pr??stup povolen??
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        //pr??stup zamietnut??
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).create().show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onPause() {
        if (CameraAllowed) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }
}
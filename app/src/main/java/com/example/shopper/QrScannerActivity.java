package com.example.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

public class QrScannerActivity extends AppCompatActivity {
CodeScanner codeScanner;
CodeScannerView scannerView;
TextView resultData;
ImageView qrscannerimg;
Button searchforproduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this,scannerView);
        resultData = findViewById(R.id.resultData);
        searchforproduct = findViewById(R.id.searchforproductbtn);
        qrscannerimg = findViewById(R.id.qrscannerimage);
        qrscannerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browsebtn();
            }
        });
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        resultData.setText(result.getText());
                        searchforproduct.setVisibility(View.VISIBLE);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }

                    }
                });
            }
        });
        searchforproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent producctDetailsIntent = new Intent(QrScannerActivity.this, ProductDetailsActivity.class);
                producctDetailsIntent.putExtra("product_ID", resultData.getText().toString());
                startActivity(producctDetailsIntent);
                finish();
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
        if (resultData.getText().length() != 20){
            searchforproduct.setVisibility(View.GONE);
            Toast.makeText(QrScannerActivity.this, "This is not valid product link of Shopping Fire!", Toast.LENGTH_LONG).show();
        }else {
            searchforproduct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(QrScannerActivity.this, "Please allow camera permission!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    public void  browsebtn(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1000);
    }
    @Override

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {

        super.onActivityResult(reqCode, resultCode, data);





        if (resultCode == RESULT_OK) {

            try {

                final Uri imageUri = data.getData();

                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                try {

                    Bitmap bMap = selectedImage;

                    String contents = null;



                    int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];

                    bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());



                    LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));



                    MultiFormatReader reader = new MultiFormatReader();

                    Result result = reader.decode(bitmap);
                    contents = result.getText();
                    resultData.setText(contents);
                    searchforproduct.setVisibility(View.VISIBLE);
                    codeScanner.stopPreview();

                    Toast.makeText(getApplicationContext(),contents,Toast.LENGTH_LONG).show();



                }catch (Exception e){

                    e.printStackTrace();

                }

                //  image_view.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {

                e.printStackTrace();

                Toast.makeText(QrScannerActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }



        }else {

            Toast.makeText(QrScannerActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();

        }

    }
}
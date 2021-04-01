package com.example.shopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrGeneratorActivity extends AppCompatActivity {
    String Tag = "GenerateQrCode";
    private TextView qrGeneratortext;
    private ImageView qrgenereatesavebtn, qrshareBtn;
    private EditText qrGenerateEdit;
    private ImageView qrCode,scanit;

    private Button qrgeneratebtn;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    String inputvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        qrGeneratortext = findViewById(R.id.qrgeneratortext);
        qrgenereatesavebtn = findViewById(R.id.downloadQr);
        qrgeneratebtn = findViewById(R.id.qrgenereatebtn);
        qrGenerateEdit = findViewById(R.id.qrgeneratorEdit);
        qrCode = findViewById(R.id.qrCode);
        qrshareBtn = findViewById(R.id.shareQr);
        scanit = findViewById(R.id.scanit);

        final String productid = getIntent().getStringExtra("ProductID");
        qrGenerateEdit.setText(productid);
        ActivityCompat.requestPermissions(qrGeneratorActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(qrGeneratorActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        scanit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrGeneratorActivity.this,QrScannerActivity.class);
                startActivity(intent);
            }
        });
        qrshareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image();
                finish();
            }
        });

        qrgenereatesavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery();
                Toast.makeText(qrGeneratorActivity.this, "Image saved to Gallery.", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        qrgeneratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputvalue = qrGenerateEdit.getText().toString().trim();
                if (inputvalue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    qrgEncoder = new QRGEncoder(inputvalue, null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrCode.setAlpha((float) 1);
                        qrCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(Tag, e.toString());
                    }
                } else {
                    qrGenerateEdit.setError("Required!");
                }
            }
        });


    }

    private void saveToGallery() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCode.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath()+"/qrcodeshoppingfire");
        dir.mkdirs();
        String fileName = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,fileName);
        try {
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try {
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void image(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable = (BitmapDrawable) qrCode.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File f = new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");

        Intent shareIntent;
        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

            outputStream.flush();
            outputStream.close();
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }catch (Exception e){
            throw  new RuntimeException(e);
        }
        startActivity(Intent.createChooser(shareIntent,"Share this Qrcode by"));
    }
}
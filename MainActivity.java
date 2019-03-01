package com.example.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    byte[] imgdata;
    Button sendButton;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button camButton = (Button) findViewById(R.id.btnCamera);
        imageView = (ImageView) findViewById(R.id.imageView);
        sendButton = (Button) findViewById(R.id.send_but);
        sendButton.setEnabled(false);

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection();
                connection.setImageData(imgdata);
                connection.execute();
                sendButton.setEnabled(false);
            }
        });
        

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageView.setImageBitmap(bitmap);
        sendButton.setBackgroundColor(Color.GREEN);
        sendButton.setEnabled(true);
        try {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        sendButton.setBackgroundColor(Color.GREEN);
        sendButton.setEnabled(true);
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
            imgdata = byteArrayOutputStream.toByteArray();
            String s = imgdata.length + "";
        }catch (Exception e){
            e.printStackTrace();
        }


    }

}

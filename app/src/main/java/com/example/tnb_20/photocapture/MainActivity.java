package com.example.tnb_20.photocapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.tnb_20.photocapture.R;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent takePictureIntent;
    File dir = new File("data"+File.separator+"data"+File.separator+"com.example.tnb_20.takephoto"+File.separator+"files");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = findViewById(R.id.image);
        File image = new File(dir, dir.listFiles()[dir.listFiles().length-1].getName());
        if (image.exists()){
            Uri uri = Uri.fromFile(image);
            img.setImageURI(uri);
        }

        Button b = findViewById(R.id.button);
        // Ensure that there's a camera activity to handle the intent
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv = findViewById(R.id.image);
            iv.setImageBitmap(imageBitmap);

            if(!dir.exists()){
                dir.mkdir();
            }

            OutputStream os = null;
            try {
                for (int i = 0; i<=dir.listFiles().length; i++){
                    File file = new File(dir, "photo" + i + ".png");
                    if (!file.exists()){
                        os = new FileOutputStream(file);
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        i=dir.listFiles().length;
                    }
                }
            } catch(IOException e) {
                System.out.println("ERROR al guardar la imagen");
            }
        }
    }
}

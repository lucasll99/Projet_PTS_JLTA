package com.example.testmaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testmaps.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class photo_printer extends AppCompatActivity {

    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_printer);

        //t1 = findViewById(R.id.t1);

        ///////////////Intent intent = getIntent();
        //t1.setText(intent.getStringExtra("text"));
        ///////////////String name = intent.getStringExtra("text");
        //byte[] byteImage = getIntent().getByteArrayExtra("imageByte");
        //Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        ///////////////ImageView imageView = (ImageView) findViewById(R.id.i1);
        //imageView.setImageBitmap(bitmapImage);
        //t1 = findViewById(R.id.t1);
        //t1.setText(intent.getStringExtra("text"));
        /////////////// imageView.setImageResource(getResources().getIdentifier(name, "drawable", getPackageName()));

        try {
            Intent intent = getIntent();
            t1 = findViewById(R.id.t1);
            t1.setText(intent.getStringExtra("description"));

            String image = intent.getStringExtra("id_image");

            //File file = new File(getFilesDir(), "id2");
            //Bitmap bitmapFromFile = BitmapFactory.decodeFile(file.getAbsolutePath());

            Bitmap bitmapFromFile = loadBitmapFromInternalStorage(image);

            ImageView imageView = findViewById(R.id.i1);
            imageView.setImageBitmap(bitmapFromFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Bitmap loadBitmapFromInternalStorage(String filename) {
        Bitmap bitmap = null;
        FileInputStream fis = null;

        try {
            fis = openFileInput(filename);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
}
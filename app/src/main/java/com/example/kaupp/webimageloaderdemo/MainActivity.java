package com.example.kaupp.webimageloaderdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {

    private ImageView imageView;
    private TextView textView;
    private ProgressBar progressBar;
    private final String PATH = "http://student.labranet.jamk.fi/~K2418/ttms0500-harkat/h02/img/";
    private String[] images = {"kuva1.jpg","kuva2.jpg","kuva3.jpg"};
    private int imageIndex;
    private DownloadImageTask task;
    private float x1,x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageIndex = 0;
        showImage();
    }
    public void showImage() {
        task = new DownloadImageTask();
        task.execute(PATH + images[imageIndex]);
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            Bitmap bitmap = null;
            try {
                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 < x2) {
                    imageIndex--;
                    if (imageIndex < 0) imageIndex = images.length-1;
                } else {
                    imageIndex++;
                    if (imageIndex > (images.length-1)) imageIndex = 0;
                }
                showImage();
                break;
        }
        return false;
    }
}

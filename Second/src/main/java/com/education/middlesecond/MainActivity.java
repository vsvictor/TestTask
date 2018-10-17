package com.education.middlesecond;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.education.middlesecond.Constants.URI_LINKS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST = 201;
    private long id = -1;
    private String url = null;
    private String from = null;
    private int status = 3;
    private Date date;
    private ImageView ivPcture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            from = getIntent().getExtras().getString("from");
            id = getIntent().getExtras().getLong("id", -1);
            url = getIntent().getExtras().getString("url");
            status = getIntent().getExtras().getInt("status", -1);
            date = new Date(getIntent().getExtras().getLong("date"));
            Log.i(TAG, url);
            Log.i(TAG, from);
            ivPcture = findViewById(R.id.ivPicture);
        }catch (NullPointerException ex){
            startTimer();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST);
        }else {
            start();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start();
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void startTimer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final AppCompatTextView tvClose = view.findViewById(R.id.tvClose);
        dialog.setView(view);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int allTime = 10000;
                int step = 1000;
                while (allTime > 0){
                    final String s = "This application NOT independent \n Close after "+Math.round(allTime/step)+" seconds...";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvClose.setText(s);
                        }
                    });

                    try {
                        Thread.sleep(step);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    allTime -= step;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }).start();

    }
    private void start(){
        if(null != from && !from.isEmpty()) {
            Uri uri = null;
            ContentValues values = null;
            if ("test".equals(from)) {
                values = new ContentValues();
                values.put("url",url);
                values.put("status", 3);
                values.put("date", Calendar.getInstance().getTime().getTime());
                uri = getContentResolver().insert(URI_LINKS,values);
                Log.i(TAG, "Inserted to "+uri.toString());
            }
            else if("history".equals(from)){
                values = new ContentValues();
                values.put("id", id);
                values.put("url", url);
                values.put("status", status);
                values.put("date", date.getTime());
                uri = Uri.parse("content://com.education.middle/links/"+id);
                Log.i(TAG, "Binded "+uri.toString());
            }
            download(values, uri);
        }
    }
    private void download(final ContentValues values, final Uri uri){
        Picasso.get()
                .load(values.getAsString("url"))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom fromURL) {
                values.put("status", 1);
                getContentResolver().update(uri, values, "id=?", new String[]{String.valueOf(values.getAsLong("id"))});
                ivPcture.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "Downloaded\n"+values.getAsString("url"), Toast.LENGTH_SHORT).show();
                Log.i(TAG, from+" Status:"+status);
                if("history".equals(from) && status == 1){
                    saveToFile(bitmap);
                    Log.i(TAG, "Saved to file");
                    Intent serviceIntent = new Intent(MainActivity.this, PictureService.class);
                    serviceIntent.putExtra("id",values.getAsLong("id"));
                    startService(serviceIntent);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                values.put("status", 2);
                getContentResolver().update(uri,values,"id=?",new String[]{String.valueOf(values.getAsLong("id"))});
                ivPcture.setImageResource(R.drawable.ic_launcher_foreground);
                Toast.makeText(MainActivity.this, "Download error\n"+values.getAsString("url"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void saveToFile(Bitmap bitmap){

        List<String> sPath = Arrays.asList(new String[]{"BIGDIG", "test", "B"});

        String s = Environment.getExternalStorageDirectory().getAbsolutePath();
        File sd = null;
        for(String ss: sPath){
            sd = new File(s+(File.separator+ss));
            if(!sd.exists()){
                sd.mkdir();
            }
            s+=(File.separator+ss);
        }
        Log.i(TAG, sd.getPath());
        String filename = "img_"+Calendar.getInstance().getTime().getTime()+".jpg";
        File dest = new File(sd, filename);
        try {
            String ss = dest.getCanonicalPath();
            Log.i(TAG, "File: "+ss);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

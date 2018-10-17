package com.education.middlesecond;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PictureService extends Service {
    private static final String TAG = PictureService.class.getSimpleName();
    public PictureService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try {
            final long id = intent.getExtras().getLong("id");
            Log.i(TAG, "ID:"+id);
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    String s = Constants.URI_LINKS+"/"+id;
                    getContentResolver().delete(Uri.parse(s),"id=?",new String[]{String.valueOf(id)});
                    Toast.makeText(PictureService.this,"Delete link, id="+id,Toast.LENGTH_SHORT).show();
                    stopSelf();
                }
            }, 15000);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

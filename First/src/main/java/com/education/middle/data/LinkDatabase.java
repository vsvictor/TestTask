package com.education.middle.data;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.Calendar;
import java.util.List;

@Database(entities = {Row.class}, version = 2, exportSchema = true)
public abstract class LinkDatabase extends RoomDatabase implements LifecycleObserver {
    private static final String TAG = LinkDatabase.class.getSimpleName();
    public static final String DB_NAME = "urls";

    private HandlerThread thread;
    private Handler handler;

    public LinkDatabase(){
        thread = new HandlerThread("database");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public abstract LinkDAO linkDAO();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(){
        thread.interrupt();
        handler = null;
    }



    public Handler getHandler(){
        return handler;
    }

    public void checkEmpty(){
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                List<Row> list = linkDAO().getData();
                if(list.size() == 0){
                    Row r1 = new Row("https://www.google.com.ua/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwifmKWYtIveAhVEECwKHYOHB9EQjRx6BAgBEAU&url=https%3A%2F%2Fauto.ria.com%2Fauto_ford_fusion_21610047.html&psig=AOvVaw1fbJBRLMSB3QRtdpQD7gsu&ust=1539794642424366", RowStatus.UNDEFINED, Calendar.getInstance().getTime());
                    linkDAO().insert(r1);
                    Row r2 = new Row("https://www.google.com.ua/url?sa=i&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwjG_O6VtoveAhXChSwKHeABDZ0QjRx6BAgBEAU&url=https%3A%2F%2Fauto.ria.com%2Fauto_ford_fusion_21129011.html&psig=AOvVaw1fbJBRLMSB3QRtdpQD7gsu&ust=1539794642424366", RowStatus.LOADED, Calendar.getInstance().getTime());
                    linkDAO().insert(r2);
                    Row r3 = new Row("http://???", RowStatus.ERROR, Calendar.getInstance().getTime());
                    linkDAO().insert(r3);
                }

            }
        });
    }
}

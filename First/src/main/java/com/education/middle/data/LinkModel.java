package com.education.middle.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.education.middle.App;

import java.util.List;

public class LinkModel extends AndroidViewModel {
    private LinkDatabase db;
    private LiveData<List<Row>> stream;

    public LinkModel(@NonNull Application application) {
        super(application);
        db = App.getInstance().getDatabase();
        stream = db.linkDAO().getLiveData();
    }

    public LiveData<List<Row>> getStream() {
        return stream;
    }
}

package com.education.middle.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.education.middle.Constants;

import java.util.Calendar;
import java.util.Date;

import static android.arch.persistence.room.ColumnInfo.TEXT;

@Entity(tableName = Constants.TABLE_LINKS)
public class Row {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    @TypeConverters({Convertor.class})
    private RowStatus status;
    @TypeConverters({Convertor.class})
    private Date date;

    public Row(){
        this.url = null;
        status = RowStatus.UNDEFINED;
        date = Calendar.getInstance().getTime();
    }

    public Row (String link, RowStatus status, Date date){
        this.url = link;
        this.status = status;
        this.date = date;
    }
    public Row (String link, int status, Date date){
        Convertor convertor = new Convertor();
        this.url = link;
        this.status = convertor.toRowStatus(status);
        this.date = date;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RowStatus getStatus() {
        return status;
    }

    public void setStatus(RowStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

package com.education.middle.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface LinkDAO {
    @Query("SELECT * FROM links")
    LiveData<List<Row>> getLiveData();

    @Query("SELECT * FROM links")
    List<Row> getData();

    @Query("SELECT * FROM links")
    Cursor getCursor();

    @Query("SELECT * FROM links WHERE id=:id")
    Cursor getRow(long id);

    @Insert
    long insert(Row row);
    @Insert
    long[] insertAll(Row[] rows);

    @Delete
    int delete(Row row);
    @Query("DELETE FROM  links WHERE id = :id")
    int deleteById(long id);

    @Update
    int update(Row row);
}

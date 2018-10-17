package com.education.middle.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.education.middle.App;
import com.education.middle.Constants;
import com.education.middle.data.LinkDAO;
import com.education.middle.data.LinkDatabase;
import com.education.middle.data.Row;

import java.util.ArrayList;
import java.util.Date;

import static com.education.middle.data.LinkDatabase.DB_NAME;

public class URLContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.education.middle";
    public static final Uri URI_LINKS = Uri.parse("content://" + AUTHORITY + "/" + Constants.TABLE_LINKS);
    private static final int CODE_LINKS_DIR = 1;
    private static final int CODE_LINKS_ITEM = 2;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(AUTHORITY, Constants.TABLE_LINKS, CODE_LINKS_DIR);
        MATCHER.addURI(AUTHORITY, Constants.TABLE_LINKS + "/*", CODE_LINKS_ITEM);
    }
    public URLContentProvider() {
    }
    @Override
    public boolean onCreate() {
        return true;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + Constants.TABLE_LINKS;
            case CODE_LINKS_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + Constants.TABLE_LINKS;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_LINKS_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = App.getInstance().getDatabase().linkDAO().deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = App.getInstance().getDatabase().linkDAO().insert(fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_LINKS_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_LINKS_DIR || code == CODE_LINKS_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            LinkDAO link = App.getInstance().getDatabase().linkDAO();
            final Cursor cursor;
            if (code == CODE_LINKS_DIR) {
                cursor = link.getCursor();
            } else {
                cursor = link.getRow(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_LINKS_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Row row = fromContentValues(values);
                row.setId(ContentUris.parseId(uri));
                final int count = App.getInstance().getDatabase().linkDAO().update(row);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final LinkDatabase database = App.getInstance().getDatabase();
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_LINKS_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final LinkDatabase database = App.getInstance().getDatabase();
                final Row[] rows = new Row[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    rows[i] = fromContentValues(valuesArray[i]);
                }
                return database.linkDAO().insertAll(rows).length;
            case CODE_LINKS_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    private Row fromContentValues(ContentValues values){
        String url = values.getAsString("url");
        int status = values.getAsInteger("status");
        Date date = new Date(values.getAsLong("date"));
        return new Row(url, status, date );
    }
}

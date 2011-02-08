package net.tapi.handynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {
    
	private final Context context;
	
    public static final String KEY_ROWID = "_id";
    public static final String KEY_BODY = "body";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_CREATE =
        "create table notes (_id integer primary key, "
        + "body text not null);";
    
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 1;
	
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }
        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
    
    public NotesDbAdapter(Context c) {
        this.context = c;
    }
    
    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    
    public long createNote(int wId, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROWID, wId);
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }
    
    public boolean deleteNote(int wId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + wId, null) > 0;
    }
    
    public String showNoteText(int wId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_BODY}, KEY_ROWID + "=" + wId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        String result = "";
        
        if (mCursor.getCount() > 0) {
        	result = mCursor.getString(mCursor.getColumnIndex(KEY_BODY));
        }
        
        mCursor.close();
        return result;
        	
    }
    
    public boolean updateNote(int wId, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + wId, null) > 0;
    }
}

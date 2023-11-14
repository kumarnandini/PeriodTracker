package com.example.afinal;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PeriodRecords.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Column names
    public static final String TABLE_NAME = "PeriodRecords";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_MOODS = "moods";

    // SQL query to create the table
    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_START_DATE + " TEXT, " +
                    COLUMN_END_DATE + " TEXT, " +
                    COLUMN_MOODS + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
        Log.d("DatabaseHelper", "Table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrades if needed
    }
}

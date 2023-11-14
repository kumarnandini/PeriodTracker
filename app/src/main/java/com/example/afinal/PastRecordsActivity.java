package com.example.afinal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PastRecordsActivity extends AppCompatActivity {

    private ListView listViewRecords;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_records);

        listViewRecords = findViewById(R.id.listViewRecords);
        dbHelper = new DatabaseHelper(this);

        // Retrieve records from the database
        List<String> records = getRecords();

        // Display records in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        listViewRecords.setAdapter(adapter);
    }

    private List<String> getRecords() {
        List<String> recordsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define columns to retrieve
        String[] projection = {
                DatabaseHelper.COLUMN_START_DATE,
                DatabaseHelper.COLUMN_END_DATE,
                DatabaseHelper.COLUMN_MOODS
        };

        // Query the database
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Extract data from the cursor
        while (cursor.moveToNext()) {
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_DATE));
            String moods = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOODS));

            // Format the record and add it to the list
            String record = "Start Date: " + startDate + "\nEnd Date: " + endDate + "\nMoods: " + moods + "\n";
            recordsList.add(record);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return recordsList;
    }
}

package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText editTextStartDate;
    private EditText editTextEndDate;
    private Button btnCalculate;
    private TextView textViewResult;
    private TextView textViewMyPeriod;
    private TextView textViewPredictedPeriod;
    private Button btnSendEmail, btnSubmitMoods, btnViewPastRecords;
    private CheckBox checkBoxHappy, checkBoxSad, checkBoxAngry, checkBoxCranky, checkBoxConfident,
            checkBoxInspired;

    private List<String> selectedMoods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        btnCalculate = findViewById(R.id.btnCalculate);
        textViewResult = findViewById(R.id.textViewResult);
        textViewMyPeriod = findViewById(R.id.textViewMyPeriod);
        textViewPredictedPeriod = findViewById(R.id.textViewPredictedPeriod);
        btnSendEmail = findViewById(R.id.btnSendEmail);

        checkBoxHappy = findViewById(R.id.checkBoxHappy);
        checkBoxSad = findViewById(R.id.checkBoxSad);
        checkBoxAngry = findViewById(R.id.checkBoxAngry);
        checkBoxCranky = findViewById(R.id.checkBoxCranky);
        checkBoxConfident = findViewById(R.id.checkBoxConfident);
        checkBoxInspired = findViewById(R.id.checkBoxInspired);
        btnSubmitMoods = findViewById(R.id.btnSubmitMoods);


        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculatePeriodDuration();
                predictNextPeriod();
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        btnSubmitMoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMoods();
            }
        });

        btnViewPastRecords = findViewById(R.id.btnViewPastRecords);

        btnViewPastRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPastRecordsActivity();
            }
        });
    }

    private void calculatePeriodDuration() {
        String startDateStr = editTextStartDate.getText().toString();
        String endDateStr = editTextEndDate.getText().toString();

        long periodDuration = getDaysDifference(startDateStr, endDateStr);

        textViewResult.setText("Period Duration: " + periodDuration + " days");
    }

    private void predictNextPeriod() {
        String endDateStr = editTextEndDate.getText().toString();

        String predictedNextPeriod = calculateNextPeriod(endDateStr);

        textViewMyPeriod.setVisibility(View.VISIBLE);
        textViewPredictedPeriod.setText("Predicted Next Period: " + predictedNextPeriod);
    }

    private long getDaysDifference(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String calculateNextPeriod(String lastEndDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            Date endDate = sdf.parse(lastEndDate);
            calendar.setTime(endDate);

            calendar.add(Calendar.DAY_OF_MONTH, 28);

            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lastEndDate;
    }

    private void sendEmail() {
        String predictedNextPeriod = textViewPredictedPeriod.getText().toString();

        String emailSubject = "Next Period Information";
        String emailBody = "The predicted next period date is: " + predictedNextPeriod;

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }

    private void submitMoods() {
        // Clear the previous selection
        selectedMoods.clear();

        if (checkBoxHappy.isChecked()) {
            selectedMoods.add("Happy");
        }
        if (checkBoxSad.isChecked()) {
            selectedMoods.add("Sad");
        }
        if (checkBoxAngry.isChecked()) {
            selectedMoods.add("Angry");
        }
        if (checkBoxCranky.isChecked()) {
            selectedMoods.add("Cranky");
        }
        if (checkBoxConfident.isChecked()) {
            selectedMoods.add("Confident");
        }
        if (checkBoxInspired.isChecked()) {
            selectedMoods.add("Inspired");
        }

        // Insert data into the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_START_DATE, editTextStartDate.getText().toString());
        values.put(DatabaseHelper.COLUMN_END_DATE, editTextEndDate.getText().toString());
        values.put(DatabaseHelper.COLUMN_MOODS, TextUtils.join(",", selectedMoods));

        db.insert(DatabaseHelper.TABLE_NAME, null, values);

        // Close the database connections
        dbHelper.close();
    }

    private void openPastRecordsActivity() {
        Intent intent = new Intent(this, PastRecordsActivity.class);
        startActivity(intent);
    }
}

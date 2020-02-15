//Nikolaos Katsiopis
//icsd13076

package com.buftas.patternanalyzer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    private Button submitUserButton, colorPatternButton, pianoPatternButton, creditsButton, generalStatisticsButton, behaviouralStatisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitUserButton = findViewById(R.id.submit);
        submitUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                addUserToDatabase();
            }
        });

        colorPatternButton = findViewById(R.id.colorPatternButton);
        colorPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToColorPatternView();
            }
        });

        pianoPatternButton = findViewById(R.id.pianoPatternButton);
        pianoPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPianoPatternView();
            }
        });

        creditsButton = findViewById(R.id.creditsButton);
        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Credits", "Creator: Nick Katsiopis\nDepartment: ICSD\nInstitution: University of Aegean");
            }
        });

        generalStatisticsButton = findViewById(R.id.generalStatisticsButton);
        generalStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStatisticsView();
            }
        });

        behaviouralStatisticsButton = findViewById(R.id.behaviouralStatisticsButton);
        behaviouralStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage("Info!", "This type is statistics are Unavailable at the moment!");
            }
        });
    }

    //-----------------------------------------------------------------------------------------VIEW TRIGGER METHODS---------------------------------------------------------------------------------------------------

    //This method opens ColorPatternView
    public void goToColorPatternView() {
        Intent intent = new Intent(this, ColorPatternActivity.class);
        startActivity(intent);
    }

    //This method open PianoPatternView
    public void goToPianoPatternView() {
        Intent intent = new Intent(this, PianoPatternActivity.class);
        startActivity(intent);
    }

    //This method open PianoPatternView
    public void goToStatisticsView() {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    //-------------------------------------------------------------------------------------------DIALOG METHODS--------------------------------------------------------------------------------------------------------

    //This method provides an information dialog to the user
    public void showMessage(String title, String message) {
        InfoDialog credits = new InfoDialog();
        credits.setTitle(title);
        credits.setMessage(message);
        credits.show(getSupportFragmentManager(), title);
    }


    //------------------------------------------------------------------------------------------FUNCTIONAL METHODS-----------------------------------------------------------------------------------------------------

    //Method that contacts the Database and adds a new user entry, after checking if it already exists or has submitted already the patterns
    public void addUserToDatabase() {
        EditText username_field = findViewById(R.id.usernameField);
        String username = username_field.getText().toString().trim();
        username_field.setText("");
        if (username.equals("")) {
            showMessage("Error!", "Field cant be blank!");
            return;
        }
        DatabaseConnectionHelper db_connection = new DatabaseConnectionHelper(MainActivity.this);
        SQLiteDatabase database = db_connection.getWritableDatabase();
        if (userExists(username, database)) {
            Toast.makeText(MainActivity.this, "Username already exists...", Toast.LENGTH_SHORT).show();
            Log.d("Database Operations", "User Already Exists!...");
            return;
        }
        long result = db_connection.addUser(username, database);
        if (result != -1) {
            Toast.makeText(MainActivity.this, "Username Added Successfully...", Toast.LENGTH_SHORT).show();
            Log.d("Database Operations", "1 ROW Added...");
            db_connection.close();
        } else {
            Toast.makeText(MainActivity.this, "Something Went Wrong...", Toast.LENGTH_LONG).show();
            Log.d("Database Operations", "You f***d up bruh!...");
            db_connection.close();
        }
    }

    //This method check if a given username already exists on the Database
    private boolean userExists(String username, SQLiteDatabase database) {
        Cursor c = database.query(DatabaseSchema.UsernameEntry.TABLE_NAME,
                new String[]{DatabaseSchema.UsernameEntry.USERNAME_ID, DatabaseSchema.UsernameEntry.USERNAME}, DatabaseSchema.UsernameEntry.USERNAME + " = ?",
                new String[]{username}, null, null, null);
        boolean result = c.getCount() > 0;
        c.close();
        return result;
    }
}


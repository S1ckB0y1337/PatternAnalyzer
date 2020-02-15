//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
//Java imports
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;


public class ColorPatternActivity extends AppCompatActivity {

    private TextView attemptCounter, progressCounter;
    private EditText username_field;
    private String currentUsernameInUse = "";
    private Button[][] colorButtonArray;
    private Button start, confirm, cancel, upload;
    private int patternStep = 0, buttonCounter = 1, attemptNo = 1;
    private int[] pattern = new int[4];
    private long[] holdDurations = new long[4];
    private long lastDown, lastUp;
    private int x, y, centerX, centerY;
    private int[][] defaultValues;
    private UserData userData;
    private ArrayList<RawPatternEntry> rawPatternEntries;
    private ArrayList<String> allPatterns;
    private final String users = "UserData/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collor_pattern);
        attemptCounter = findViewById(R.id.AttempCounter);
        username_field = findViewById(R.id.collorPatternUsername);
        progressCounter = findViewById(R.id.progressCounter);
        initializeButtons();
        lockOrUnlockPattern("lock");
        cancel.setEnabled(false);
        confirm.setEnabled(false);
        //Request storage permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    //This method initializes the main pattern buttons and their listeners
    @SuppressLint("ClickableViewAccessibility")
    private void initializeButtons() {
        //Initialize Start and Confirm Button
        start = findViewById(R.id.colorPatternStartButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(ColorPatternActivity.this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (confirmUser()) {
                    start.setEnabled(false);
                    attemptCounter.setText("Attempt:1");
                }
            }
        });
        //Linking and adding listener on confirm button
        confirm = findViewById(R.id.colorPatternConfirmButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If the attempt number is in the range
                if (patternStep < 4) {
                    showMessage("Error!", "Please add a 4 entry color pattern before we proceed!");
                } else {
                    //If we are in the capture phase, we check if a given pattern has already been submitted
                    if (attemptNo <= 8 && allPatterns.contains(Arrays.toString(pattern))) {
                        showMessage("Error!", "The pattern you entered already has been submitted!\nTry a different one");
                        return;
                    }
                    if (attemptNo == 8) {
                        showMessage("Info!", "Now try to remember 3 of the previous entered patterns!");
                    }
                    if (attemptNo > 8) {
                        if (!confirmExistingPattern(Arrays.toString(pattern))) {
                            return;
                        }
                    }
                    //Adding the RawPatternEntry objects to the "Profile object" of the user, userData
                    for (RawPatternEntry entry : rawPatternEntries) {
                        userData.addRawPattern(entry);
                    }
                    //Add the current pattern on a list for later comparison
                    allPatterns.add(Arrays.toString(pattern));
                    //Create and add a PatternMetadata Object to the UserData Object
                    //Calculate the Time_To_Complete value
                    long timeToComplete = rawPatternEntries.get(3).getTimestamp() - rawPatternEntries.get(0).getTimestamp();
                    //Now i calculate the Pattern Length in pixels by calculating and summing the distance between each of the points an even occurred
                    double patternLength = calcEuclideanDistance(rawPatternEntries.get(0).getFirstXpoint(), rawPatternEntries.get(0).getFirstYpoint(), rawPatternEntries.get(1).getFirstXpoint(), rawPatternEntries.get(1).getFirstYpoint()) +
                            calcEuclideanDistance(rawPatternEntries.get(1).getFirstXpoint(), rawPatternEntries.get(1).getFirstYpoint(), rawPatternEntries.get(2).getFirstXpoint(), rawPatternEntries.get(2).getFirstYpoint()) +
                            calcEuclideanDistance(rawPatternEntries.get(2).getFirstXpoint(), rawPatternEntries.get(2).getFirstYpoint(), rawPatternEntries.get(3).getFirstXpoint(), rawPatternEntries.get(3).getFirstYpoint());
                    //Calculate the average speed by dividing patternLength and timeToComplete
                    double avg_speed = patternLength / (double) timeToComplete;
                    //Calculate and save Lowest and Highest finger pressure
                    ArrayList<Float> pressures = new ArrayList<>();
                    int[] tempPattern = new int[4];
                    int c = 0;
                    for (RawPatternEntry entry : rawPatternEntries) {
                        tempPattern[c] = entry.getNoOfPoint();
                        pressures.add(entry.getPressure());
                        c++;
                    }
                    float maxPressure = Collections.max(pressures);
                    float minPressure = Collections.min(pressures);
                    PatternMetadata metadata = new PatternMetadata(userData.getUsername(), attemptNo, tempPattern, 4, timeToComplete, patternLength, avg_speed, maxPressure, minPressure, 1, 1);
                    userData.addPatternMeta(metadata);
                    //Create and add a PatternTouch object in th userData object
                    Log.d("Debugging of touch events", Arrays.toString(holdDurations));
                    PatternTouch pressureData = new PatternTouch(userData.getUsername(), attemptNo, tempPattern, holdDurations[0], holdDurations[1], holdDurations[2], holdDurations[3]);
                    userData.addPatternTouch(pressureData);
                    //Create and add a PairedMetadata Object to the UserData Object
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = size.y;
                    String screenSize = width + "x" + height;
                    //Calculate the Euclidean distance between A point and the last B point
                    double Distance_AB = calcEuclideanDistance(rawPatternEntries.get(0).getFirstXpoint(),
                            rawPatternEntries.get(0).getFirstYpoint(), rawPatternEntries.get(1).getLastXpoint(), rawPatternEntries.get(1).getLastYpoint());
                    //Now calculate the time the user took to go from point A to point B by subtracting the timestamps
                    long Intertime_AB = rawPatternEntries.get(1).getTimestamp() - rawPatternEntries.get(0).getTimestamp();
                    //Calculate the Average speed of the users finger while going from point A to point B
                    double Avg_speedAB = Distance_AB / (double) Intertime_AB;
                    //Finally calculate the average pressure applied on point A and B
                    float Avg_pressure = (rawPatternEntries.get(0).getPressure() + rawPatternEntries.get(1).getPressure()) / 2;
                    PairedMetadata pairedData1_2 = new PairedMetadata(userData.getUsername(), attemptNo, screenSize, tempPattern[0], tempPattern[1], rawPatternEntries.get(0).getCenterXpoint(), rawPatternEntries.get(0).getCenterYpoint(),
                            rawPatternEntries.get(1).getCenterXpoint(), rawPatternEntries.get(1).getCenterYpoint(), rawPatternEntries.get(0).getFirstXpoint(),
                            rawPatternEntries.get(0).getFirstYpoint(), rawPatternEntries.get(1).getLastXpoint(), rawPatternEntries.get(1).getLastYpoint(), Distance_AB, Intertime_AB, Avg_speedAB, Avg_pressure);
                    //Doing the same for point 2 and 3
                    Distance_AB = calcEuclideanDistance(rawPatternEntries.get(1).getFirstXpoint(),
                            rawPatternEntries.get(1).getFirstYpoint(), rawPatternEntries.get(2).getLastXpoint(), rawPatternEntries.get(2).getLastYpoint());
                    Intertime_AB = rawPatternEntries.get(2).getTimestamp() - rawPatternEntries.get(1).getTimestamp();
                    Avg_speedAB = Distance_AB / (double) Intertime_AB;
                    Avg_pressure = (rawPatternEntries.get(1).getPressure() + rawPatternEntries.get(2).getPressure()) / 2;
                    PairedMetadata pairedData2_3 = new PairedMetadata(userData.getUsername(), attemptNo, screenSize, tempPattern[1], tempPattern[2], rawPatternEntries.get(1).getCenterXpoint(), rawPatternEntries.get(1).getCenterYpoint(),
                            rawPatternEntries.get(2).getCenterXpoint(), rawPatternEntries.get(2).getCenterYpoint(), rawPatternEntries.get(1).getFirstXpoint(),
                            rawPatternEntries.get(1).getFirstYpoint(), rawPatternEntries.get(2).getLastXpoint(), rawPatternEntries.get(2).getLastYpoint(), Distance_AB, Intertime_AB, Avg_speedAB, Avg_pressure);
                    //And for 3 and 4
                    Distance_AB = calcEuclideanDistance(rawPatternEntries.get(2).getFirstXpoint(),
                            rawPatternEntries.get(2).getFirstYpoint(), rawPatternEntries.get(3).getLastXpoint(), rawPatternEntries.get(3).getLastYpoint());
                    Intertime_AB = rawPatternEntries.get(3).getTimestamp() - rawPatternEntries.get(2).getTimestamp();
                    Avg_speedAB = Distance_AB / (double) Intertime_AB;
                    Avg_pressure = (rawPatternEntries.get(2).getPressure() + rawPatternEntries.get(3).getPressure()) / 2;
                    PairedMetadata pairedData3_4 = new PairedMetadata(userData.getUsername(), attemptNo, screenSize, tempPattern[2], tempPattern[3], rawPatternEntries.get(2).getCenterXpoint(), rawPatternEntries.get(2).getCenterYpoint(),
                            rawPatternEntries.get(3).getCenterXpoint(), rawPatternEntries.get(3).getCenterYpoint(), rawPatternEntries.get(2).getFirstXpoint(),
                            rawPatternEntries.get(2).getFirstYpoint(), rawPatternEntries.get(3).getLastXpoint(), rawPatternEntries.get(3).getLastYpoint(), Distance_AB, Intertime_AB, Avg_speedAB, Avg_pressure);
                    //Adding the entries on the UserData Object for later use
                    userData.addPairedPattern(pairedData1_2);
                    userData.addPairedPattern(pairedData2_3);
                    userData.addPairedPattern(pairedData3_4);
                    if (attemptNo == 11) {
                        //Reset username
                        currentUsernameInUse = "";
                        //Re-enable the "log in" fields
                        username_field.setEnabled(true);
                        start.setEnabled(true);
                        lockOrUnlockPattern("lock");
                        cancel.setEnabled(false);
                        confirm.setEnabled(false);
                        //Generate the metadata and save them into .csv files
                        generateAndExportData();
                        showMessage("Success!!", "You data has been submitted!\n Thank you!");
                        //Upload generated files to the server
                        uploadDataToServer();
                        showMessage("Success!!", "Data successfully uploaded to server!");
                        //Update DB
                        setUserDoneWithColorPattern();
                        //Reset the recording counters etc
                        resetDataRecordingProcess();
                        attemptNo = 1;
                        allPatterns.clear();
                        attemptCounter.setText("Attempt:");
                    } else {
                        //Resetting patternStep
                        resetDataRecordingProcess();
                        incrementAttemptCounter();
                    }
                }
            }
        });
        //Linking and adding listener on cancel button
        cancel = findViewById(R.id.colorPatternCancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDataRecordingProcess();
            }
        });

        //Create a default value array for the color pattern buttons
        defaultValues = new int[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                defaultValues[i][j] = buttonCounter;
                buttonCounter++;
            }
        }

        buttonCounter = 1;
        //Initialize Color Pattern Buttons
        colorButtonArray = new Button[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "colorbutton" + buttonCounter;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                colorButtonArray[i][j] = findViewById(resID);
                final Button tempButton = findViewById(resID);
                final int defaultValue = defaultValues[i][j];
                final RawPatternEntry tempEntry = new RawPatternEntry();
                colorButtonArray[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                //Reduce the button's size to create the click illusion
                                float reducedvalue = (float) 0.9;
                                v.setScaleX(reducedvalue);
                                v.setScaleY(reducedvalue);
                                if (patternStep == 4) {
                                    showMessage("Error!", "Pattern can have max 4 entries!");
                                    return true;
                                } else {
                                    //Get the current even time of the button getting pressed
                                    lastDown = event.getEventTime();
                                    //Get the number of the pressed button
                                    int noOfPoint = defaultValue;
                                    //Get the timestamp of the event
                                    long timestamp = SystemClock.elapsedRealtimeNanos();
                                    //Get the number of the pressed button again and add it on an array to have the full pattern saved
                                    pattern[patternStep] = defaultValue;
                                    //Adding noOfPoint and timestamp into the RawUserEntry object
                                    tempEntry.setNoOfPoint(noOfPoint);
                                    tempEntry.setTimestamp(timestamp);
                                    //Get the x and y points on the screen of the button pressed
                                    x = (int) event.getRawX();
                                    y = (int) event.getRawY();
                                    //Calculate the center coords of the button to use them later on the Paired Metadata files
                                    int buttonWidth = tempButton.getWidth();
                                    int buttonHeight = tempButton.getHeight();
                                    int[] buttonCoords = new int[2];
                                    tempButton.getLocationOnScreen(buttonCoords);
                                    centerX = (buttonCoords[0] + buttonCoords[0] + buttonWidth) / 2;
                                    centerY = (buttonCoords[1] + buttonCoords[1] + buttonHeight) / 2;
                                    //Get the pressure the user applied
                                    //Adding the rest of the metrics into the object
                                    tempEntry.setFirstXpoint(x);
                                    tempEntry.setFirstYpoint(y);
                                    tempEntry.setCenterXpoint(centerX);
                                    tempEntry.setCenterYpoint(centerY);
                                    tempEntry.setPressure(event.getPressure());
                                    Log.d("Debugging of touch events", tempEntry.toString());
                                    return true;
                                }
                            case MotionEvent.ACTION_UP:
                                //Resize the button again to its original size
                                v.setScaleX(1);
                                v.setScaleY(1);
                                if (patternStep < 4) {
                                    lastUp = event.getEventTime();
                                    holdDurations[patternStep] = lastUp - lastDown;
                                    x = (int) event.getRawX();
                                    y = (int) event.getRawY();
                                    tempEntry.setLastXpoint(x);
                                    tempEntry.setLastYpoint(y);
                                    //Adding the temporary entry object into a list
                                    rawPatternEntries.add(tempEntry);
                                    //Add 1 into the patternStep attemptCounter
                                    patternStep++;
                                    updatePatternProgress();
                                    return true;
                                }
                            default:
                                return false;
                        }
                    }
                });
                buttonCounter++;
            }
        }
    }

    //This method provides an information dialog to the user
    public void showMessage(String title, String message) {
        InfoDialog dialog = new InfoDialog();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show(getSupportFragmentManager(), title);
    }

    //Method that sets the variables for the user to start the pattern capture process, after confirming its existence on the DataBase
    private boolean confirmUser() {
        String username = username_field.getText().toString().trim();
        username_field.setText("");
        if (username.equals("")) {
            showMessage("Error!", "Field cant be blank!");
            return false;
        }
        DatabaseConnectionHelper db_connection = new DatabaseConnectionHelper(ColorPatternActivity.this);
        SQLiteDatabase database = db_connection.getReadableDatabase();
        if (!userExists(username, database)) {
            showMessage("Error", "User doesn't exist!");
            return false;
        } else if (userExists(username, database) && !isUserDoneWithColorPattern(username, database)) {
            this.currentUsernameInUse = username;
            username_field.setEnabled(false);
            start.setEnabled(false);
            userData = new UserData(this.currentUsernameInUse);
            rawPatternEntries = new ArrayList<>();
            allPatterns = new ArrayList<>();
            lockOrUnlockPattern("unlock");
            cancel.setEnabled(true);
            confirm.setEnabled(true);
            showMessage("Info!", "Please enter 8 different patterns!");
            return true;
        } else {
            showMessage("Info", "Data for this user has been already provided!!");
            return false;
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

    //This method checks if a given user has completed the pattern receiving process and has provided the proper data
    private boolean isUserDoneWithColorPattern(String username, SQLiteDatabase database) {
        Cursor c = database.query(DatabaseSchema.UsernameEntry.TABLE_NAME, new String[]{DatabaseSchema.UsernameEntry.COLOR_PATTERN_SUBMISSION},
                DatabaseSchema.UsernameEntry.USERNAME + " = ?", new String[]{username}, null, null, null);
        c.moveToFirst();
        boolean result = c.getInt(c.getColumnIndex(DatabaseSchema.UsernameEntry.COLOR_PATTERN_SUBMISSION)) == 1;
        c.close();
        return result;
    }

    //This method updates a users status for the color pattern
    private void setUserDoneWithColorPattern() {
        DatabaseConnectionHelper db_connection = new DatabaseConnectionHelper(ColorPatternActivity.this);
        SQLiteDatabase db = db_connection.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.UsernameEntry.COLOR_PATTERN_SUBMISSION, 1);
        db.update(DatabaseSchema.UsernameEntry.TABLE_NAME, values, DatabaseSchema.UsernameEntry.USERNAME + " = ?", new String[]{userData.getUsername()});
        db_connection.close();
    }

    //This Method updates the attempt counter of the current pattern submission
    private void incrementAttemptCounter() {
        attemptNo++;
        attemptCounter.setText(String.format(Locale.getDefault(), "Attempt:%d", attemptNo));
    }

    //This method updates the current progress of the pattern entries to inform the user of how many pattern entries has left
    private void updatePatternProgress() {
        progressCounter.setText(String.format(Locale.getDefault(), "Entry:%d", patternStep));
    }

    //This method resets the basic counters and Lists for new data collection
    private void resetDataRecordingProcess() {
        //Putting step back to 0
        patternStep = 0;
        updatePatternProgress();
        //Zeroing out the pattern and hold durations, just to be debugging friendly
        for (int x : pattern) {
            x = 0;
        }
        for (long y : holdDurations) {
            y = 0;
        }
        //Deleting all entries of the RawPattern list
        rawPatternEntries.clear();
    }

    //This method locks or unlocks the pattern buttons
    private void lockOrUnlockPattern(String action) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                switch (action) {
                    case "lock":
                        colorButtonArray[i][j].setEnabled(false);
                        break;
                    case "unlock":
                        colorButtonArray[i][j].setEnabled(true);
                        break;
                    default:
                        showMessage("Error!", "An unexpected error occurred!");
                        break;
                }
            }
        }
    }

    //This method will check if a given pattern is contained in a list of previous given patterns and if the pattern doesn't match we inform the user and reset the process
    private boolean confirmExistingPattern(String pattern) {
        if (!allPatterns.contains(pattern)) {
            showMessage("Error!", "Pattern Recording Process Failed!\nPlease Try Again!");
            resetDataRecordingProcess();
            attemptNo = 1;
            allPatterns.clear();
            userData = new UserData(currentUsernameInUse);
            attemptCounter.setText(String.format(Locale.getDefault(), "Attempt: %d", attemptNo));
            return false;
        } else {
            return true;
        }
    }

    //This method uses the Euclidean distance formula -> distance = sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)) to calculate the distance of two points
    private double calcEuclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    //This method generates and saves data on csv files for later  analysis
    private void generateAndExportData() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("List size Debug", String.format("Raw Pattern List Size: %d, Metadata List size: %d", userData.getRawPatterns().size(), userData.getPatternMeta().size()));
                    //We create a new thread for the generation of the files, so our GUI is still usable
                    //We get hold of the application's folder on internal storage
                    File appFolder = getApplicationContext().getFilesDir();
                    final String userFolder = users + userData.getUsername() + "/colorPatternData/";
                    File currentUser = new File(appFolder, userFolder);
                    //Check the result of the folder creation!
                    if (!currentUser.mkdirs())
                        showMessage("Error!", "App's folder creation failed");
                    //-------------------------------------------------------------Create and export the RawPattern data files----------------------------------------------------------------------
                    int counter = 1;
                    for (int i = 0; i < userData.getRawPatterns().size(); i += 4) {
                        //Create the file name
                        String rawFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_raw";
                        //Create an object to handle the file creation
                        CSVFileHandler fileHandler = new CSVFileHandler(rawFileNameTemplate, currentUser.getPath());
                        //Write the header of the file
                        fileHandler.writeLine("number_of_activated_point;xpoint;ypoint;timestamp;pressure");
                        //Loop and write 4lines for every event for each pattern file
                        for (int j = i; j < i + 4; j++) {
                            RawPatternEntry entry = userData.getRawPatterns().get(j);
                            fileHandler.writeLine(entry.getNoOfPoint() + ";" + entry.getFirstXpoint() + ";" + entry.getFirstYpoint() + ";" + entry.getTimestamp() + ";" + entry.getPressure());
                        }
                        //Close the File Stream
                        fileHandler.close();
                        counter++;
                    }
                    //-------------------------------------------------------------Create and export the Pattern Metadata files---------------------------------------------------------------------
                    String metaFileNameTemplate = "color_" + userData.getUsername() + "_metadata";
                    ArrayList<PatternMetadata> metaEntries = userData.getPatternMeta();
                    CSVFileHandler fileHandler = new CSVFileHandler(metaFileNameTemplate, currentUser.getPath());
                    fileHandler.writeLine("Username;Attempt_number;Sequence;Seq_length;Time_to_complete;PatternLength;Avg_speed;Highest_pressure;Lowest_pressure;HandNum;FingerNum");
                    for (int z = 0; z < metaEntries.size(); z++) {
                        PatternMetadata entry = metaEntries.get(z);
                        fileHandler.writeLine(entry.getUsername() + ";" + entry.getAttemptNumber() + ";" +
                                //In the Sequence entry i remove the [ and ] characters, plus trim the spaces to be more readable in the string format
                                Arrays.toString(entry.getSequence()).replace("[", "\"").replace("]", "\"").trim() +
                                ";" + entry.getSeqLength() + ";" + entry.getTimeToComplete() + ";" + entry.getPatternLength() + ";" + entry.getAvgSpeed() + ";" + entry.getHighestPressure() + ";" +
                                entry.getLowestPressure() + ";" + entry.getHandNum() + ";" + entry.getFingerNum());
                    }
                    fileHandler.close();
                    //------------------------------------------------------------Create and export the Deep/Swallow touch files-------------------------------------------------------------------
                    counter = 1;
                    ArrayList<PatternTouch> touchData = userData.getTouchMetrics();
                    for (int i = 0; i < touchData.size(); i++) {
                        PatternTouch entry = touchData.get(i);
                        String touchFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_touch";
                        fileHandler = new CSVFileHandler(touchFileNameTemplate, currentUser.getPath());
                        fileHandler.writeLine("Username;Attempt_number;Sequence;hold_duration1;hold_duration2;hold_duration3;hold_duration4");
                        fileHandler.writeLine(entry.getUsername() + ";" + entry.getAttemptNumber() + ";" +
                                Arrays.toString(entry.getSequence()).replace("[", "\"").replace("]", "\"").trim() + ";" +
                                entry.getHold_duration1() + ";" + entry.getHold_duration2() + ";" + entry.getHold_duration3() + ";" + entry.getHold_duration4());
                        fileHandler.close();
                        counter++;
                    }
                    //-------------------------------------------------------------Create and export the Pair metadata files--------------------------------------------------------------------
                    counter = 1;
                    ArrayList<PairedMetadata> pairedData = userData.getPairedMeta();
                    for (int i = 0; i < userData.getPairedMeta().size(); i += 3) {
                        String pairFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_pairs";
                        fileHandler = new CSVFileHandler(pairFileNameTemplate, currentUser.getPath());
                        fileHandler.writeLine("Username;Attempt_number;Screen_resolution;Pattern_number_A;Pattern_number_B;Xcoord_of_central_Point_of_A;Ycoord_of_central_Point_of_A;" +
                                "Xcoord_of_central_Point_of_B;Ycoord_of_central_Point_of_B;First_Xcoord_of_A;First_Ycoord_of_A;Last_ Xcoord_of_B;Last_Ycoord_of_B;Distance_AB;Intertime_AB;" +
                                "Avg_speeadAB;Avg_pressure");
                        for (int j = i; j < i + 3; j++) {
                            PairedMetadata entry = pairedData.get(j);
                            fileHandler.writeLine(entry.getUsername() + ";" + entry.getAttemptNumber() + ";" + entry.getScreen_resolution() + ";" + entry.getPattern_number_A() + ";" +
                                    entry.getPattern_number_B() + ";" + entry.getXcoord_of_central_Point_of_A() + ";" + entry.getYcoord_of_central_Point_of_A() + ";" + entry.getXcoord_of_central_Point_of_B() + ";" +
                                    entry.getYcoord_of_central_Point_of_B() + ";" + entry.getFirst_Xcoord_of_A() + ";" + entry.getFirst_Ycoord_of_A() + ";" + entry.getLast_Xcoord_of_B() + ";" +
                                    entry.getLast_Ycoord_of_B() + ";" + entry.getDistance_AB() + ";" + entry.getIntertime_AB() + ";" + entry.getAvg_speeadAB() + ";" + entry.getAvg_pressure());
                        }
                        counter++;
                        fileHandler.close();
                    }
                } catch (Exception e) {
                    showMessage("Error!", "An unexpected Error occurred!");
                    Log.d("IO Debugging", e.toString());
                }
            }
        };
        thread.start();
    }

    //This method calls the uploadFileToServer() method and fills the correct details so every generated .csv file of the current user gets upload to the server
    private void uploadDataToServer() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Path filePath;
                    //Here i have the default WebService ID's on a string array
                    final String[] webServiceRequests = {"ColorRawPatternUpload?", "ColorPatternMetadataUpload?", "ColorTouchDataUpload?", "ColorPairedMetadataUpload?"};
                    //We get the user's folder path in string format
                    final String userFolder = users + userData.getUsername() + "/colorPatternData/";
                    //We get the application's internal storage path
                    File appFolder = getApplicationContext().getFilesDir();
                    //And finally i have the full path to the folder of the user
                    File currentUser = new File(appFolder, userFolder);
                    int counter = 1;
                    //Upload the Raw Pattern Files
                    for (int i = 0; i < 11; i++) {
                        String rawFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_raw.csv";
                        filePath = Paths.get(currentUser.getAbsolutePath(), rawFileNameTemplate);
                        uploadFileToServer(filePath, webServiceRequests[0]);
                        counter++;
                        sleep(1000);
                    }
                    //Upload the Pattern Metadata file
                    String metaFileNameTemplate = "color_" + userData.getUsername() + "_metadata.csv";
                    filePath = Paths.get(currentUser.getAbsolutePath(), metaFileNameTemplate);
                    uploadFileToServer(filePath, webServiceRequests[1]);
                    //Upload the Touch Data files
                    counter = 1;
                    for (int i = 0; i < 11; i++) {
                        String touchFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_touch.csv";
                        filePath = Paths.get(currentUser.getAbsolutePath(), touchFileNameTemplate);
                        uploadFileToServer(filePath, webServiceRequests[2]);
                        counter++;
                        sleep(1000);
                    }
                    //Upload the Paired Metadata files
                    counter = 1;
                    for (int i = 0; i < 11; i++) {
                        String pairFileNameTemplate = "color_" + userData.getUsername() + "_" + counter + "_pairs.csv";
                        filePath = Paths.get(currentUser.getAbsolutePath(), pairFileNameTemplate);
                        uploadFileToServer(filePath, webServiceRequests[3]);
                        counter++;
                        sleep(1000);
                    }
                } catch (InterruptedException ex) {
                    //Nothing
                }
            }
        };
        t.start();
    }

    //This method connects and authenticates as user admin to the RapidMiner Server, then uploads a specific file by calling the specified WebService
    //Note: Indeed the authentication is done over HTTP and it is not secure, but this is not the point of the application, plus the documentation of RapidMiner Server on enabling HTTPS is very poor..so i couldn't implement it
    private void uploadFileToServer(final Path filePath, final String webService) {
        //Bellow we initialize the basic objects and the authentication process for the Server
        String usernameColonPassword = "admin:rapidminerrocks21";
        //Create the basic authentication payload
        String basicAuthPayload = "Basic " + Base64.encodeToString(usernameColonPassword.getBytes(), Base64.NO_WRAP);
        //Create the static wrappers
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            // Connect to the web server endpoint
            URL serverUrl = new URL("http://35.204.200.249:8080/api/rest/process/" + webService);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
            // Set HTTP method as POST
            urlConnection.setRequestMethod("POST");
            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);
            //Set other parameters
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            //Bellow we start the file uploading process
            //Create basic static stuff for the HTTP requests
            String attachmentName = "patternData";
            String attachmentFileName = "patternData.csv";
            //Set connection to Keep-Alive, set Cache-Control to no-cache and Content-Type to multipart
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Cache-Control", "no-cache");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            //Open a data outputstream
            //Start content wrapper
            DataOutputStream request = new DataOutputStream(urlConnection.getOutputStream());
            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);
            //Read all the bytes of the file and store them
            byte[] bytes = Files.readAllBytes(filePath);
            //Write the bytes on the stream
            request.write(bytes);
            //End content wrapper
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            //Check if we get a 200 OK from server and get the response!
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //Get Server response
                InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = responseStreamReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                responseStreamReader.close();
                String response = stringBuilder.toString();
                Log.d("Response Debug!", response);
            }
            //Disconnect from Server
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.d("URL Debugging", e.toString());
        } catch (IOException e) {
            showMessage("Error!", "Unknown Error while trying to upload files to the Server!");
            Log.d("IO Debugging", e.toString());
        }
    }
}

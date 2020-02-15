//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
//JSON imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//Java imports
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class StatisticsActivity extends AppCompatActivity {

    private RadioGroup colorGroup, pianoGroup;
    private RadioButton radioButton;
    private Button colorShowButton, pianoShowButton;
    private File appFolder;
    private File statisticsFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getPatternStatistics();
        colorGroup = findViewById(R.id.colorRadioGroup);
        pianoGroup = findViewById(R.id.pianoRadioGroup);
        colorShowButton = findViewById(R.id.colorStatisticsShow);
        pianoShowButton = findViewById(R.id.pianoStatisticsShow);
        appFolder = getApplicationContext().getFilesDir();
        statisticsFolder = new File(appFolder, "Statistics");
        //Now i set listeners for the two Show buttons
        colorShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedButton = getSelectedButton(colorGroup);
                switch (selectedButton) {
                    case "Color Usage Frequency":
                        File file = new File(statisticsFolder, "colorRawStatistics.json");
                        JSONArray jArray = parseJSONArray(file);
                        HashMap<Integer, Float> map = new HashMap<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject obj = jArray.getJSONObject(i);
                                Integer number_of_point = obj.getInt("number_of_activated_point");
                                Integer times_used = obj.getInt("Times Used");
                                //I calculate the Usage Frequency by taking the times the color is used divided by 352 which are
                                //the total points that occurred and *100 to get the %
                                float frequency = (float) times_used / 352.0f * 100f;
                                map.put(number_of_point, frequency);
                            } catch (JSONException e) {
                                showMessage("Error", "An error occurred, please restart the Application");
                                Log.d("JSON Debugging", e.toString());
                            }
                        }
                        goToPointFrequencyGraphActivity(map, "Color", "Color");
                        break;
                    case "Pattern Reusability":
                        File file2 = new File(statisticsFolder, "colorPatternUsage.json");
                        JSONArray jArray2 = parseJSONArray(file2);
                        HashMap<String, Float> map2 = new HashMap<>();
                        for (int i = 0; i < jArray2.length(); i++) {
                            try {
                                JSONObject obj = jArray2.getJSONObject(i);
                                String sequence = obj.getString("Sequence");
                                Integer times_used = obj.getInt("Sequence Usage");
                                //I calculate the Reusability Frequency by taking the times the pattern is used divided by 88 which are
                                //the total points that occurred and *100 to get the %
                                float frequency = (float) times_used / 88.0f * 100f;
                                map2.put(sequence, frequency);
                            } catch (JSONException e) {
                                showMessage("Error", "An error occurred, please restart the Application");
                                Log.d("JSON Debugging", e.toString());
                            }
                        }
                        goToPatternUsabilityGraphActivity(map2);
                        break;
                    default:
                        break;
                }
            }
        });

        pianoShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedButton = getSelectedButton(pianoGroup);
                switch (selectedButton) {
                    case "Note Usage Frequency":
                        File file = new File(statisticsFolder, "pianoRawStatistics.json");
                        JSONArray jArray = parseJSONArray(file);
                        HashMap<Integer, Float> map = new HashMap<>();
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject obj = jArray.getJSONObject(i);
                                Integer number_of_point = obj.getInt("number_of_activated_point");
                                Integer times_used = obj.getInt("Times Used");
                                //I calculate the Usage Frequency by taking the times the color is used divided by 352 which are
                                //the total points that occurred and *100 to get the %
                                float frequency = (float) times_used / 352.0f * 100f;
                                map.put(number_of_point, frequency);
                            } catch (JSONException e) {
                                showMessage("Error", "An error occurred, please restart the Application");
                                Log.d("JSON Debugging", e.toString());
                            }
                        }
                        goToPointFrequencyGraphActivity(map, "Note", "Piano");
                        break;
                    case "Pattern Reusability":
                        File file2 = new File(statisticsFolder, "pianoPatternUsage.json");
                        JSONArray jArray2 = parseJSONArray(file2);
                        HashMap<String, Float> map2 = new HashMap<>();
                        for (int i = 0; i < jArray2.length(); i++) {
                            try {
                                JSONObject obj = jArray2.getJSONObject(i);
                                String sequence = obj.getString("Sequence");
                                Integer times_used = obj.getInt("Sequence Usage");
                                //I calculate the Reusability Frequency by taking the times the pattern is used divided by 88 which are
                                //the total points that occurred and *100 to get the %
                                float frequency = (float) times_used / 88.0f * 100f;
                                map2.put(sequence, frequency);
                            } catch (JSONException e) {
                                showMessage("Error", "An error occurred, please restart the Application");
                                Log.d("JSON Debugging", e.toString());
                            }
                        }
                        goToPatternUsabilityGraphActivity(map2);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    //This method reads a specific file and parses the json array
    private JSONArray parseJSONArray(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = "", result = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            return new JSONArray(result);
        } catch (FileNotFoundException e) {
            showMessage("FileNotFoundException", "Unexpected Error while opening file");
        } catch (IOException e) {
            showMessage("IOException", "Unexpected Error while trying to open a file");
            Log.d("IO Debugging", e.toString());
        } catch (JSONException e) {
            showMessage("Error", "An error occurred, please restart the Application");
            Log.d("JSON Debugging", e.toString());
        }
        return null;
    }

    //This method returns the Text of the current selected RadioButton of the provided RadioGroup
    private String getSelectedButton(RadioGroup group) {
        int radioID = group.getCheckedRadioButtonId();
        radioButton = findViewById(radioID);
        return radioButton.getText().toString();
    }

    //This method provides an information dialog to the user
    private void showMessage(String title, String message) {
        InfoDialog dialog = new InfoDialog();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show(getSupportFragmentManager(), title);
    }

    //This method calls the getStatisticData() method and provides the correct parameters to get the chosen statistical data
    private void getPatternStatistics() {
        //Defined String array with all the WebService IDs
        final String[] webServiceRequests = {"GetColorPatternRawStatistics?",
                "GetColorPatternUsage?",
                "GetPianoPatternRawStatistics?",
                "GetPianoPatternUsage?"};
        //Get Statistical data for color pattern
        getStatisticalData("colorRawStatistics.json", webServiceRequests[0]);
        getStatisticalData("colorPatternUsage.json", webServiceRequests[1]);
        //Get Statistical data for piano pattern
        getStatisticalData("pianoRawStatistics.json", webServiceRequests[2]);
        getStatisticalData("pianoPatternUsage.json", webServiceRequests[3]);
    }

    //This Method calls the Server's WenServices to get statistics in JSON format
    private void getStatisticalData(final String fileName, final String webService) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    String response = "";
                    boolean areDataReceived = false;
                    //Bellow we initialize the basic objects and the authentication process for the Server
                    String usernameColonPassword = "admin:rapidminerrocks21";
                    //Create the basic authentication payload
                    String basicAuthPayload = "Basic " + Base64.encodeToString(usernameColonPassword.getBytes(), Base64.NO_WRAP);
                    // Connect to the web server endpoint
                    URL serverUrl = new URL("http://35.204.200.249:8080/api/rest/process/" + webService);
                    HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
                    // Set HTTP method as GET
                    urlConnection.setRequestMethod("GET");
                    // Include the HTTP Basic Authentication payload
                    urlConnection.addRequestProperty("Authorization", basicAuthPayload);
                    //Set other parameters
                    urlConnection.setUseCaches(false);
                    urlConnection.setDoOutput(true);
                    //Set connection to Keep-Alive, set Cache-Control to no-cache and Content-Type application/json data and charset to utf-8
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Cache-Control", "no-cache");
                    urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    //Open an InputStream ang get the response of the server
                    InputStream responseStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check for the response code and if it is 200 OK we proceed else inform the user of an error
                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        while ((line = responseStreamReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        response = stringBuilder.toString();
                        Log.d("Response Debug!", response);
                        areDataReceived = true;
                    } else {
                        Log.d("Response Debug!", "Response Code: " + urlConnection.getResponseCode());
                        showMessage("Error", "Server Connection Failed!");
                    }
                    responseStreamReader.close();
                    //Disconnect from Server
                    urlConnection.disconnect();
                    //Now write the JSON data into a file
                    if (!areDataReceived) {
                        showMessage("Error!", "Statistics are not available at this time!");
                    } else {
                        if (!statisticsFolder.exists()) {
                            statisticsFolder.mkdirs();
                        }
                        File file = new File(statisticsFolder, fileName);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        bw.write(response);
                        bw.flush();
                        bw.close();
                    }
                    sleep(500);
                } catch (MalformedURLException e) {
                    Log.d("URL Debugging", e.toString());
                } catch (IOException e) {
                    showMessage("Error!", "Unknown IO Error");
                    Log.d("IO Debugging", e.toString());
                } catch (InterruptedException ex) {
                    //Wtver
                }
            }
        };
        t.start();
    }

    //This method opens PointFrequencyGraphActivity
    public void goToPointFrequencyGraphActivity(HashMap<Integer, Float> data, String value, String patternType) {
        Intent intent = new Intent(this, PointFrequencyGraphActivity.class);
        intent.putExtra("GraphData", data);
        intent.putExtra("Value", value);
        intent.putExtra("PatternType", patternType);
        startActivity(intent);
    }

    //This method opens ColorPatternView
    public void goToPatternUsabilityGraphActivity(HashMap<String, Float> data) {
        Intent intent = new Intent(this, PatternUsabilityGraphActivity.class);
        intent.putExtra("GraphData", data);
        startActivity(intent);
    }
}


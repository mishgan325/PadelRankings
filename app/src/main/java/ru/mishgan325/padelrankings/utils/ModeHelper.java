package ru.mishgan325.padelrankings.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModeHelper {
    private Context context;
    private JSONObject jsonData;
    private String fileName;

    public ModeHelper(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.jsonData = new JSONObject();
    }

    public void addUser(String name, ArrayList<Integer> values) {
        try {
            jsonData.put(name, values);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveToFile();
    }

    public void addValueToPlayer(String name, int value) {
        ArrayList<Integer> userValues = getPlayerValues(name);
        if (userValues != null) {
            userValues.add(value);
            try {
                jsonData.put(name, userValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            saveToFile();
        } else {
            // Handle user not found
        }
    }

    private ArrayList<Integer> getPlayerValues(String name) {
        try {
            return jsonData.has(name) ? (ArrayList<Integer>) jsonData.get(name) : null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getPlayerMode(String nick) {
        ArrayList<Integer> userValues = getPlayerValues(nick);
        if (userValues != null) {
            Map<Integer, Integer> frequencyMap = new HashMap<>();

            for (int num : userValues) {
                frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
            }

            int mode = 0;
            int maxFrequency = 0;
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                int frequency = entry.getValue();
                if (frequency > maxFrequency) {
                    maxFrequency = frequency;
                    mode = entry.getKey();
                }
            }

            return mode;
        } else {
            return -1;
        }
    }

    private void saveToFile() {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(jsonData.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

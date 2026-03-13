package com.example.comufavor;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JobPreferences {
    private static final String PREFS_NAME = "comufavor_jobs";
    private static final String KEY_JOBS = "published_jobs";
    private final SharedPreferences prefs;

    public JobPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveJob(RecruiterJob job) {
        List<RecruiterJob> jobs = getAllJobs();
        jobs.add(0, job); // Add at the beginning

        JSONArray array = new JSONArray();
        for (RecruiterJob j : jobs) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("title", j.getTitle());
                obj.put("location", j.getLocation());
                obj.put("price", j.getPrice());
                obj.put("date", j.getDate());
                obj.put("proposalsCount", j.getProposalsCount());
                // In a real app we'd save image URI/path too
                array.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString(KEY_JOBS, array.toString()).apply();
    }

    public List<RecruiterJob> getAllJobs() {
        List<RecruiterJob> jobs = new ArrayList<>();
        String json = prefs.getString(KEY_JOBS, "[]");
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                jobs.add(new RecruiterJob(
                        obj.getString("title"),
                        obj.getString("location"),
                        obj.getString("price"),
                        obj.getString("date"),
                        obj.optInt("proposalsCount", 0)
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jobs;
    }
}

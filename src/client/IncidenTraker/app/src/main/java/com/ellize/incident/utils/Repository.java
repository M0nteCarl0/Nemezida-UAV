package com.ellize.incident.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

class Repository {

    private static Repository ourInstance;
    private static String STORE_PATH;
    String[] paths = new String[4];

    static Repository getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new Repository(c.getApplicationContext());
        }
        return ourInstance;
    }

    private Repository(Context context) {
        //String f = Environment.getExternalStorageDirectory()+"/auv";
        STORE_PATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/nemezida/cats/";
    }

    /**
     * loads photos on disk for spe
     *
     * @param type - type of category
     * @return - path to direcotry
     */
    public void loadDataForCategory(int type) {
        new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                //todo load files from serever
                downloadFromServer(integers[0]);
                //todo load photos from directory
                return null;
            }

        }.execute();

    }

    private void downloadFromServer(int type) {

    }

}

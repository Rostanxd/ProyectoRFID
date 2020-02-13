package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import android.widget.ArrayAdapter;

import java.util.HashMap;

public class AdapterHashMap {
    private ArrayAdapter<String> adapter;
    private HashMap<Integer,String> hashMap;

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    public HashMap<Integer, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, String> hashMap) {
        this.hashMap = hashMap;
    }
}

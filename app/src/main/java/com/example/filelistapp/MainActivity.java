package com.example.filelistapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button1;
    ListView fileList;
    TextView textView;
    ArrayAdapter adapter;
    ArrayList<String> myList;
    ArrayList<String> filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                Log.v("TAG", "Granted!");
            else {
                Log.v("TAG", "Denied!");
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
            }
        }

        button1 = findViewById(R.id.button);
        fileList = findViewById(R.id.fileList);
        textView = findViewById(R.id.textView);

        myList = new ArrayList<>();
        filePath = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, R.layout.activity_listview, myList);
        fileList.setAdapter(adapter);

        button1.setOnClickListener(view -> {
            String folderToList = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            filePath.add(folderToList);
            File folderFile = new File(folderToList);
            myList.clear();
            if (folderFile.isDirectory()) {
                File[] files = folderFile.listFiles();
                assert files != null;
                for (File f : files) {
                    myList.add(f.getAbsolutePath());
                }
                adapter.notifyDataSetChanged();
            } else if (folderFile.isFile()) {
                try {
                    InputStream is = getAssets().open("folderFile");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                        builder.append(line).append("\n");
                    reader.close();
                    textView.setText(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        fileList.setOnItemClickListener((adapterView, view, i, l) -> {
            Object o = fileList.getItemAtPosition(i);
            String folderToList = o.toString() + "/";
            filePath.add(folderToList);
            File folderFile = new File(folderToList);
            myList.clear();
            if (folderFile.isDirectory()) {
                File[] files = folderFile.listFiles();
                assert files != null;
                for (File f : files) {
                    myList.add(f.getAbsolutePath());
                }
                adapter.notifyDataSetChanged();
            } else if (folderFile.isFile()) {
                try {
                    InputStream is = getAssets().open("folderFile");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                        builder.append(line).append("\n");
                    reader.close();
                    textView.setText(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1234)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.v("TAG", "Permission granted");
            else
                Log.v("TAG", "Permission denied");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(filePath.size() != 0) {
                filePath.remove(filePath.size() - 1);
                String folderToList = filePath.get(filePath.size() - 1);
                File folderFile = new File(folderToList);
                myList.clear();
                if (folderFile.isDirectory()) {
                    File[] files = folderFile.listFiles();
                    assert files != null;
                    for (File f : files) {
                        myList.add(f.getAbsolutePath());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
package com.example.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        List<String> items = new ArrayList<>();
        items.add(0, "Choose the action");
        items.add("gift");
        items.add("car");
        items.add("pay");
        items.add("pet");
        items.add("sell");
        items.add("explain");
        items.add("that");
        items.add("book");
        items.add("now");
        items.add("work");
        items.add("total");
        items.add("trip");
        items.add("future");
        items.add("good");
        items.add("thank you");
        items.add("learn");
        items.add("agent");
        items.add("should");
        items.add("like");
        items.add("movie");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if( parent.getItemAtPosition( position).equals("Choose the action"))
                {
                    //do nothing
                }
                else
                {
                    String item = parent.getItemAtPosition( position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " +item, Toast.LENGTH_SHORT).show();

                    if( parent.getItemAtPosition(position).equals(item))
                    {
                        EditText edit = (EditText)findViewById(R.id.editText);
                        TextView tview = (TextView)findViewById(R.id.editText);
                        String result = edit.getText().toString();
                        tview.setText(result);

                        Bundle selectedItem = new Bundle();
                        selectedItem.putString("lastname", result);
                        selectedItem.putString("Vid",item);
                        Intent intent = new Intent( MainActivity.this, Main2Activity.class);
                        intent.putExtras(selectedItem);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}


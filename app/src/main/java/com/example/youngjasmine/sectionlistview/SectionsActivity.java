package com.example.youngjasmine.sectionlistview;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class SectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView list = new ListView(this);

        SimpleSectionAdapter<String> adapter = new SimpleSectionAdapter<String>(
                list,
                R.layout.activity_sections,
                android.R.layout.simple_list_item_1){
            @Override
            public void onSectionItemClick(String item){
                Toast.makeText(SectionsActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        };

        adapter.addSection("Fruits", new String[]{"Apples", "Oranges", "Bananas", "Mangos"});
        //adapter.addSection("Meats", new String[]{"Pork", "Chicken", "Beef", "Lamb"});
        list.setAdapter(adapter);
        setContentView(list);
    }
}

package com.markpaveszka.pavloswaterfall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EditRulesActivity extends AppCompatActivity {

    private Button backToMainBtn, addNewBtn, addRuleBtn;
    private ListView rulesListView;
    private ArrayList<String> rules = new ArrayList<>();
    private BufferedReader ruleReader;
    private EditText ruleInput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rules);



    }

    private void showRules()
    {
        setContentView(R.layout.activity_edit_rules);

        backToMainBtn = (Button) findViewById(R.id.backToMainBtnER);
        addNewBtn = (Button) findViewById(R.id.addNewRuleBtn);
        rulesListView = (ListView) findViewById(R.id.rulesListview);




        setUp();

        PlayerListAdapter adapter = new PlayerListAdapter(EditRulesActivity.this,
                R.layout.adapter_view_layout, rules);

        rulesListView.setAdapter(adapter);

        rulesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                rules.remove(i);
            }
        });


        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRule();
            }
        });
    }

    private void addRule()
    {
        setContentView(R.layout.add_new_rule_layout);

        addRuleBtn = (Button) findViewById(R.id.addRuleBtn);
        ruleInput = (EditText) findViewById(R.id.ruleET);

        addRuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newRule = ruleInput.getText().toString();

                if (!newRule.equals("") && !rules.contains(newRule))
                {
                    rules.add(newRule);

                    showRules();
                }
                else
                {
                    //TOAST
                    Toast.makeText(EditRulesActivity.this,getString(R.string.incorrect_form),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUp()
    {
        try{
            ruleReader = new BufferedReader(new InputStreamReader(getAssets()
                                                .open("rules.txt")));

            String currentLine = ruleReader.readLine();

            /*WHILE CURRENTLINE IS NOT NULL, ADD THE CARD TO THE CARDS ARRAYLIST, ALSO READ THE
            NEXT LINE */

            while(currentLine != null)
            {
                //ADD CURRENT LINE TO THE ARRAYLIST
                rules.add(currentLine);

                //JUMP TO THE NEXT LINE
                currentLine = ruleReader.readLine();

            }//while
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}

package com.markpaveszka.pavloswaterfall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class AddPlayersActivity extends AppCompatActivity {

    private ListView playersList;
    private Button addNewPlayerBtn, allPlayersAddedBtn, addCurrentPlayerBtn;
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();
    private boolean isItFirstCallForNextPlayer =true;
    private EditText playerNameEditText;
    private RadioGroup maleOrFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_added_players);







        //call next player --> BASE FUNCTION OF THE LAYOUT_SHOW_ADDED_PLAYERS LAYOUT
        nextPlayer();


    }

    //BASE FUNCTION OF THE LAYOUT_SHOW_ADDED_PLAYERS LAYOUT
    private void nextPlayer()
    {

        //set the layout
        setContentView(R.layout.layout_show_added_players);

        // identify listview
        playersList =(ListView) findViewById(R.id.playersListViewSP);

        //create a adapter for the list
        PlayerListAdapter adapter = new PlayerListAdapter(AddPlayersActivity.this,
                R.layout.adapter_view_layout, playerNames);


        //set the adapter

        playersList.setAdapter(adapter);


        //if this function is called for the first time we should remove every entry from the list,
        // just to be sure that it is empty
        //and set the first call boolean to false, so it won't execute again
        if (isItFirstCallForNextPlayer)
        {
            playerNames.clear();
            isItFirstCallForNextPlayer= false;
        }


        //identify buttons
        addNewPlayerBtn = (Button) findViewById(R.id.addNewPlayerBtn);



        allPlayersAddedBtn =(Button) findViewById(R.id.allPlayersReadyBtn);




        //if addNewPlayer is pressed change screen by calling  addPlayer()
        addNewPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayer();
            }
        });

        allPlayersAddedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (players.size()>=3)
                {
                    Intent goToGame = new Intent(AddPlayersActivity.this,
                            DeckActivity.class).putStringArrayListExtra("players", players);
                    startActivity(goToGame);
                    finish();
                }
                else
                {
                    Toast.makeText(AddPlayersActivity.this,
                            getString(R.string.need_more_players), Toast.LENGTH_SHORT ).show();

                }
            }
        });


    }


    //BASE FUNCTION OF THE ACTIVITY_ADD_PLAYERS LAYOUT
    private void addPlayer()
    {
        //set the layout
        setContentView(R.layout.activity_add_players);

        //identify elements (radiogroup, edittext, button)

        maleOrFemale = (RadioGroup) findViewById(R.id.radiogroup);



        playerNameEditText = (EditText) findViewById(R.id.playerNameET);


        addCurrentPlayerBtn = (Button) findViewById(R.id.addPlayerBtn);


        addCurrentPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = playerNameEditText.getText().toString();
                String radioGroupValue;
                if(maleOrFemale.getCheckedRadioButtonId() == R.id.boyRB)
                {
                    radioGroupValue ="_Boy";
                }
                else if(maleOrFemale.getCheckedRadioButtonId() == R.id.girlRB)
                {
                    radioGroupValue ="_Girl";
                }
                else
                {
                    radioGroupValue ="_EMPTY";
                }

                if(!name.equals("")&&!playerNames.contains(name)&&!radioGroupValue.equals("_EMPTY"))
                {
                    playerNames.add(name);
                    String currentPlayer = name+radioGroupValue;
                    players.add(currentPlayer);

                    nextPlayer();
                }
                else
                {
                    Toast.makeText(AddPlayersActivity.this,
                            getString(R.string.player_add_error),  Toast.LENGTH_SHORT ).show();
                }
            }
        });

    }
}

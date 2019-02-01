package com.markpaveszka.pavloswaterfall;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class DeckActivity extends AppCompatActivity {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<String> cards = new ArrayList<>(), rules = new ArrayList<>(),
    playerNames = new ArrayList<>();
    private BufferedReader reader, ruleReader;
    private Button nextTurnBtn, endGameBtn;
    private ConstraintLayout drinkLayout, drinkIndicatorLayout, rulesLayout;
    private ImageView card;
    private int cardNumber =0, playerNumber =0, playerLimit;
    private ListView playersListToDrinkLV, rulesLV;
    private String currentLine;
    private TextView turnIndicatorTW, drinkTitleTV, playerNamesToDrinkTV, indicatorTitleTV;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck);

        card=(ImageView)findViewById(R.id.cardIMG);
        turnIndicatorTW = (TextView) findViewById(R.id.turnIndicatorTV);
        endGameBtn = (Button) findViewById(R.id.endGameBtn);

        /*DRINK LAYOUT IS THE FADE IN LAYOUT WHICH INDICATES WHICH PLAYERS HAVE TO DRINK
          IT IS SET TO INVISIBLE BY DEFAULT*/

        drinkLayout = (ConstraintLayout) findViewById(R.id.drinkLayout);
        drinkLayout.setVisibility(View.INVISIBLE);
        nextTurnBtn = (Button) findViewById(R.id.nextTurnBtn);
        drinkTitleTV = (TextView) findViewById(R.id.drinkTitleTV);
        playerNamesToDrinkTV = (TextView) findViewById(R.id.playersToDrinkTW);



        /*DRINK INDICATOR LAYOUT IS THE FADE IN LAYOUT WHICH CONTAINS A LIST OF PLAYERS, IF ONE IS
          PRESSED IT IS SENT FORWARD TO THE DRINK LAYOUT SAYING THAT PLAYER HAS TO DRINK
          IT IS SET TO INVISIBLE BY DEFAULT*/

        drinkIndicatorLayout = (ConstraintLayout) findViewById(R.id.drinkIndicatorLayout);
        drinkIndicatorLayout.setVisibility(View.INVISIBLE);
        playersListToDrinkLV = (ListView) findViewById(R.id.playerListDeckActivityLV);
        indicatorTitleTV = (TextView) findViewById(R.id.indicatorTitleTV);


        /*RULES LAYOUT IS THE FADE IN LAYOUT WHICH CONTAINS THE OPTIONS FOR THE JACK CARD
          IT IS SET TO INVISIBLE BY DEFAULT*/

        rulesLayout = (ConstraintLayout) findViewById(R.id.rulesLayout);
        rulesLayout.setVisibility(View.INVISIBLE);
        rulesLV =(ListView) findViewById(R.id.rulesListDeckActivityLV);


        //GET THE PLAYER NAMES FROM AddPlayerActivity
        ArrayList<String> passedNames = this.getIntent().getStringArrayListExtra("players");

        /* CREATING EACH PLAYER BY SPLITTING THE PASSED NAME AT '_'
           THIS WILL CREATE TWO PARAMETERS FOR THE PLAYERS GENDER AND NAME
        */
        for (int i =0; i< passedNames.size(); i++)
        {
            String[] temp_player = passedNames.get(i).split("_");
            players.add(new Player(temp_player[0], temp_player[1]));
            playerNames.add(temp_player[0]);
        }
        playerLimit = players.size();
        //toMiddle.setAnimationListener(this);
        //fromMiddle.setAnimationListener(this);

        //A SET UP METHOD WHICH EXECUTES PRIOR TO THE ACTUAL GAME STARTS
        setUp();

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = card;
                final String currentCard = chooseCards();
                card.setEnabled(false);
                // first quarter turn
                v.animate().withLayer()
                        .rotationY(90)
                        .setDuration(300)
                        .withEndAction(
                                new Runnable() {
                                    @Override public void run() {
                                        //CHANGE CARD IMAGE HERE:

                                            showCards(currentCard);


                                        // second quarter turn
                                        v.setRotationY(-90);
                                        v.animate().withLayer()
                                                .rotationY(0)
                                                .setDuration(300)
                                                .start();
                                    }
                                }
                        ).start();
                char cardTypeChar = currentCard.charAt(1);
                switch (cardTypeChar)
                {
                    case '2':
                        showDrinkIndicatorLayout("drink");
                        break;

                    case '3':
                        showDrinkLayout(playerNumber);
                        break;

                    case '4':
                        showDrinkIndicatorLayout("drink");
                        break;

                    case '5':
                        showDrinkLayout("guys");
                        break;

                    case '6':
                        showDrinkLayout("chicks");
                        break;

                    case '7':
                        showDrinkIndicatorLayout("drink");
                        break;

                    case '8':
                        showDrinkIndicatorLayout("mate");
                        break;

                    case '9':
                        showDrinkIndicatorLayout("drink");
                        break;

                    case '1':
                        showDrinkIndicatorLayout("drink");
                        break;

                    case 'j':
                        showDrinkLayout("rules");
                        break;

                    case 'q':
                        showDrinkLayout("questionKing", playerNumber);
                        break;

                    case 'k':
                        showDrinkLayout("everybody");
                        break;

                    case 'a':
                        showDrinkLayout("waterfall", playerNumber);
                        break;

                    default:
                        showDrinkLayout("error");
                        break;
                }

            }
        });


        nextTurnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDrinkLayout();
                showBack();
                nextTurn();
                card.setEnabled(true);
                nextTurnBtn.setEnabled(false);
            }
        });

        endGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> points = new ArrayList<>();
                for (int i=0; i<players.size(); i++)
                {
                    points.add(players.get(i).getConsumedSips());
                }

                Intent goToCharts = new Intent(DeckActivity.this, ChartActivity.class)
                        .putExtra("names",playerNames).putExtra("points", points);
                startActivity(goToCharts);
                finish();
            }
        });

    }


    /** The set up function sets up the game. It reads the cards and the rules. It also shuffles
     * the card set.*/
    private void setUp()
    {
        //CLEAR CARD ARRAYLIST TO INSURE THERE IS NOTHING IN IT AT THE GAME SETUP
        cards.clear();

        //SET THE TURNINDICATOR TEXTVIEW'S TEXT TO THE FIRST PLAYER'S NAME
        turnIndicatorTW.setText(players.get(playerNumber).getName());

        //USE A TRY-CATCH BLOCK TO READ IN THE CARDS FROM FILE
        try
        {
            //READ TWO DECKS OF CARDS FROM CARDLIST.TXT
            reader = new BufferedReader(
                     new InputStreamReader(getAssets().open("cardlist.txt")));

            //READ THE FIRST LINE OF THE TXT FILE
            currentLine = reader.readLine();

            /*WHILE CURRENTLINE IS NOT NULL, ADD THE CARD TO THE CARDS ARRAYLIST, ALSO READ THE
            NEXT LINE */

            while(currentLine != null)
            {
                //ADD CURRENT LINE TO THE ARRAYLIST
                cards.add(currentLine);

                //JUMP TO THE NEXT LINE
                currentLine = reader.readLine();

                //SHUFFLE THE CARDS EACH TIME A CARD IS ADDED, TO MAKE SURE THAT IT IS WELL SHUFFLED

                Collections.shuffle(cards);
            }//while


            currentLine ="";
            String cLine2 ="";

            //READ THE RULES FROM RULES.TXT
            ruleReader = new BufferedReader(
                    new InputStreamReader(getAssets().open("rules.txt")));

            //READ THE FIRST LINE OF THE TXT FILE
            cLine2 = ruleReader.readLine();

            /*WHILE CURRENTLINE IS NOT NULL, ADD THE CARD TO THE CARDS ARRAYLIST, ALSO READ THE
            NEXT LINE */

            while(cLine2 != null)
            {
                //ADD CURRENT LINE TO THE ARRAYLIST
                rules.add(cLine2);

                //JUMP TO THE NEXT LINE
                cLine2 = ruleReader.readLine();

            }//while
        }//try

        //CATCH THE IOEXCEPTION AND PRINTSTACKTRACE
        catch (IOException e)
        {
            e.printStackTrace();
        }//catch
        finally
        {
            try {
                reader.close();
                ruleReader.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }


        }

    }//setUp

    /** @param cardName is a parameter of this function. It is the name of the file in the drawable
     * folder and the appropriate card is chosen by this name
    */
    private void showCards(String cardName)
    {
        //GET THE RESUORCE ID OF THE CURRENT CARD FROM THE DRAWABLE FOLDER
        int id = getResources().getIdentifier("com.markpaveszka.pavloswaterfall:drawable/"
                        +cardName, null, null);

        //SET THE IMAGE FOR THE CARD IMAGEVIEW BY RESOURCE ID USING THE ID ABOVE
        card.setImageResource(id);

    }//showCards

    private void showBack()
    {
        final View v = card;
        final String currentCard = chooseCards();
        card.setEnabled(false);
        // first quarter turn
        v.animate().withLayer()
                .rotationY(90)
                .setDuration(300)
                .withEndAction(
                        new Runnable() {
                            @Override public void run() {

                                int id = getResources()
                                        .getIdentifier("com.markpaveszka.pavloswaterfall:"+
                                                "drawable/card_back", null, null);
                                card.setImageResource(id);

                                // second quarter turn
                                v.setRotationY(-90);
                                v.animate().withLayer()
                                        .rotationY(0)
                                        .setDuration(300)
                                        .start();
                            }
                        }
                ).start();



    }

    private String chooseCards()
    {


        String cardName = cards.get(cardNumber);
        cardNumber++;
        if(cardNumber==cards.size())
        {
            Collections.shuffle(cards);
            cardNumber=0;
        }
        return cardName;

    }

    private void startFadeInAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_anim));

    }


    private void startFadeOutAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_anim_reverse));

    }

    private void showDrinkLayout(String type)
    {
        nextTurnBtn.setEnabled(true);
        String title= getString(R.string.drink_players);
        switch (type)
        {
            case "everybody":
                playerNamesToDrinkTV.setText(getString(R.string.everybody));
                drinkTitleTV.setText(title);
                for (int i =0; i<players.size(); i++)
                {
                    players.get(i).increaseConsumedSips();
                }

                break;
            case "guys":
                ArrayList<String> toDrink = new ArrayList<>();
                for (int i=0; i<players.size(); i++)
                {
                    if (players.get(i).getGender().equals("Boy"))
                    {
                        ArrayList<String> mates = players.get(i).getMates();
                        for (int j=0; j<mates.size();j++)
                        {
                            if(!toDrink.contains(mates.get(j)))
                            {
                                toDrink.add(mates.get(j));
                            }
                        }
                        toDrink.add(players.get(i).getName());
                    }
                }
                String toDisplay ="";
                for (int i=0; i<toDrink.size(); i++)
                {
                    if(i== toDrink.size()-1)
                    {
                        toDisplay += toDrink.get(i);
                    }
                    else
                    {
                        toDisplay += toDrink.get(i)+", ";
                    }

                }
                playerNamesToDrinkTV.setText(toDisplay);
                drinkTitleTV.setText(title);
                for (int i=0; i< players.size();i++)
                {
                    for (int j = 0; j< toDrink.size(); j++)
                    {
                        if(players.get(i).getName().equals(toDrink.get(j))){
                            players.get(i).increaseConsumedSips();
                        }
                    }
                }
                break;
            case "chicks":
                toDrink = new ArrayList<>();
                for (int i=0; i<players.size(); i++)
                {
                    if (players.get(i).getGender().equals("Girl"))
                    {
                        ArrayList<String> mates = players.get(i).getMates();
                        for (int j=0; j<mates.size();j++)
                        {
                            if(!toDrink.contains(mates.get(j)))
                            {
                                toDrink.add(mates.get(j));
                            }
                        }
                        toDrink.add(players.get(i).getName());
                    }
                }
                toDisplay = "";
                for (int i=0; i<toDrink.size(); i++)
                {
                    if(i== toDrink.size()-1)
                    {
                        toDisplay += toDrink.get(i);
                    }
                    else
                    {
                        toDisplay += toDrink.get(i)+", ";
                    }
                }
                playerNamesToDrinkTV.setText(toDisplay);
                drinkTitleTV.setText(title);
                for (int i=0; i< players.size();i++)
                {
                    for (int j = 0; j< toDrink.size(); j++)
                    {
                        if(players.get(i).getName().equals(toDrink.get(j))){
                            players.get(i).increaseConsumedSips();
                        }
                    }
                }
                break;
            case "rules":
                    playerNamesToDrinkTV.setText(getString(R.string.rule_in_effect));
                    drinkTitleTV.setText(players.get(playerNumber).getName());
                break;


            case "error":
                playerNamesToDrinkTV.setText(R.string.error_content);
                drinkTitleTV.setText(R.string.error);
                break;
            default:
                playerNamesToDrinkTV.setText(R.string.error_content);
                drinkTitleTV.setText(R.string.error);
                break;
        }

        //CODE TO DISPLAY STRINGS ARE MISSING

        startFadeInAnimation(drinkLayout);
        drinkLayout.setVisibility(View.VISIBLE);
    }

    private void showDrinkLayout(int playerNo)
    {
        nextTurnBtn.setEnabled(true);

        ArrayList<String> matesOfPlayer = players.get(playerNo).getMates();
        ArrayList<String> toDrink = new ArrayList<>();
        String playersToDrink= "";
        String title= getString(R.string.drink_players);
        toDrink.add(players.get(playerNo).getName());
        if (matesOfPlayer.size()>0)
        {
            for (int i =0; i<matesOfPlayer.size();i++)

            {

               playersToDrink += matesOfPlayer.get(i)+", ";

            }

        }
        playersToDrink += players.get(playerNo).getName();

        playerNamesToDrinkTV.setText(playersToDrink);
        drinkTitleTV.setText(title);

        for (int i=0; i< players.size();i++)
        {
            for (int j = 0; j< matesOfPlayer.size(); j++)
            {
                if(players.get(i).getName().equals(matesOfPlayer.get(j))){
                    players.get(i).increaseConsumedSips();
                }
            }
        }
        players.get(playerNo).increaseConsumedSips();

        startFadeInAnimation(drinkLayout);
        drinkLayout.setVisibility(View.VISIBLE);
    }

    private void showDrinkLayout(String type, int index)
    {
        nextTurnBtn.setEnabled(true);
        String toDisplay ="", title ="";

        /*if(type.equals("rules"))
        {
            toDisplay = rules.get(index);
            title=getString(R.string.rule_in_effect);
        }*/
        if(type.equals("questionKing"))
        {
            toDisplay = players.get(index).getName();
            title=getString(R.string.questionking);
        }
        else if(type.equals("waterfall"))
        {
            title = players.get(index).getName();
            toDisplay= getString(R.string.waterfall);
            players.get(index).increaseWaterfalls();
        }
        else if(type.equals("mate"))
        {
            toDisplay = players.get(index).getName()+ " "+getString(R.string.and)+" "+players.get(playerNumber).getName() ;
            title= getString(R.string.new_mates);
            players.get(index).addMate(players.get(playerNumber).getName());
            players.get(playerNumber).addMate(players.get(index).getName());
        }
        else
        {
            title = getString(R.string.error);
            toDisplay= getString(R.string.error_content);
        }

        playerNamesToDrinkTV.setText(toDisplay);
        drinkTitleTV.setText(title);

        //FINISH CODE TO DISPLAY THAT STRING

        startFadeInAnimation(drinkLayout);
        drinkLayout.setVisibility(View.VISIBLE);
    }

    private void showDrinkIndicatorLayout(final String type)
    {
        if(type.equals("mate"))
        {
            indicatorTitleTV.setText(getText(R.string.choose_mate));

            if(checkMateNumber())
            {
                for (int i =0; i<players.size(); i++)
                {
                    players.get(i).resetMates();
                }
            }

        }
        else
        {
            indicatorTitleTV.setText(getText(R.string.choose_drink));
        }

        PlayerListAdapter adapter =
                new PlayerListAdapter(DeckActivity.this, R.layout.adapter_view_layout,
                        playerNames);

        playersListToDrinkLV.setAdapter(adapter);

        playersListToDrinkLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( type.equals("mate"))
                {
                    if(i!=playerNumber)
                    {
                        if(!players.get(playerNumber).getMates().contains(players.get(i).getName()))
                        {
                            hideDrinkIndicatorLayout();
                            showDrinkLayout(type, i);
                        }
                        else
                        {
                            Toast.makeText(DeckActivity.this,
                                    getString(R.string.already_mate),Toast.LENGTH_SHORT ).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(DeckActivity.this,getString(R.string.own_mate),
                                Toast.LENGTH_SHORT ).show();
                    }
                }
                else if(type.equals("drink"))
                {

                    hideDrinkIndicatorLayout();
                    showDrinkLayout(i);
                }
                else
                {
                    hideDrinkIndicatorLayout();
                    showDrinkLayout("error");
                }
            }
        });

        startFadeInAnimation(drinkIndicatorLayout);
        drinkIndicatorLayout.setVisibility(View.VISIBLE);

    }

   /* private void showRulesLayout()
    {
       PlayerListAdapter adapter =
                new PlayerListAdapter(DeckActivity.this,R.layout.adapter_view_layout,rules);

        rulesLV.setAdapter(adapter);

        rulesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideRulesLayout();
                showDrinkLayout("rules", i);
            }
        });

        startFadeInAnimation(rulesLayout);
        rulesLayout.setVisibility(View.VISIBLE);
    }*/

    private void hideDrinkLayout()
    {
        startFadeOutAnimation(drinkLayout);
        drinkLayout.setVisibility(View.INVISIBLE);
    }

    private void hideDrinkIndicatorLayout()
    {
        startFadeOutAnimation(drinkIndicatorLayout);
        drinkIndicatorLayout.setVisibility(View.INVISIBLE);
    }

    private void hideRulesLayout()
    {
        startFadeOutAnimation(rulesLayout);
        rulesLayout.setVisibility(View.INVISIBLE);
    }

    private void nextTurn()
    {
        playerNumber++;
        if(playerNumber==players.size())
        {
            playerNumber=0;
        }

        turnIndicatorTW.setText(players.get(playerNumber).getName());
    }

    private boolean checkMateNumber()
    {
        int playerNo = players.size();
        int maxMateNo = (playerNo*(playerNo-1))/2;

        int mateCounter=0;

        for (int i=0; i<playerNo; i++)
        {
            mateCounter += players.get(i).getMates().size();
        }

        mateCounter= mateCounter/2;

        if(mateCounter == maxMateNo)
        {
            return true;
        }

        else
        {
            return false;
        }

    }

    private int getPlayerNumberByName(String name)
    {
        for( int i=0; i<players.size(); i++)
        {
            if(name.equals(players.get(i).getName()))
            {
                return i;
            }
        }

        return -1;
    }


}

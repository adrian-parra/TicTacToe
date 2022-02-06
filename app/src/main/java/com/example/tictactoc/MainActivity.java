package com.example.tictactoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView playerOneScore,playerTwoScore,playerStatus;
    private Button[] buttons = new Button[9];

    private Button resetGame ,speack ,cale;

    private int playerOneScoreCount,playerTwoScoreCount ,rountCount;
    boolean activePlayer;

    //p1 = 0
    //p2 = 1
    //empty = 2

    int [] gameState = {2,2,2,2,2,2,2,2,2};

    int [][] winningPositions = {
            {0,1,2},{3,4,5},{6,7,8}, //rows
            {0,3,6},{1,4,7},{2,5,8}, //columns
            {0,4,8},{2,4,6} //cross

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = (TextView)findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView)findViewById(R.id.playerTwoScore);


        resetGame = (Button)findViewById(R.id.btnRecet);
        speack = (Button)findViewById(R.id.btnVoice);

        //cale = (Button)findViewById(R.id.btn_8);
        rountCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

        for(int i=0; i < buttons.length; i++){
            String buttonID = "btn_"+i;
            int resourceID =getResources().getIdentifier(buttonID,"id",getPackageName());
            buttons[i] = (Button)findViewById(resourceID);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   inputGame(v ,false ,0);

                }
            });
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                updatePlayerScore();
                playAgain();
            }
        });

        speack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi speak something");

                try {
                    startActivityForResult(intent ,1000);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage() ,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void inputGame(View v ,Boolean voice ,int input){
        if (!((Button)v).getText().toString().equals("")){
            return;
        }

        int gameStatePointer = 0;
        if (voice){
            gameStatePointer = input;
        }else{
            String buttonID = v.getResources().getResourceEntryName(v.getId()); //btn_0
            gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length())); //0
        }



        if(activePlayer){
            ((Button)v).setText("X");

            gameState[gameStatePointer] = 0;
        }else{
            ((Button)v).setText("O");

            gameState[gameStatePointer] = 1;
        }

        rountCount++;

        if(checkWinner()){
            if(activePlayer){
                playerOneScoreCount++;
                Snackbar.make(v, "Jugador uno ,ganador", Snackbar.LENGTH_LONG)
                        .setTextColor(v.getResources().getColor(R.color.black))
                        .setBackgroundTint(v.getResources().getColor(R.color.Primary))
                        .show();
            }else{
                playerTwoScoreCount++;
                Snackbar.make(v, "Jugador dos ,ganador ", Snackbar.LENGTH_LONG)
                        .setTextColor(v.getResources().getColor(R.color.black))
                        .setBackgroundTint(v.getResources().getColor(R.color.Primary))
                        .show();
            }
            updatePlayerScore();
            playAgain();
        }else if(rountCount == 9){
            playAgain();
            Snackbar.make(v, "Empate", Snackbar.LENGTH_LONG)
                    .setTextColor(v.getResources().getColor(R.color.black))
                    .setBackgroundTint(v.getResources().getColor(R.color.Primary))
                    .show();
        }else{
            activePlayer = !activePlayer;
        }
    }

    public boolean checkWinner(){
        boolean winnerResult = false;

        for (int [] winningPosition : winningPositions){
            //Toast.makeText(this, Integer.toString( gameState[winningPosition[0]]), Toast.LENGTH_SHORT).show();
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] != 2){
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));

    }

    public void playAgain(){
        rountCount = 0;
        activePlayer = true;


        for(int i=0; i < buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }

    public int convertLyricsToNumber(String Lyrics){
        switch (Lyrics){

            case "uno":
            case "1":{
                return 0;
            }
            case "dos":
            case "2":{
                return 1;
            }
            case "tres":
            case "3":{
                return 2;
            }
            case "cuatro":
            case "4":{
                return 3;
            }
            case "cinco":
            case "5": {
                return 4;
            }
            case "seis":
                case "6":{
                return 5;
            }
            case "siete": case "7":{
                return 6;
            }
            case "ocho": case  "8":{
                return 7;
            }
            case "nueve": case  "9":{
                return 8;
            }
        }

        return 9;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1000:{
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                   if(convertLyricsToNumber(result.get(0)) == 9){
                       Toast.makeText(this, "OPCION INCORRECTA", Toast.LENGTH_SHORT).show();
                   }else{
                       String buttonID = "btn_"+ convertLyricsToNumber(result.get(0));
                       int resourceID =getResources().getIdentifier(buttonID,"id",getPackageName());

                       Button btn = (Button)findViewById(resourceID);

                       inputGame(btn ,true, convertLyricsToNumber(result.get(0)));
                   }


                }
                break;
            }
        }
    }
}
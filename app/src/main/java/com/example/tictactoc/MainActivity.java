package com.example.tictactoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView playerOneScore,playerTwoScore,playerStatus;
    private Button[] buttons = new Button[9];
    private Button resetGame;

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

                    if (!((Button)v).getText().toString().equals("")){
                        return;
                    }

                    String buttonID = v.getResources().getResourceEntryName(v.getId()); //btn_0
                    int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length())); //0


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
                            Snackbar.make(v, "Ganador ,Jugador Uno ", Snackbar.LENGTH_LONG)
                                    .setTextColor(v.getResources().getColor(R.color.white))
                                    .setBackgroundTint(v.getResources().getColor(R.color.Primary))
                                    .show();
                        }else{
                            playerTwoScoreCount++;
                            Snackbar.make(v, "Ganador ,Jugador dos ", Snackbar.LENGTH_LONG)
                                    .setTextColor(v.getResources().getColor(R.color.white))
                                    .setBackgroundTint(v.getResources().getColor(R.color.Primary))
                                    .show();
                        }
                        updatePlayerScore();
                        playAgain();
                    }else if(rountCount == 9){
                        playAgain();
                    }else{
                        activePlayer = !activePlayer;
                    }

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


}
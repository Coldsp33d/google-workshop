package com.example.shivadeviah.scarnesdices;

import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int computerOverallScore = 0;
    private int computerTurnScore = 0;

    private String scoreText = "Your score: %d\t Computer score: %d";
    private TextView scoreTextView;
    private Random random = new Random();
    private ImageView diceFace;
    private Button rollButton, holdButton;
    final Handler handler = new Handler();
    Runnable runnableCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = (TextView) findViewById(R.id.score_text);
        diceFace = (ImageView) findViewById(R.id.dice_face);
        scoreTextView.setText(String.format(scoreText, userOverallScore, computerOverallScore));
        rollButton = (Button) findViewById(R.id.roll);
        holdButton = (Button) findViewById(R.id.hold);
    }

    int rollAndUpdate()
    {
        int roll = random.nextInt(6) + 1;
        switch(roll)
        {
            case 1: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
                break;

            case 2: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
                break;

            case 3: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
                break;

            case 4: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
                break;

            case 5: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
                break;

            case 6: diceFace.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
                break;
        }

        return roll;
    }

    void computerTurn()
    {
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);


        // Define the code block to be executed

        runnableCode = new Runnable() {
            @Override
            public void run() {
                int roll;
                String temp;

                if (computerTurnScore < 20) {
                    roll = rollAndUpdate();
                    if (roll != 1) {
                        temp = scoreText + "\n [Computer turn score: %d]";
                        computerTurnScore += roll;
                        scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore, computerTurnScore));
                        handler.postDelayed(this, 1000);
                    } else {
                        temp = scoreText + "\n [Computer rolled a 1!]";
                        computerTurnScore = 0;
                        scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore));
                        rollButton.setEnabled(true);
                        holdButton.setEnabled(true);
                        handler.removeCallbacks(this);
                    }
                } else {
                    //Log.i("TAG1 Turn", computerTurnScore + "");
                    //Log.i("TAG2 Overall", computerOverallScore + "");
                    temp = scoreText + "\n [Computer holds!]";
                    computerOverallScore += computerTurnScore;
                    computerTurnScore = 0;
                    scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore));
                    rollButton.setEnabled(true);
                    holdButton.setEnabled(true);
                    handler.removeCallbacks(this);
                }
            }
        };

        // Start the initial runnable task by posting through the handler
        handler.postDelayed(runnableCode, 1000);



        /*while(computerTurnScore < 20)
        {
            roll = rollAndUpdate();
            if(roll == 1)
            {
                temp = scoreText + "\t Yes! Computer rolled a 1!";
                computerTurnScore = 0;
                scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore));
                break;
            }

            temp = scoreText + "\t Computer turn score: %d";
            computerTurnScore += roll;
            scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore, computerTurnScore));

        }*/

        if(computerOverallScore >= 100)
        {
            String scoreText = "COMPUTER WINS!";
            scoreTextView.setText(scoreText);
        }
    }

    public void onRoll(View view)
    {
        int roll = rollAndUpdate();
        String temp;
        if(roll != 1)
        {
            temp = scoreText + "\n [Your turn score: %d]";
            userTurnScore += roll;
            scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore, userTurnScore));
        }
        else
        {
            temp = scoreText + "\n [You rolled a 1!]";
            userTurnScore = 0;
            scoreTextView.setText(String.format(temp, userOverallScore, computerOverallScore));
            computerTurn();
        }
    }

    public void onHold(View view)
    {
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        if(userOverallScore >= 100)
        {
            String scoreText = "USER WINS!";
            rollButton.setEnabled(false);
            scoreTextView.setText(scoreText);
        }
        else
        {
            scoreTextView.setText(String.format(scoreText, userOverallScore, computerOverallScore));
            computerTurn();
        }
    }

    public void onReset(View view)
    {
        handler.removeCallbacks(runnableCode);
        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);

        scoreTextView.setText(String.format(scoreText, userOverallScore, computerOverallScore));
    }
}

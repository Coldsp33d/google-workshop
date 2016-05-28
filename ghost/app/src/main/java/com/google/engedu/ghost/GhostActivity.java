package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String FRAGMENT = "FRAGMENT";
    private String STATUS = "STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
            //dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        char c = (char)event.getUnicodeChar();

        c = Character.toString(c).toLowerCase().charAt(0);

        if(c >= 'a' && c <= 'z')
        {
            String fragment = text.getText() + Character.toString(c);
            text.setText(fragment);

            userTurn = false;
            label.setText(COMPUTER_TURN);
            computerTurn();
            /*if(dictionary.isWord(text.getText() + ""))
                label.setText("It's a complete word!");*/
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    public void onChallenge(View view)
    {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        String fragment = text.getText() +"";

        if(fragment.length() >=4 && dictionary.isWord(fragment)){
            label.setText("It's a valid word! User wins!");
        }
        else
        {
            String temp = dictionary.getGoodWordStartingWith(fragment);
            if(temp == null)
            {
                label.setText("Not a prefix of any valid word! User wins!");
            }
            else {
                label.setText("It's a valid fragment! Computer wins! \n [Possible word: " + temp + "]");

            }
        }
    }

    private void computerTurn() {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);

        String fragment = text.getText() +"";

        if(fragment.length() >=4 && dictionary.isWord(fragment)){
            label.setText("It's a valid word! Computer wins!");
        }

        else
        {
            String temp = dictionary.getAnyWordStartingWith(fragment);
            if(temp == null)
            {
                label.setText("Not a prefix of any valid word! Computer wins!");
            }
            else
            {
                fragment += temp.charAt(fragment.length());
                text.setText(fragment);

                // Do computer turn stuff then make it the user's turn again
                userTurn = true;
                label.setText(USER_TURN);
            }
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Save the user's current game state
        savedInstanceState.putString(FRAGMENT, text.getText() + "");
        savedInstanceState.putString(STATUS, label.getText()+ "");

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Restore state members from saved instance
        text.setText(savedInstanceState.getString(FRAGMENT));
        label.setText(savedInstanceState.getString(STATUS));
    }
}

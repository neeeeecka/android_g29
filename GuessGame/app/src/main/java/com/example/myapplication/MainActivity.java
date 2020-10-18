package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.os.Vibrator;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int _Lives = 5;
    private int lives = 0, score = 0;
    private  int currentRandomNumber = 0;
    private ImageView[] hearts = new ImageView[_Lives];

    TextView scoreText;
    EditText typedTextbox;
    TextView clap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Guess Game by Nika Gabisonia");

        scoreText = (TextView) findViewById(R.id.score);
        typedTextbox = (EditText) findViewById(R.id.typed);
        clap = (TextView) findViewById(R.id.clap);

        for(int i = 1; i<=_Lives; i++) {
            String heartId = "heart" + i;
            int resID = getResources().getIdentifier(heartId, "id", getPackageName());
            hearts[i-1] = (ImageView) findViewById(resID);
        }
        clap.animate().alpha(0f).setDuration(0);
        startGame();
    }

    private  void startGame(){
        makeNewRandomNumber();
        clap.animate().alpha(0f).setDuration(125);
        for(int i = 0; i<_Lives; i++){
            hearts[i].setImageResource((R.drawable.heart));
        }
        lives = _Lives;
        score = 0;
    }

    private  void waitForRestart(int length){
        (new Handler()).postDelayed(this::startGame, length);
    }

    private void makeNewRandomNumber(){
        Random rand = new Random();
        currentRandomNumber = rand.nextInt(10) + 1;
    }

    public void onCheck(View exitView) {
        String typedText = typedTextbox.getText().toString();
        typedTextbox.setText("");

        String message = "";

        if (typedText.length() > 0) {
            int typedNum = Integer.parseInt(typedText);

            if (typedNum == currentRandomNumber) {
                clap.animate().alpha(1f).setDuration(125);
                waitForRestart(2500);
                score += lives;
                scoreText.setText("Score: " + score);
                message = "Nice job! You won. Game restarted. Your score: " + score + " Points";
            } else {
                hearts[lives-1].setImageResource(R.drawable.heartoff);
                lives--;
                looseLife(50);

                if (lives <= 0) {
                    looseLife(600);
                    message = "You died. Try again.";
                    waitForRestart(600);
                }else{
                    if(typedNum > currentRandomNumber){
                        message = "Try smaller";
                    }else{
                        message = "Try higher";
                    }
                }
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void looseLife(int length){
        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(length);
        }
    }

    public void onExit(View exitView){
        this.finishAffinity();
    }
}
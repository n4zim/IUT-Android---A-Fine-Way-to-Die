package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;

public class Game extends Activity {

    float bpm = 103;
    long beat = (long) ((float) (60/bpm) * 1000);
    int currentBeat, bigBeat, score, diviseWtf = 0;
    Boolean wtfMode = false;

    private MediaPlayer mPlayer;

    TextView beatDisplay, scoreDisplay, levelDisplay;
    RelativeLayout gameLayout;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    Random rand = new Random();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT >= 19)
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_game);
        gameLayout = (RelativeLayout)findViewById(R.id.gamelay);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        beatDisplay = (TextView)findViewById(R.id.beatDisplay);
        scoreDisplay = (TextView)findViewById(R.id.score);
        levelDisplay = (TextView)findViewById(R.id.level);
        mPlayer.start();

        startTimer();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                Intent intent = new Intent(Game.this, Result.class);
                intent.putExtra("SCORE", ""+score);

                startActivity(intent);
                finish();
            }
        });

    }

    public void startTimer() {
        timer = new Timer();
        initTimer();
        try {
            timer.schedule(timerTask, new Date(), (beat/4));
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Illegal argument : " + (beat/4));
        }
    }

    public void initTimer() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        currentBeat++;

                        Random rnd = new Random();

                        if(!wtfMode) {
                            diviseWtf = 4;
                        } else {
                            diviseWtf = 1;
                        }

                        if((((currentBeat-1) % diviseWtf) == 0))
                            gameLayout.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

                        if((((currentBeat-1) % 4) == 0)) bigBeat++;

                        beatDisplay.setText(
                                "Progression : " + bigBeat+"/363 (" + ((currentBeat % 4)+1) + "/4)\n"
                                +"Longueur : " + currentBeat + "/1457"
                        );

                        scoreDisplay.setText("" + ++score);

                        switch (bigBeat) {
                            case 8: // Whaooo
                                levelDisplay.setText("WHAOW");
                                wtfMode = true;
                                break;

                            case 12: // DROP #1
                                levelDisplay.setText("Niveau 1");
                                wtfMode = false;
                                break;

                            case 28:
                                levelDisplay.setText("Niveau 2");
                                break;

                            case 44:
                                levelDisplay.setText("Niveau 3");
                                break;

                            case 60:
                                levelDisplay.setText("Niveau 4");
                                break;

                            case 80:
                                levelDisplay.setText("GET READY");
                                wtfMode = true;
                                break;

                            case 84: // DROP #2
                                levelDisplay.setText("Niveau 5");
                                wtfMode = false;
                                break;

                            case 116:
                                levelDisplay.setText("Niveau 6");
                                break;

                            case 148: // Chant
                                levelDisplay.setText("COOL MODE");
                                break;

                            case 180:
                                levelDisplay.setText("VERY COOL MODE");
                                break;

                            case 212:
                                levelDisplay.setText("STILL COOL");
                                break;

                            case 244:
                                levelDisplay.setText("OWH...");
                                break;

                            case 260:
                                levelDisplay.setText("...YES !");
                                break;

                            case 273: // Whaooo
                                levelDisplay.setText("WHAOW");
                                wtfMode = true;
                                break;

                            case 277: // DROP #3
                                levelDisplay.setText("Niveau 7");
                                wtfMode = false;
                                break;
                        }

                    }
                });
            }
        };
    }

    public void onDestroy() {
        mPlayer.stop();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            mPlayer.pause();
            timer.cancel();
            final int pausedBeat = currentBeat;
            Toast.makeText(getApplicationContext(), "PAUSED : "+pausedBeat, Toast.LENGTH_SHORT).show();

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Quitter le jeu en cours ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Game.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mPlayer.start();
                            currentBeat = pausedBeat;
                            startTimer();
                        }
                    })
                    .show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
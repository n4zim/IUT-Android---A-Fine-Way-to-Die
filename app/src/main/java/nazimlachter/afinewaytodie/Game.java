package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    Boolean coolMode = false;
    int beatTimeCoolMode = 0;
    Boolean[] buttonStates = { false, false, false };
    int[] backgrounds = { R.drawable.banana, R.drawable.quiche, R.drawable.violon,
            R.drawable.paperplane, R.drawable.screen, R.drawable.rocket, R.drawable.koala, R.drawable.cow };

    private MediaPlayer mPlayer;

    TextView beatDisplay, scoreDisplay, levelDisplay, shake;
    RelativeLayout gameLayout;
    Button b1, b2, b3;

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

        shake = (TextView)findViewById(R.id.shake);
        shake.setVisibility(View.GONE);

        b1 = (Button)findViewById(R.id.b1);
        b1.setVisibility(View.GONE);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) { buttonState(1); }});

        b2 = (Button)findViewById(R.id.b2);
        b2.setVisibility(View.GONE);
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) { buttonState(2); }});

        b3 = (Button)findViewById(R.id.b3);
        b3.setVisibility(View.GONE);
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) { buttonState(3); }});

        mPlayer.start();
        startTimer();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                if(timer != null) {
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

                        // -------------------------------------------------------------------------

                        if(!wtfMode) diviseWtf = 4;
                            else diviseWtf = 1;

                        // -------------------------------------------------------------------------

                        if((((currentBeat-1) % diviseWtf) == 0))
                            gameLayout.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

                        // -------------------------------------------------------------------------

                        if(shake.getVisibility() == View.GONE)
                            if(coolMode && (rnd.nextInt(20)) == 1) {
                                    beatTimeCoolMode = 6;
                                    shake.setVisibility(View.VISIBLE);

                            } else if((rnd.nextInt(50)) == 1) {
                                    beatTimeCoolMode = 4;
                                    shake.setVisibility(View.VISIBLE);
                            }

                        // -------------------------------------------------------------------------

                        if((((currentBeat-1) % 4) == 0)) {

                            if(beatTimeCoolMode > 0)
                                if(--beatTimeCoolMode == 0) shake.setVisibility(View.GONE);

                            // ---------------------------------------------------------------------

                            //if(b1.getVisibility() == View.VISIBLE) {
                            int b1X = (int) ((gameLayout.getWidth() - b1.getX()) - 1)+2;
                            int b1Y = (int) ((gameLayout.getHeight() - b1.getY()) - 1)+2;
                            b1.setX(rnd.nextInt(b1X <= 0 ? -b1X : b1X));
                            b1.setY(rnd.nextInt(b1Y <= 0 ? -b1Y : b1Y));

                            b1.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                            b1.setCompoundDrawablesWithIntrinsicBounds(backgrounds[rnd.nextInt(backgrounds.length)], 0, 0, 0);
                            //} else if(buttonStates[0]) { b1.setVisibility(View.VISIBLE); buttonStates[0] = false; }
                            if(buttonStates[0]) { b1.setVisibility(View.VISIBLE); buttonStates[0] = false; }

                            //if(b2.getVisibility() == View.VISIBLE) {
                            int b2X = (int) ((gameLayout.getWidth() - b2.getX()) - 1)+2;
                            int b2Y = (int) ((gameLayout.getHeight() - b2.getY()) - 1)+2;
                            b2.setX(rnd.nextInt(b2X <= 0 ? -b2X : b2X));
                            b2.setY(rnd.nextInt(b2Y <= 0 ? -b2Y : b2Y));

                            b2.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                            b2.setCompoundDrawablesWithIntrinsicBounds(backgrounds[rnd.nextInt(backgrounds.length)], 0, 0, 0);
                            //} else if(buttonStates[1]) { b2.setVisibility(View.VISIBLE); buttonStates[1] = false; }
                            if(buttonStates[1]) { b2.setVisibility(View.VISIBLE); buttonStates[1] = false; }

                            //if(b3.getVisibility() == View.VISIBLE) {
                            int b3X = (int) ((gameLayout.getWidth() - b3.getX()) - 1)+2;
                            int b3Y = (int) ((gameLayout.getHeight() - b3.getY()) - 1)+2;
                            b3.setX(rnd.nextInt(b3X < 0 ? -b3X : b3X));
                            b3.setY(rnd.nextInt(b3Y < 0 ? -b3Y : b3Y));

                            b3.setBackgroundColor(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                            b3.setCompoundDrawablesWithIntrinsicBounds(backgrounds[rnd.nextInt(backgrounds.length)], 0, 0, 0);
                            //} else if(buttonStates[2]) { b3.setVisibility(View.VISIBLE); buttonStates[2] = false; }
                            if(buttonStates[2]) { b3.setVisibility(View.VISIBLE); buttonStates[2] = false; }

                            // ---------------------------------------------------------------------

                            bigBeat++;
                        }

                        // -------------------------------------------------------------------------

                        if(beatTimeCoolMode > 0) {
                            scoreDisplay.setText("" + ++score);
                        }

                        // -------------------------------------------------------------------------

                        beatDisplay.setText(
                                "Progression : " + bigBeat+"/363 (" + ((currentBeat % 4)+1) + "/4)\n"
                                        +"Longueur : " + currentBeat + "/1457"
                        );

                        // -------------------------------------------------------------------------

                        switch (bigBeat) {
                            case 8: // Whaooo
                                levelDisplay.setText("WHAOW");
                                wtfMode = true;
                                beatTimeCoolMode = 4;
                                break;

                            case 12: // DROP #1
                                levelDisplay.setText("Niveau 1");
                                b1.setVisibility(View.VISIBLE);
                                beatTimeCoolMode = 2;
                                wtfMode = false;
                                break;

                            case 28:
                                levelDisplay.setText("Niveau 2");
                                b2.setVisibility(View.VISIBLE);
                                beatTimeCoolMode = 2;
                                break;

                            case 44:
                                levelDisplay.setText("Niveau 3");
                                beatTimeCoolMode = 2;
                                break;

                            case 60:
                                levelDisplay.setText("Niveau 4");
                                beatTimeCoolMode = 2;
                                break;

                            case 80:
                                levelDisplay.setText("GET READY");
                                wtfMode = true;
                                beatTimeCoolMode = 4;
                                b1.setVisibility(View.GONE);
                                b2.setVisibility(View.GONE);
                                break;

                            case 84: // DROP #2
                                levelDisplay.setText("Niveau 5");
                                b1.setVisibility(View.VISIBLE);
                                b2.setVisibility(View.VISIBLE);
                                b3.setVisibility(View.VISIBLE);
                                beatTimeCoolMode = 2;
                                wtfMode = false;
                                break;

                            case 116:
                                levelDisplay.setText("Niveau 6");
                                beatTimeCoolMode = 2;
                                break;

                            case 148: // Chant
                                levelDisplay.setText("COOL MODE");
                                b1.setVisibility(View.GONE);
                                b2.setVisibility(View.GONE);
                                b3.setVisibility(View.GONE);
                                coolMode = true;
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
                                coolMode = false;
                                b1.setVisibility(View.VISIBLE);
                                b2.setVisibility(View.VISIBLE);
                                b3.setVisibility(View.VISIBLE);
                                break;

                            case 273: // Whaooo
                                levelDisplay.setText("WHAOW");
                                wtfMode = true;
                                beatTimeCoolMode = 4;
                                b1.setVisibility(View.GONE);
                                b2.setVisibility(View.GONE);
                                b3.setVisibility(View.GONE);
                                break;

                            case 277: // DROP #3
                                levelDisplay.setText("Niveau 7");
                                wtfMode = false;
                                beatTimeCoolMode = 2;
                                b1.setVisibility(View.VISIBLE);
                                b2.setVisibility(View.VISIBLE);
                                b3.setVisibility(View.VISIBLE);
                                break;
                        }

                    }
                });
            }
        };
    }

    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    protected void onPause() {
        super.onPause();
        mPlayer.stop();
    }

    private void buttonState(int button) {
        switch(button) {
            case 1:
                buttonStates[0] = true;
                b1.setVisibility(View.GONE);
                break;
            case 2:
                buttonStates[1] = true;
                b2.setVisibility(View.GONE);
                break;
            case 3:
                buttonStates[2] = true;
                b3.setVisibility(View.GONE);
                break;
        }
        scoreDisplay.setText("" + ++score);
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
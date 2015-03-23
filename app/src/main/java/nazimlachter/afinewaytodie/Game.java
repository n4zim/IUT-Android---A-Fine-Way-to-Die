package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;

public class Game extends Activity {

    float bpm = 103;
    long beat = (long) ((float) (60/bpm) * 1000);
    int currentBeat = 0;

    private MediaPlayer mPlayer;

    TextView beatDisplay;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        beatDisplay = (TextView)findViewById(R.id.beatDisplay);
        mPlayer.start();

        startTimer();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }

                Intent intent = new Intent(Game.this, Result.class);
                intent.putExtra("TIME", ""+currentBeat);
                intent.putExtra("SCORE", "100");

                startActivity(intent);
                finish();
            }
        });

    }

    public void startTimer() {
        timer = new Timer();
        initTimer();
        try {
            timer.schedule(timerTask, new Date(), beat);
        } catch (IllegalArgumentException e) {
            Log.e("ERROR", "Illegal argument : " + beat);
        }
    }

    public void initTimer() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        currentBeat++;
                        if(!(((currentBeat-1) % 4) == 0)) {
                            beatDisplay.setText("--- " + ((currentBeat % 4)+1) + " ---"+"\n"+currentBeat);
                        } else {
                            beatDisplay.setText("- - " + ((currentBeat % 4)+1) +" - -"+"\n"+currentBeat);
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
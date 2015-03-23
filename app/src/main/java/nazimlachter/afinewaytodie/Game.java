package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.view.View;

public class Game extends Activity {

    float bpm = 103;
    long beat = (long) ((float) (60/bpm) * 1000);
    int currentBeat = 0;

    private MediaPlayer mPlayer;

    TextView tvTime;
    TextView beatDisplay;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        beatDisplay = (TextView)findViewById(R.id.beatDisplay);
        mPlayer.start();

        startTimer();

        // ------------------------------------------------------------------
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String datas = extras.getString("EXTRA_ID");
            if (datas!= null) { Toast.makeText(getApplication(), datas, Toast.LENGTH_SHORT).show(); }
        }

    }

    public void startTimer() {
        timer = new Timer();
        initTimer();
        try {
            timer.schedule(timerTask, new Date(), beat);
        } catch (IllegalArgumentException e) {
            Log.e("TAGLIFE", "Illegal argument : " + beat);
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

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class Game extends Activity {

    float bpm = 103;
    float beat = 60/bpm;

    private MediaPlayer mPlayer;
    TextView tvTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.start();

        Toast.makeText(getApplication(), "" + beat, Toast.LENGTH_LONG).show();
    }

    public void onDestroy() {
        mPlayer.stop();
        super.onDestroy();
    }

}
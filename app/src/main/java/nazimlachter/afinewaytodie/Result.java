package nazimlachter.afinewaytodie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Result extends Activity {

    TextView tvScore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = (TextView)findViewById(R.id.score);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String score = extras.getString("SCORE");
            if (score != null) { tvScore.setText(score); }
        }

    }

}

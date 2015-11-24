package com.lfk.game2048;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lfk.game2048.Info.UseInfo;
import com.lfk.game2048.Utils.SpUtils;

public class MainActivity extends AppCompatActivity {
    private TextView score;
    private TextView maxScore;
    private GameView g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        g = (GameView) findViewById(R.id.gameview);
        AnimView a = (AnimView) findViewById(R.id.animview);
        g.setAnimView(a);

        score = (TextView) findViewById(R.id.score);
        maxScore = (TextView) findViewById(R.id.max_score);

        Button restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.startGame();
                score.setText("0");
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("REFRESH");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SpUtils.contains(this, "max")) {
            maxScore.setText((String) SpUtils.get(this, "max", "0"));
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "REFRESH":
                    setScore();
                    break;
            }
        }
    };

    private void setScore() {
        score.setText(UseInfo.SCORE + "");
    }
}

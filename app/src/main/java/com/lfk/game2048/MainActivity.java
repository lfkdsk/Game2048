package com.lfk.game2048;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lfk.game2048.Info.UseInfo;
import com.lfk.game2048.Utils.SpUtils;

public class MainActivity extends AppCompatActivity {
    private TextView score;
    private TextView maxScore;
    private GameView g;
    private FrameLayout layout;
    private TextView endText;

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

        initEnd();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("REFRESH");
        intentFilter.addAction("END");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SpUtils.contains(this, "max")) {
            maxScore.setText((int) SpUtils.get(this, "max", 0) + "");
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "REFRESH":
                    setScore();
                    break;
                case "END":
                    gameEnd();
                    break;
            }
        }
    };

    private void setScore() {
        score.setText(UseInfo.SCORE + "");
    }

    private void initEnd() {
        layout = (FrameLayout) View.inflate(this, R.layout.end_frame, null);
        endText = (TextView) layout.findViewById(R.id.end_score);
        Button button = (Button) layout.findViewById(R.id.end_restart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                g.startGame();
                score.setText("0");
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        layout.setLayoutParams(layoutParams);
        layout.setBackgroundColor(getResources().getColor(R.color.white_half));
        addContentView(layout, layoutParams);
        layout.setVisibility(View.GONE);
    }

    private void gameEnd() {
        layout.setVisibility(View.VISIBLE);
        endText.setText(UseInfo.SCORE + "");
        if (SpUtils.contains(this, "max")) {
            if ((int) SpUtils.get(this, "max", 0) < UseInfo.SCORE) {
                SpUtils.put(this, "max", UseInfo.SCORE);
                maxScore.setText(UseInfo.SCORE + "");
            }
        } else {
            SpUtils.put(this, "max", UseInfo.SCORE);
            maxScore.setText(UseInfo.SCORE + "");
        }
    }
}

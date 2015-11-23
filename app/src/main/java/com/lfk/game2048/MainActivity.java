package com.lfk.game2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameView g = (GameView) findViewById(R.id.gameview);
        AnimView a = (AnimView) findViewById(R.id.animview);
//        g.setAnimView(a);
//
//        FrameLayout frameLayout = new FrameLayout(this);
//        LinearLayout.LayoutParams  p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        frameLayout.setLayoutParams(p);
//        frameLayout.setBackgroundColor(getResources().getColor(R.color.white_half));
//
//        addContentView(frameLayout,p);
    }
}

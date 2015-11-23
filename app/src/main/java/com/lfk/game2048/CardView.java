package com.lfk.game2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liufengkai on 15/11/23.
 */
public class CardView extends FrameLayout {
    private RelativeLayout cardView;
    private LayoutInflater layoutInflater;
    private TextView numberTextView;
    private int number;
    private Context context;

    public CardView(Context context) {
        super(context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        initView();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        LayoutParams lp;
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        // 载入布局
        cardView = (RelativeLayout) layoutInflater.inflate(R.layout.card_item, null);
        numberTextView = (TextView) cardView.findViewById(R.id.number);
        GradientDrawable sd = (GradientDrawable) cardView.getBackground();
        sd.setColor(getResources().getColor(R.color.color_item));
        addView(cardView, lp);
        setNumber(0);
    }

    public void setNumber(int numbers) {
        this.number = numbers;
        if (numbers <= 0) {
            numberTextView.setText("");
        } else {
            numberTextView.setText(numbers + "");
        }
        GradientDrawable sd = (GradientDrawable) numberTextView.getBackground();
        switch (numbers) {
            case 0:
                sd.setColor(getResources().getColor(R.color.color_0));
                break;
            case 2:
                sd.setColor(getResources().getColor(R.color.color_2));
                break;
            case 4:
                sd.setColor(getResources().getColor(R.color.color_4));
                break;
            case 8:
                sd.setColor(getResources().getColor(R.color.color_8));
                break;
            case 16:
                sd.setColor(getResources().getColor(R.color.color_16));
                break;
            case 32:
                sd.setColor(getResources().getColor(R.color.color_32));
                break;
            case 64:
                sd.setColor(getResources().getColor(R.color.color_64));
                break;
            case 128:
                sd.setColor(getResources().getColor(R.color.color_128));
                break;
            case 256:
                sd.setColor(getResources().getColor(R.color.color_256));
                break;
            case 512:
                sd.setColor(getResources().getColor(R.color.color_512));
                break;
            case 1024:
                sd.setColor(getResources().getColor(R.color.color_1024));
                break;
            case 2048:
                sd.setColor(getResources().getColor(R.color.color_2048));
                break;
        }
    }

    public TextView getNumberTextView() {
        return numberTextView;
    }

    public int getNumber() {
        return number;
    }

    public boolean equals(int number) {
        return getNumber() == number;
    }
}

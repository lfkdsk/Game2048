package com.lfk.game2048;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufengkai on 15/11/23.
 */
public class GameView extends LinearLayout implements View.OnTouchListener {
    // 初始 / 偏移
    private float startX, startY, offsetX, offsetY;
    private final int OFFSET_MAX = 5;
    private Context context;
    private CardView[][] cardViews = new CardView[UseInfo.LINES][UseInfo.LINES];
    private List<Point> cardPoints = new ArrayList<>();
    private AnimView animView;

    public GameView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.color_background));
        setOnTouchListener(this);
        animView = new AnimView(context);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    if (offsetX < -OFFSET_MAX) {
                        swipeLeft();
                    } else if (offsetX > OFFSET_MAX) {
                        swipeRight();
                    }
                } else {
                    if (offsetY < -OFFSET_MAX) {
                        swipeTop();
                    } else if (offsetY > OFFSET_MAX) {
                        swipeBottom();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        UseInfo.CARD_WIDTH = UseInfo.CARD_HEIGHT
                = (Math.min(w, h) - 10) / UseInfo.LINES;
        addCards(UseInfo.CARD_WIDTH, UseInfo.CARD_HEIGHT);
        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {
        CardView cardView;
        LinearLayout line;
        LinearLayout.LayoutParams lineP;
        // add views
        for (int y = 0; y < UseInfo.LINES; y++) {
            // add LinearLayout
            line = new LinearLayout(context);
            lineP = new LinearLayout.LayoutParams(-1, cardHeight);
            addView(line, lineP);
            // 默认添加数字为0的块
            for (int x = 0; x < UseInfo.LINES; x++) {
                cardView = new CardView(context);
                line.addView(cardView, cardWidth, cardHeight);
                cardViews[x][y] = cardView;
            }
        }
    }

    private void startGame() {
        for (int y = 0; y < UseInfo.LINES; y++) {
            for (int x = 0; x < UseInfo.LINES; x++) {
                cardViews[x][y].setNumber(0);
            }
        }
        addRandom(2);
    }

    private void addRandom(int num) {
        for (int i = 0; i < num; i++) {
            cardPoints.clear();
            for (int y = 0; y < UseInfo.LINES; y++) {
                for (int x = 0; x < UseInfo.LINES; x++) {
                    if (cardViews[x][y].getNumber() <= 0) {
                        cardPoints.add(new Point(x, y));
                    }
                }
            }
            if (cardPoints.size() > 0) {
                Point p = cardPoints.remove((int) (Math.random() * cardPoints.size()));
                cardViews[p.x][p.y].setNumber(Math.random() > 0.5 ? 2 : 4);
                animView.createCardAnim(cardViews[p.x][p.y]);
            }
        }
    }

    private void swipeLeft() {
        boolean findIt = false;
        for (int y = 0; y < UseInfo.LINES; y++) {
            for (int x = 0; x < UseInfo.LINES; x++) {
                for (int x_next = x + 1; x_next < UseInfo.LINES; x_next++) {
                    if (cardViews[x_next][y].getNumber() > 0) {
                        if (cardViews[x][y].getNumber() <= 0) {
                            // 从x_next to x 所以应该是from进行调用
                            animView.translateToCardAnim(
                                    cardViews[x_next][y],
                                    cardViews[x][y],
                                    x_next, y,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x_next][y].getNumber());
                            cardViews[x_next][y].setNumber(0);
                            x--;
                            findIt = true;
                        } else if (cardViews[x][y].equals(cardViews[x_next][y].getNumber())) {
                            animView.translateToCardAnim(cardViews[x_next][y],
                                    cardViews[x][y],
                                    x_next, y,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x_next][y].getNumber() * 2);
                            cardViews[x_next][y].setNumber(0);
                            findIt = true;
                        }
                        break;
                    }
                }
            }
        }
        if (findIt) {
            addRandom(1);
        }
    }


    private void swipeRight() {
        boolean findIt = false;
        for (int y = 0; y < UseInfo.LINES; y++) {
            for (int x = UseInfo.LINES - 1; x >= 0; x--) {
                for (int x_next = x - 1; x_next >= 0; x_next--) {
                    if (cardViews[x_next][y].getNumber() > 0) {
                        if (cardViews[x][y].getNumber() <= 0) {
                            animView.translateToCardAnim(
                                    cardViews[x_next][y],
                                    cardViews[x][y],
                                    x_next, y,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x_next][y].getNumber());
                            cardViews[x_next][y].setNumber(0);
                            x++;
                            findIt = true;
                        } else if (cardViews[x][y].equals(cardViews[x_next][y].getNumber())) {
                            animView.translateToCardAnim(
                                    cardViews[x_next][y],
                                    cardViews[x][y],
                                    x_next, y,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x_next][y].getNumber() * 2);
                            cardViews[x_next][y].setNumber(0);
                            findIt = true;
                        }
                        break;
                    }
                }
            }
        }
        if (findIt) {
            addRandom(1);
        }
    }

    private void swipeTop() {
        boolean findIt = false;
        for (int x = 0; x < UseInfo.LINES; x++) {
            for (int y = 0; y < UseInfo.LINES; y++) {
                for (int y_next = y + 1; y_next < UseInfo.LINES; y_next++) {
                    if (cardViews[x][y_next].getNumber() > 0) {
                        if (cardViews[x][y].getNumber() <= 0) {
                            animView.translateToCardAnim(
                                    cardViews[x][y_next],
                                    cardViews[x][y],
                                    x, y_next,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x][y_next].getNumber());
                            cardViews[x][y_next].setNumber(0);
                            y--;
                            findIt = true;
                        } else if (cardViews[x][y].equals(cardViews[x][y_next].getNumber())) {
                            animView.translateToCardAnim(
                                    cardViews[x][y_next],
                                    cardViews[x][y],
                                    x, y_next,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x][y_next].getNumber() * 2);
                            cardViews[x][y_next].setNumber(0);
                            findIt = true;
                        }
                        break;
                    }
                }
            }
        }
        if (findIt) {
            addRandom(1);
        }
    }

    private void swipeBottom() {
        boolean findIt = false;
        for (int x = 0; x < UseInfo.LINES; x++) {
            for (int y = UseInfo.LINES - 1; y >= 0; y--) {
                for (int y_next = y - 1; y_next >= 0; y_next--) {
                    if (cardViews[x][y_next].getNumber() > 0) {
                        if (cardViews[x][y].getNumber() <= 0) {
                            animView.translateToCardAnim(
                                    cardViews[x][y_next],
                                    cardViews[x][y],
                                    x, y_next,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x][y_next].getNumber());
                            cardViews[x][y_next].setNumber(0);
                            y++;
                            findIt = true;
                        } else if (cardViews[x][y].equals(cardViews[x][y_next].getNumber())) {
                            animView.translateToCardAnim(
                                    cardViews[x][y_next],
                                    cardViews[x][y],
                                    x, y_next,
                                    x, y);
                            cardViews[x][y].setNumber(cardViews[x][y_next].getNumber() * 2);
                            cardViews[x][y_next].setNumber(0);
                            findIt = true;
                        }
                        break;
                    }
                }
            }
        }
        if (findIt) {
            addRandom(1);
        }
    }

}

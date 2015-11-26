package com.lfk.game2048;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lfk.game2048.Info.UseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufengkai on 15/11/23.
 */
public class GameView extends LinearLayout implements View.OnTouchListener {
    // 初始 / 偏移
    private float startX, startY, offsetX, offsetY;
    // 手指移动阀值
    private final int OFFSET_MAX = 5;
    private Context context;
    // 存储所有的卡片
    private CardView[][] cardViews = new CardView[UseInfo.LINES][UseInfo.LINES];
    // 用于标注空的卡片位置
    private List<Point> cardPoints = new ArrayList<>();
    // 用于展示动画
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
    }


    public void setAnimView(AnimView animView) {
        this.animView = animView;
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

    /**
     * 测量尺寸
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        UseInfo.CARD_WIDTH = UseInfo.CARD_HEIGHT
//                = (Math.min(w, h) - 10) / UseInfo.LINES;
        addCards(UseInfo.CARD_WIDTH, UseInfo.CARD_HEIGHT);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w,
//                UseInfo.CARD_WIDTH * UseInfo.LINES + 10);
//        this.setLayoutParams(params);
//        animView.setLayoutParams(params);
        startGame();
    }

    /**
     * 添加卡片
     *
     * @param cardWidth
     * @param cardHeight
     */
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

    /**
     * 开始游戏
     */
    public void startGame() {
        for (int y = 0; y < UseInfo.LINES; y++) {
            for (int x = 0; x < UseInfo.LINES; x++) {
                cardViews[x][y].setNumber(0);
            }
        }
        addRandom(2);
        UseInfo.SCORE = 0;
    }

    /**
     * 随机生成卡片
     *
     * @param num 生成数量
     */
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

    /**
     * 四个方向的手势
     */
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
                            addScore(cardViews[x_next][y].getNumber());
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
            complete();
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
                            addScore(cardViews[x_next][y].getNumber());
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
            complete();
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
                            addScore(cardViews[x][y_next].getNumber());
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
            complete();
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
                            addScore(cardViews[x][y_next].getNumber());
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
            complete();
        }
    }

    /**
     * 计算空间大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int w = manager.getDefaultDisplay().getHeight();
        int h = manager.getDefaultDisplay().getWidth();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.gameBar);
        UseInfo.CARD_WIDTH = UseInfo.CARD_HEIGHT
                = (Math.min(w, h) - layout.getHeight() - 10) / UseInfo.LINES;
        setMeasuredDimension(UseInfo.CARD_WIDTH * UseInfo.LINES + 10,
                UseInfo.CARD_WIDTH * UseInfo.LINES+10);
    }

    /**
     * 用广播更新分数
     *
     * @param num
     */
    private void addScore(int num) {
        UseInfo.addScore(num);
        Intent intent = new Intent();
        intent.setAction("REFRESH");
        context.sendBroadcast(intent);
    }

    private void complete() {
        boolean complete = true;

        for (int y = 0; y < UseInfo.LINES; y++) {
            for (int x = 0; x < UseInfo.LINES; x++) {
                if ((cardViews[x][y].getNumber() == 0) ||
                        (x > 0 && cardViews[x][y].equals(cardViews[x - 1][y])) ||
                        (x < UseInfo.LINES - 1 && cardViews[x][y].equals(cardViews[x + 1][y])) ||
                        (y > 0 && cardViews[x][y].equals(cardViews[x][y - 1])) ||
                        (y < UseInfo.LINES - 1 && cardViews[x][y].equals(cardViews[x][y + 1]))) {
                    complete = false;
                }
            }
        }

        if (complete) {
            Intent intent = new Intent();
            intent.setAction("END");
            context.sendBroadcast(intent);
        }
    }
}

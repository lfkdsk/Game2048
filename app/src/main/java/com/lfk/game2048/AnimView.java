package com.lfk.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.lfk.game2048.Info.UseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufengkai on 15/11/23.
 */
public class AnimView extends FrameLayout {
    private List<CardView> usedCardView = new ArrayList<>();

    public AnimView(Context context) {
        super(context);
    }

    public void createCardAnim(CardView card) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        card.startAnimation(scaleAnimation);
    }

    public AnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void translateToCardAnim(final CardView from, final CardView to, int fromX, int fromY, int toX, int toY) {
        final CardView cardView = getCardView(from.getNumber());

        LayoutParams lp = new LayoutParams(UseInfo.CARD_WIDTH, UseInfo.CARD_HEIGHT);
        lp.leftMargin = fromX * UseInfo.CARD_WIDTH;
        lp.topMargin = fromY * UseInfo.CARD_HEIGHT;
        cardView.setLayoutParams(lp);

        if (to.getNumber() <= 0) {
            to.getNumberTextView().setVisibility(View.INVISIBLE);
        }

        TranslateAnimation translateAnimation =
                new TranslateAnimation(0,
                        UseInfo.CARD_WIDTH * (toX - fromX),
                        0,
                        UseInfo.CARD_HEIGHT * (toY - fromY));

        translateAnimation.setDuration(100);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                to.getNumberTextView().setVisibility(VISIBLE);
                recycleCard(cardView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cardView.startAnimation(translateAnimation);
    }

    private void recycleCard(CardView card) {
        card.setVisibility(View.INVISIBLE);
        card.setAnimation(null);
        usedCardView.add(card);
    }

    private CardView getCardView(int number) {
        CardView card;
        if (!usedCardView.isEmpty()) {
            card = usedCardView.remove(0);
        } else {
            card = new CardView(getContext());
            addView(card);
        }
        card.setVisibility(View.VISIBLE);
        card.setNumber(number);
        return card;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(UseInfo.CARD_WIDTH * UseInfo.LINES + 20,
                UseInfo.CARD_WIDTH * UseInfo.LINES + 20);
    }
}

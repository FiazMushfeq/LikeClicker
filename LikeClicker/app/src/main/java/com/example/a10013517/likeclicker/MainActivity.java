package com.example.a10013517.likeclicker;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    static ConstraintLayout constraintLayout;
    static ConstraintSet constraintSet;

    static TextView getDisplayLikeCounter;
    static TextView getDisplayCPS;
    static ImageView getDisplayLikeImage;
    static TextView getUpgrade;
    static TextView getCurrent;
    static ImageView finger;

    static AtomicInteger counter = new AtomicInteger(0); // Need to make Thread Safe --> Atomic Integer
    static AtomicInteger cps = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.layout);

        // Source: https://stackoverflow.com/questions/36894384/android-move-background-continuously-with-animation
        final ImageView backgroundOne = findViewById(R.id.backgroundOne);
        final ImageView backgroundTwo = findViewById(R.id.backgroundTwo);

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(10000L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                final float progress = (float) animator.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float moveX = width * progress;
                backgroundOne.setTranslationX(moveX);
                backgroundTwo.setTranslationX(moveX - width);
            }
        });
        valueAnimator.start();

        getDisplayLikeCounter = findViewById(R.id.displayCounter);
        getDisplayCPS = findViewById(R.id.displayCPS);
        getDisplayLikeImage = findViewById(R.id.displayLike);
        getUpgrade = findViewById(R.id.upgrade);
        getCurrent = findViewById(R.id.currentUpgrade);

        getDisplayLikeCounter.setText("Likes: " + counter);
        getDisplayLikeCounter.setTextColor(Color.MAGENTA);
        getDisplayLikeCounter.setTextSize(50);

        getDisplayCPS.setText(cps.get() + " Like(s) Per Second");
        getDisplayCPS.setTextColor(Color.MAGENTA);
        getDisplayCPS.setTextSize(30);

        getDisplayLikeImage.setImageResource(R.drawable.like);
        getDisplayLikeImage.getLayoutParams().height = 200;
        getDisplayLikeImage.getLayoutParams().width = 200;

        getUpgrade.setText("Extra Finger for 10");
        getUpgrade.setTextColor(Color.MAGENTA);
        getUpgrade.setTextSize(20);

        getCurrent.setText("Current Upgrades(s)");
        getCurrent.setTextColor(Color.MAGENTA);
        getCurrent.setTextSize(20);

        finger = new ImageView(MainActivity.this);
        finger.setId(View.generateViewId());
        finger.setImageResource(R.drawable.finger);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        finger.setLayoutParams(params);
        finger.getLayoutParams().width = 100;
        finger.getLayoutParams().height = 100;
        finger.setColorFilter(Color.DKGRAY);
        finger.setVisibility(View.INVISIBLE);

        constraintLayout.addView(finger);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(finger.getId(), ConstraintSet.TOP, getUpgrade.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(finger.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(finger.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraintSet.connect(finger.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        constraintSet.setVerticalBias(finger.getId(), 0.2f);
        constraintSet.setHorizontalBias(finger.getId(), 0.2f);
        constraintSet.applyTo(constraintLayout);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);

        getDisplayLikeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                view.startAnimation(scaleAnimation);
                counter.getAndIncrement();
                getDisplayLikeCounter.setText("Likes: " + counter);

                final TextView plusOne;
                plusOne = new TextView(MainActivity.this);
                plusOne.setId(View.generateViewId());
                plusOne.setText("+1");
                plusOne.setTextColor(Color.MAGENTA);
                final ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                plusOne.setLayoutParams(params);

                constraintLayout.addView(plusOne);
                final ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(plusOne.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                constraintSet.connect(plusOne.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                constraintSet.connect(plusOne.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                constraintSet.connect(plusOne.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
                constraintSet.setVerticalBias(plusOne.getId(), 0.5f);
                constraintSet.setHorizontalBias(plusOne.getId(), 0.5f);
                constraintSet.applyTo(constraintLayout);

                int rand = (int)(Math.random() * 140) - 70;

                final TranslateAnimation translateAnimation = new TranslateAnimation(rand, rand, -90, -150);
                translateAnimation.setDuration(700);
                translateAnimation.setFillAfter(true);
                plusOne.startAnimation(translateAnimation);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        constraintLayout.removeView(plusOne);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                new fingerThread().start();
            }
        });

        Thread passiveThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while(true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                counter.getAndAdd(cps.get());
                                getDisplayLikeCounter.setText("Likes: " + counter.get());
                                new fingerThread().start();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        passiveThread.start();
    }

    public class fingerThread extends Thread {
        @Override
        public void run() {
            super.run();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(finger.getVisibility() == View.INVISIBLE)
                        finger.setClickable(false);

                    if(counter.get() >= 10) {
                        if(finger.getVisibility() == View.INVISIBLE) {
                            finger.setVisibility(View.INVISIBLE);
                            final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                            fadeIn.setDuration(500);
                            fadeIn.setFillAfter(true);
                            finger.setAnimation(fadeIn);
                            finger.setVisibility(View.VISIBLE);
                            finger.setClickable(true);
                        }

                        if (finger.getVisibility() == View.VISIBLE) {
                            finger.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    counter.set(counter.get() - 10);
                                    getDisplayLikeCounter.setText("Likes: " + counter.get());

                                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                                    fadeOut.setDuration(500);
                                    fadeOut.setFillAfter(true);
                                    finger.setAnimation(fadeOut);
                                    finger.setVisibility(View.INVISIBLE);
                                    if(counter.get() < 10)
                                        finger.setClickable(false);

                                    if(counter.get() >= 10) {
                                        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                                        fadeIn.setDuration(0);
                                        fadeIn.setFillAfter(true);
                                        finger.setAnimation(fadeIn);
                                        finger.setVisibility(View.VISIBLE);
                                    }

                                    ImageView copyfinger2 = new ImageView(MainActivity.this);
                                    copyfinger2.setId(View.generateViewId());
                                    copyfinger2.setImageResource(R.drawable.finger);
                                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                                    copyfinger2.setLayoutParams(params);
                                    copyfinger2.getLayoutParams().width = 100;
                                    copyfinger2.getLayoutParams().height = 100;
                                    copyfinger2.setColorFilter(Color.DKGRAY);

                                    constraintLayout.addView(copyfinger2);
                                    ConstraintSet constraintSet = new ConstraintSet();
                                    constraintSet.clone(constraintLayout);
                                    constraintSet.connect(copyfinger2.getId(), ConstraintSet.TOP, getCurrent.getId(), ConstraintSet.BOTTOM);
                                    constraintSet.connect(copyfinger2.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                    constraintSet.connect(copyfinger2.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                    constraintSet.connect(copyfinger2.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
                                    constraintSet.setVerticalBias(copyfinger2.getId(), 0.2f);
                                    float rand = (float)(Math.random() * 40) + 60;
                                    rand /= 100;
                                    constraintSet.setHorizontalBias(copyfinger2.getId(), rand);
                                    constraintSet.applyTo(constraintLayout);

                                    cps.getAndIncrement();
                                    getDisplayCPS.setText(cps.get() + " Like(s) Per Second");
                                }
                            });
                        }
                    }
                }
            });
        }
    }
}

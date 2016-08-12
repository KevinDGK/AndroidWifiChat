package com.dgk.wifichat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgk.wifichat.R;


/**
 * Created by SUMMER on 2016/8/5.
 */
public class LoadingRelativeLayout extends RelativeLayout {

    ImageView ivLoading;
    TextView tvLoading;
    private int picture;
    private int textColor;

    public LoadingRelativeLayout(Context context) {
        this(context, null);
    }

    public LoadingRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingRelativeLayout);
        picture = a.getResourceId(R.styleable.LoadingRelativeLayout_picture, R.mipmap.loading_green);
        textColor = a.getColor(R.styleable.LoadingRelativeLayout_textColor, context.getResources().getColor(R.color.color_start_bg));
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.loading_relativelayout, this);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvLoading = (TextView) findViewById(R.id.tv_loading);

        ivLoading.setImageResource(picture);
        tvLoading.setTextColor(textColor);

        RotateAnimation
                rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        ivLoading.setAnimation(rotateAnimation);
        rotateAnimation.start();
    }
}

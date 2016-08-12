package com.dgk.wifichat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgk.wifichat.R;


/**
 * 自定义旋转的Loading...
 *   一张旋转的图片，下面是文字说明。
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

    /**
     * 自定义view，在xml中使用的时候调用的是三个参数的构造方法
     * 在这个方法中可以获得xml中付给该节点的属性和属性值。
     * @param context
     * @param attrs  XML中该view节点下发下的所有的属性的集合
     * @param defStyleAttr
     */
    public LoadingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //1. 从属性集合中取出我们感兴趣的属性，即我们自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingRelativeLayout);

        //2. 取出所有的value值对应的资源
        // 如果是个资源的引用，可以获得该属性值对应的资源的id，第二个参数表示如果没有获取到任何的id就会赋的默认值
        picture = a.getResourceId(R.styleable.LoadingRelativeLayout_picture, R.mipmap.loading_green);
        // 如果value是个颜色，那么就可以获得该属性对应的颜色值，第二个参数表示如果没有获取到任何的颜色就会赋的默认值
        // 注意：获取颜色的方法context.getResource.getColor在api23的时候就过时了，暂时可以使用下面这个兼容的方法
        textColor = a.getColor(R.styleable.LoadingRelativeLayout_textColor, ContextCompat.getColor(context,R.color.color_loading_text));

        //3. 初始化界面
        init(context);
    }

    private void init(Context context) {

        //1. 填充view，然后传给根布局
        View.inflate(context, R.layout.loading_relativelayout, this);
        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        tvLoading = (TextView) findViewById(R.id.tv_loading);

        //2. 给界面上的图片和文字赋值
        ivLoading.setImageResource(picture);
        tvLoading.setTextColor(textColor);

        //3. 给图片设置旋转的属性动画
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

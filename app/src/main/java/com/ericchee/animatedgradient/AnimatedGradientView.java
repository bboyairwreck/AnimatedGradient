package com.ericchee.animatedgradient;

import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedGradientView extends View {
    private static final int DURATION_MILLIS = 1500;
    private static final float MID_COLOR_PERCENTAGE_SPREAD = 1.0f;

    public AnimatedGradientView(Context context) {
        super(context);
        initLayout();
    }

    public AnimatedGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public AnimatedGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        final ValueAnimator animator = TimeAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(DURATION_MILLIS);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                Drawable gradientDrawable = createGradientDrawable(valueAnimator.getAnimatedFraction());
                setBackground(gradientDrawable);
            }
        });
        animator.start();
    }

    private Drawable createGradientDrawable(float faction) {
        final int baseColor = ContextCompat.getColor(getContext(), R.color.gray);
        final int midColor = ContextCompat.getColor(getContext(), R.color.white);
        final int[] colors = new int[]{baseColor, midColor, baseColor};

        final float speedRate = 1f + (MID_COLOR_PERCENTAGE_SPREAD * 2);
        final float centerPercentage = faction*speedRate;

        final float startColorPosition = (centerPercentage - (2*MID_COLOR_PERCENTAGE_SPREAD));
        final float midColorPosition = centerPercentage - MID_COLOR_PERCENTAGE_SPREAD;
        final float endColorPosition = centerPercentage;

        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                float[] centerLocation = new float[]{startColorPosition, midColorPosition, endColorPosition};
                return new LinearGradient(0, height/2, width, height/2,
                        colors,
                        centerLocation,
                        Shader.TileMode.CLAMP);
            }
        };
        final PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());
        paintDrawable.setShaderFactory(shaderFactory);
        paintDrawable.setCornerRadius(Util.convertDpToPixel(5, getContext()));

        return paintDrawable;
    }
}

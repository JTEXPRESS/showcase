package com.jnt.showcase;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

public class GuideMessageView extends LinearLayout {
    private final RectF rectF;
    private final Paint paint;
    private final LinearLayout containerBody;
    private final LinearLayout childContainerBody;
    private final LinearLayout childContainerClose;
    private final LinearLayout containerButton;
    private final TextView title;
    private final TextView content;
    private final ImageView close;
    private final Button previous;
    private final Button next;
    private GuideListener closeListener;
    private GuideListener previousListener;
    private GuideListener nextListener;

    public GuideMessageView(Context context) {
        super(context);
        setWillNotDraw(false);
        setOrientation(VERTICAL);

        RelativeLayout.LayoutParams layoutParamsClose               = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParamsButton                             = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParamsContainerButton                    = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

        rectF               = new RectF();
        paint               = new Paint(Paint.ANTI_ALIAS_FLAG);
        containerBody       = new LinearLayout(context);
        childContainerBody  = new LinearLayout(context);
        childContainerClose = new LinearLayout(context);
        containerButton     = new LinearLayout(context);
        title               = new TextView(context);
        content             = new TextView(context);
        close               = new ImageView(context);
        previous            = new Button(context);
        next                = new Button(context);

        layoutParamsClose.setMargins(GuideUtils.getBaseDimen(context), 0, 0,0);
        layoutParamsButton.setMargins(GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context));
        layoutParamsContainerButton.setMargins(GuideUtils.getTightDimen(context), GuideUtils.getNoneDimen(context), GuideUtils.getTightDimen(context), GuideUtils.getTightDimen(context));
        paint.setStrokeCap(Paint.Cap.ROUND);

        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);

        content.setTextColor(Color.BLACK);
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        childContainerBody.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
        childContainerBody.setOrientation(VERTICAL);
        childContainerBody.addView(title);
        childContainerBody.addView(content);

        close.setImageResource(R.drawable.ic_close);
        close.setOnClickListener(v -> {
            if (closeListener != null) closeListener.onClick(v);
        });

        childContainerClose.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
        childContainerClose.addView(close, layoutParamsClose);

        containerBody.setPadding(GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context));
        containerBody.addView(childContainerBody);
        containerBody.addView(childContainerClose);

        previous.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        previous.setGravity(Gravity.CENTER);
        previous.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        previous.setTypeface(previous.getTypeface(), Typeface.BOLD);
        previous.setOnClickListener(v -> {
            if (previousListener != null) previousListener.onClick(v);
        });

        next.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        next.setGravity(Gravity.CENTER);
        next.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        next.setTypeface(next.getTypeface(), Typeface.BOLD);
        next.setOnClickListener(v -> {
            if (nextListener != null) nextListener.onClick(v);
        });

        containerButton.setOrientation(HORIZONTAL);
        containerButton.setGravity(Gravity.RIGHT);
        containerButton.addView(previous, layoutParamsButton);
        containerButton.addView(next, layoutParamsButton);

        addView(containerBody);
        addView(containerButton, layoutParamsContainerButton);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawRoundRect(rectF, GuideUtils.getTighterDimen(getContext()), GuideUtils.getTighterDimen(getContext()), paint);
    }

    public void onClickCloseListener(GuideListener listener) { this.closeListener = listener; }

    public void onClickPreviousListener(GuideListener listener) { this.previousListener = listener; }

    public void onClickNextListener(GuideListener listener) { this.nextListener = listener; }

    public void title(String title) {
        this.title.setText(title);
        this.title.setVisibility(this.title.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void content(String content) {
        this.content.setText(content);
        this.content.setVisibility(this.content.getText().toString().isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void previous(String previous) { this.previous.setText(previous); }

    public void next(String next) { this.next.setText(next); }

    public void titleTypeface(Typeface typeface) { this.title.setTypeface(typeface); }

    public void contentTypeface(Typeface typeface) { this.content.setTypeface(typeface); }

    public void previousTypeface(Typeface typeface) { this.previous.setTypeface(typeface); }

    public void nextTypeface(Typeface typeface) { this.next.setTypeface(typeface); }

    public void backgroundColor(int color) {
        paint.setAlpha(255);
        paint.setColor(color);

        invalidate();
    }

    public void previousBackgroundColor(int color) { this.previous.setBackgroundTintList(ColorStateList.valueOf(color)); }

    public void nextBackgroundColor(int color) { this.next.setBackgroundTintList(ColorStateList.valueOf(color)); }

    public void titleTextColor(int color) { this.title.setTextColor(color); }

    public void contentTextColor(int color) { this.content.setTextColor(color); }

    public void previousTextColor(int color) { this.previous.setTextColor(color); }

    public void nextTextColor(int color) { this.next.setTextColor(color); }

    public void closeImageColor(int color) { this.close.setImageTintList(ColorStateList.valueOf(color)); }

    public void titleTextSize(int size) { this.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void contentTextSize(int size) { this.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void previousTextSize(int size) { this.previous.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void nextTextSize(int size) { this.previous.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void previousVisibility(int visibility) { this.previous.setVisibility(visibility); }

    public void tooltip() {
        title.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        containerButton.setVisibility(GONE);
    }
}

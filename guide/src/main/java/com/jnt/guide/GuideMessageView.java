package com.jnt.guide;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class GuideMessageView extends LinearLayout {
    private final RectF rectF;
    private final Paint paint;
    private final LinearLayout containerTitle;
    private final LinearLayout containerBody;
    private final LinearLayout childContainerBody;
    private final LinearLayout childContainerClose;
    private final LinearLayout containerButton;
    private final CardView cardRecent;
    private final TextView recent;
    private final TextView title;
    private final TextView content;
    private final TextView previous;
    private final TextView next;
    private final ImageView close;
    private GuideListener closeListener;
    private GuideListener previousListener;
    private GuideListener nextListener;

    public GuideMessageView(Context context) {
        super(context);
        setWillNotDraw(false);
        setOrientation(VERTICAL);

        RelativeLayout.LayoutParams layoutParamsClose   = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParamsRecent                 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParamsButton                 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParamsContainerButton        = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

        rectF               = new RectF();
        paint               = new Paint(Paint.ANTI_ALIAS_FLAG);
        containerTitle      = new LinearLayout(context);
        containerBody       = new LinearLayout(context);
        childContainerBody  = new LinearLayout(context);
        childContainerClose = new LinearLayout(context);
        containerButton     = new LinearLayout(context);
        cardRecent          = new CardView(context);
        recent              = new TextView(context);
        title               = new TextView(context);
        content             = new TextView(context);
        previous            = new TextView(context);
        next                = new TextView(context);
        close               = new ImageView(context);

        layoutParamsClose.setMargins(GuideUtils.getBaseDimen(context), GuideUtils.getNoneDimen(context), GuideUtils.getNoneDimen(context),GuideUtils.getNoneDimen(context));
        layoutParamsRecent.setMargins(GuideUtils.getNoneDimen(context), GuideUtils.getNoneDimen(context), GuideUtils.getTighterDimen(context), GuideUtils.getNoneDimen(context));
        layoutParamsButton.setMargins(GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context), GuideUtils.getTightestDimen(context));
        layoutParamsContainerButton.setMargins(GuideUtils.getTightDimen(context), GuideUtils.getNoneDimen(context), GuideUtils.getTightDimen(context), GuideUtils.getTightDimen(context));
        paint.setStrokeCap(Paint.Cap.ROUND);

        recent.setPadding(GuideUtils.getTightestDimen(context), GuideUtils.getExtraTightDimen(context), GuideUtils.getTightestDimen(context), GuideUtils.getExtraTightDimen(context));
        recent.setAllCaps(true);
        recent.setTextColor(Color.WHITE);
        recent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        recent.setTypeface(recent.getTypeface(), Typeface.BOLD);

        cardRecent.setRadius(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, context.getResources().getDisplayMetrics()));
        cardRecent.setCardElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, context.getResources().getDisplayMetrics()));
        cardRecent.setCardBackgroundColor(Color.BLACK);
        cardRecent.addView(recent);

        title.setTextColor(Color.BLACK);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);

        containerTitle.setGravity(Gravity.CENTER_VERTICAL);
        containerTitle.addView(cardRecent, layoutParamsRecent);
        containerTitle.addView(title);

        content.setTextColor(Color.BLACK);
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        childContainerBody.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
        childContainerBody.setOrientation(VERTICAL);
        childContainerBody.addView(containerTitle);
        childContainerBody.addView(content);

        close.setImageResource(R.drawable.ic_close);
        close.setOnClickListener(v -> {
            if (closeListener != null) closeListener.onClick(v);
        });

        childContainerClose.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));
        childContainerClose.addView(close, layoutParamsClose);

        containerBody.setPadding(GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getBaseDimen(context));
        containerBody.addView(childContainerBody);
        containerBody.addView(childContainerClose);

        previous.setPadding(GuideUtils.getBaseDimen(context), GuideUtils.getTighterDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getTighterDimen(context));
        previous.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        previous.setGravity(Gravity.CENTER);
        previous.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle));
        previous.setTypeface(previous.getTypeface(), Typeface.BOLD);
        previous.setOnClickListener(v -> {
            if (previousListener != null) previousListener.onClick(v);
        });

        next.setPadding(GuideUtils.getBaseDimen(context), GuideUtils.getTighterDimen(context), GuideUtils.getBaseDimen(context), GuideUtils.getTighterDimen(context));
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

    public void recent(String recent) { this.recent.setText(recent); }

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

    public void recentTypeface(Typeface typeface) { this.recent.setTypeface(typeface); }

    public void titleTypeface(Typeface typeface) { this.title.setTypeface(typeface); }

    public void contentTypeface(Typeface typeface) { this.content.setTypeface(typeface); }

    public void previousTypeface(Typeface typeface) { this.previous.setTypeface(typeface); }

    public void nextTypeface(Typeface typeface) { this.next.setTypeface(typeface); }

    public void backgroundColor(int color) {
        paint.setAlpha(255);
        paint.setColor(color);
        invalidate();
    }

    public void recentBackgroundColor(int color) { this.cardRecent.setCardBackgroundColor(color); }

    public void previousBackgroundColor(int color) { this.previous.setBackgroundTintList(ColorStateList.valueOf(color)); }

    public void nextBackgroundColor(int color) { this.next.setBackgroundTintList(ColorStateList.valueOf(color)); }

    public void recentTextColor(int color) { this.recent.setTextColor(color); }

    public void titleTextColor(int color) { this.title.setTextColor(color); }

    public void contentTextColor(int color) { this.content.setTextColor(color); }

    public void previousTextColor(int color) { this.previous.setTextColor(color); }

    public void nextTextColor(int color) { this.next.setTextColor(color); }

    public void closeImageColor(int color) { this.close.setImageTintList(ColorStateList.valueOf(color)); }

    public void recentTextSize(int size) { this.recent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);}

    public void titleTextSize(int size) { this.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void contentTextSize(int size) { this.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void previousTextSize(int size) { this.previous.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void nextTextSize(int size) { this.previous.setTextSize(TypedValue.COMPLEX_UNIT_SP, size); }

    public void recentVisibility(int visibility) { this.cardRecent.setVisibility(visibility); }

    public void previousVisibility(int visibility) { this.previous.setVisibility(visibility); }

    public void tooltip() {
        title.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        cardRecent.setVisibility(View.GONE);
        containerButton.setVisibility(View.GONE);
    }
}

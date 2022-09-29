package com.jnt.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

@SuppressLint("ViewConstructor")
public class GuideView extends FrameLayout {
    private static final int TIMER                  = 3000;
    private static final int BACKGROUND_COLOR       = 0x99000000;
    private static final int BACKGROUND_TRANSPARENT = 0x00FF0000;

    private final Paint selfPaint           = new Paint();
    private final Paint paintLine           = new Paint();
    private final Paint targetPaint         = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Xfermode X_FER_MODE_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private final GuideData data;

    private GuideMessageView messageView;
    private GuideListener listener;
    private GuideType type;
    private RectF targetRect;
    private Rect selfRect;
    private String previous;
    private String next;
    private String finish;
    private int position;
    private int yMessageView;
    private float indicatorHeightGuide;
    private boolean isTop;

    private GuideView(Context context, GuideData data) {
        super(context);
        this.data = data;

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setClickable(false);
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        initView();
        initListener();
        setTarget();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (data.getTarget(position) != null) {
            Path path                           = new Path();
            CornerPathEffect cornerPathEffect   = new CornerPathEffect(8);
            int triangleSize                    = GuideUtils.getBaseDimen(getContext());
            float x                             = (targetRect.left / 2 + targetRect.right / 2);
            float y                             = isTop ? targetRect.bottom : targetRect.top;

            canvas.drawRect(selfRect, selfPaint);
            paintLine.setStyle(Paint.Style.FILL);
            paintLine.setStrokeWidth(GuideUtils.getTightestDimen(getContext()));
            paintLine.setAntiAlias(true);
            path.setFillType(Path.FillType.EVEN_ODD);

            if (!isTop) {
                path.moveTo(x - (triangleSize), y - (triangleSize));
                path.lineTo(x, y - GuideUtils.getTightestDimen(getContext()));
                path.lineTo(x + (triangleSize), y - (triangleSize));
            } else {
                path.moveTo(x - (triangleSize), y + (triangleSize));
                path.lineTo(x, y + GuideUtils.getTightestDimen(getContext()));
                path.lineTo(x + (triangleSize), y + (triangleSize));
            }

            path.close();
            paintLine.setPathEffect(cornerPathEffect);
            canvas.drawPath(path, paintLine);
            targetPaint.setXfermode(X_FER_MODE_CLEAR);
            targetPaint.setAntiAlias(true);
            canvas.drawRoundRect(targetRect, GuideUtils.getTighterDimen(getContext()), GuideUtils.getTighterDimen(getContext()), targetPaint);
        }
    }

    public void backgroundOverlay() {
        selfPaint.setColor(type == GuideType.POPOVER ? BACKGROUND_COLOR : BACKGROUND_TRANSPARENT);
        selfPaint.setStyle(Paint.Style.FILL);
        selfPaint.setAntiAlias(true);
    }

    public void onLinkListener(GuideListener listener) { if (listener != null) this.listener = listener; }

    public void recentTypeface(Typeface typeface) { messageView.recentTypeface(typeface); }

    public void titleTypeface(Typeface typeface) { messageView.titleTypeface(typeface); }

    public void contentTypeface(Typeface typeface) { messageView.contentTypeface(typeface); }

    public void previousTypeface(Typeface typeface) { messageView.previousTypeface(typeface); }

    public void nextTypeface(Typeface typeface) { messageView.nextTypeface(typeface); }

    public void recent(String recent) { messageView.recent(recent); }

    public void previous(String previous) { this.previous = previous; }

    public void next(String next) { this.next = next; }

    public void finish(String finish) { this.finish = finish; }

    public void backgroundColor(int color) {
        messageView.backgroundColor(color);
        paintLine.setColor(color);
    }

    public void recentBackgroundColor(int color) { messageView.recentBackgroundColor(color); }

    public void previousBackgroundColor(int color) { messageView.previousBackgroundColor(color); }

    public void nextBackgroundColor(int color) { messageView.nextBackgroundColor(color); }

    public void recentTextColor(int color) { messageView.recentTextColor(color); }

    public void titleTextColor(int color) { messageView.titleTextColor(color); }

    public void contentTextColor(int color) { messageView.contentTextColor(color); }

    public void previousTextColor(int color) { messageView.previousTextColor(color); }

    public void nextTextColor(int color) { messageView.nextTextColor(color); }

    public void closeImageColor(int color) { messageView.closeImageColor(color); }

    public void recentTextSize(int size) { messageView.recentTextSize(size); }

    public void titleTextSize(int size) { messageView.titleTextSize(size); }

    public void contentTextSize(int size) { messageView.contentTextSize(size); }

    public void previousTextSize(int size) { messageView.previousTextSize(size); }

    public void nextTextSize(int size) { messageView.nextTextSize(size); }

    @SuppressLint("ClickableViewAccessibility")
    public void build() {
        if (type == GuideType.TOOLTIP) {
            messageView.tooltip();
            new android.os.Handler().postDelayed(this::dismiss, TIMER);
        }
        setButton();
        this.setOnTouchListener((View view, MotionEvent motionEvent) -> type == GuideType.POPOVER);
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(this);
    }

    private void initView() {
        messageView             = new GuideMessageView(getContext());
        selfRect                = new Rect();
        position                = 0;
        yMessageView            = 0;
        indicatorHeightGuide    = GuideUtils.getTighterDimen(getContext());

        messageView.setPadding(GuideUtils.getBaseDimen(getContext()), GuideUtils.getTightestDimen(getContext()), GuideUtils.getBaseDimen(getContext()), GuideUtils.getTightestDimen(getContext()));
    }

    private void initListener() {
        messageView.onClickCloseListener(v -> dismiss());
        messageView.onClickPreviousListener(v -> {
            if (data.getLink(position) != null) {
                if (listener != null) listener.onClick(v);
            } else {
                position--;
                setTarget();
            }
        });
        messageView.onClickNextListener(v -> {
            if (position == data.getCount() - 1) dismiss();
            else {
                position++;
                setTarget();
            }
        });
    }

    private int getNavigationBarSize() {
        int resourceId = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");

        if (resourceId > 0) return getContext().getResources().getDimensionPixelSize(resourceId);
        return 0;
    }

    private void setTarget() {
        int[] locationTarget = new int[2];

        if (messageView.getParent() != null) removeView(messageView);
        data.getTarget(position).getLocationOnScreen(locationTarget);

        targetRect = new RectF(locationTarget[0], locationTarget[1], locationTarget[0] + data.getTarget(position).getWidth(), locationTarget[1] + data.getTarget(position).getHeight());

        addView(messageView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setMessageLocation(resolveMessageViewLocation());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] locationTarget = new int[2];

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setMessageLocation(resolveMessageViewLocation());
                data.getTarget(position).getLocationOnScreen(locationTarget);

                targetRect = new RectF(locationTarget[0], locationTarget[1], locationTarget[0] + data.getTarget(position).getWidth(), locationTarget[1] + data.getTarget(position).getHeight());

                selfRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
                getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        });
        messageView.title(data.getTitle(position));
        messageView.content(data.getContent(position));
        messageView.recentVisibility(data.isNew(position) ? View.VISIBLE : View.GONE);
        messageView.previousVisibility(position == 0 && data.getLink(position) == null ? View.GONE : View.VISIBLE);
        setButton();
    }

    private void setMessageLocation(Point p) {
        messageView.setX(p.x);
        messageView.setY(p.y);
        postInvalidate();
    }

    private void setButton() {
        messageView.previous(data.getLink(position) != null ? data.getLink(position) : previous);
        messageView.next(data.getCount() - 1 == position ? finish : next);
    }

    private boolean isLandscape() { return getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT; }

    private Point resolveMessageViewLocation() {
        int xMessageView = (int) (targetRect.left - messageView.getWidth() / 2 + data.getTarget(position).getWidth() / 2);

        if (isLandscape()) xMessageView -= getNavigationBarSize();
        if (xMessageView + messageView.getWidth() > getWidth()) xMessageView = getWidth() - messageView.getWidth();
        if (xMessageView < 0) xMessageView = 0;
        if (targetRect.bottom > getHeight() / 2) {
            isTop           = false;
            yMessageView    = (int) (targetRect.top - messageView.getHeight() - indicatorHeightGuide);
        } else {
            isTop           = true;
            yMessageView    = (int) (targetRect.top + data.getTarget(position).getHeight() + indicatorHeightGuide);
        }
        if (yMessageView < 0) yMessageView = 0;
        return new Point(xMessageView, yMessageView);
    }

    private void dismiss() { ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).removeView(this); }

    public static class Builder {
        private final Context context;
        private GuideData data;
        private GuideType type;
        private GuideListener listener;
        private Typeface recentTypeface;
        private Typeface titleTypeface;
        private Typeface contentTypeface;
        private Typeface previousTypeface;
        private Typeface nextTypeface;
        private String recent;
        private String previous;
        private String next;
        private String finish;
        private int backgroundColor;
        private int recentBackgroundColor;
        private int previousBackgroundColor;
        private int nextBackgroundColor;
        private int recentTextColor;
        private int titleTextColor;
        private int contentTextColor;
        private int previousTextColor;
        private int nextTextColor;
        private int closeImageColor;
        private int recentTextSize;
        private int titleTextSize;
        private int contentTextSize;
        private int previousTextSize;
        private int nextTextSize;

        public Builder(Context context) { this.context = context; }

        public Builder data(GuideData data) {
            this.data = data;
            return this;
        }

        public Builder type(GuideType type) {
            this.type = type;
            return this;
        }

        public Builder onLinkListener(GuideListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder recentTypeface(Typeface typeface) {
            this.recentTypeface = typeface;
            return this;
        }

        public Builder titleTypeface(Typeface typeface) {
            this.titleTypeface = typeface;
            return this;
        }

        public Builder contentTypeface(Typeface typeface) {
            this.contentTypeface = typeface;
            return this;
        }

        public Builder previousTypeface(Typeface typeface) {
            this.previousTypeface = typeface;
            return this;
        }

        public Builder nextTypeface(Typeface typeface) {
            this.nextTypeface = typeface;
            return this;
        }

        public Builder recent(String recent) {
            this.recent = recent;
            return this;
        }

        public Builder previous(String previous) {
            this.previous = previous;
            return this;
        }

        public Builder next(String next) {
            this.next = next;
            return this;
        }

        public Builder finish(String finish) {
            this.finish = finish;
            return this;
        }

        public Builder backgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder recentBackgroundColor(int color) {
            this.recentBackgroundColor = color;
            return this;
        }

        public Builder previousBackgroundColor(int color) {
            this.previousBackgroundColor = color;
            return this;
        }

        public Builder nextBackgroundColor(int color) {
            this.nextBackgroundColor = color;
            return this;
        }

        public Builder recentTextColor(int color) {
            this.recentTextColor = color;
            return this;
        }

        public Builder titleTextColor(int color) {
            this.titleTextColor = color;
            return this;
        }

        public Builder contentTextColor(int color) {
            this.contentTextColor = color;
            return this;
        }

        public Builder previousTextColor(int color) {
            this.previousTextColor = color;
            return this;
        }

        public Builder nextTextColor(int color) {
            this.nextTextColor = color;
            return this;
        }

        public Builder closeImageColor(int color) {
            this.closeImageColor = color;
            return this;
        }

        public Builder recentTextSize(int size) {
            this.recentTextSize = size;
            return this;
        }

        public Builder titleTextSize(int size) {
            this.titleTextSize = size;
            return this;
        }

        public Builder contentTextSize(int size) {
            this.contentTextSize = size;
            return this;
        }

        public Builder previousTextSize(int size) {
            this.previousTextSize = size;
            return this;
        }

        public Builder nextTextSize(int size) {
            this.nextTextSize = size;
            return this;
        }

        public GuideView build() {
            GuideView view  = new GuideView(context, data);
            view.type       = type == null ? GuideType.POPOVER : type;

            if (listener != null) view.onLinkListener(listener);
            if (recentTypeface != null) view.recentTypeface(recentTypeface);
            if (titleTypeface != null) view.titleTypeface(titleTypeface);
            if (contentTypeface != null) view.contentTypeface(contentTypeface);
            if (previousTypeface != null) view.previousTypeface(previousTypeface);
            if (nextTypeface != null) view.nextTypeface(nextTypeface);
            if (recent != null) view.recent(recent);
            if (previous != null) view.previous(previous);
            if (next != null) view.next(next);
            if (finish != null) view.finish(finish);
            if (backgroundColor != 0) view.backgroundColor(backgroundColor);
            if (recentBackgroundColor != 0) view.recentBackgroundColor(recentBackgroundColor);
            if (previousBackgroundColor != 0) view.previousBackgroundColor(previousBackgroundColor);
            if (nextBackgroundColor != 0) view.nextBackgroundColor(nextBackgroundColor);
            if (recentTextColor != 0) view.recentTextColor(recentTextColor);
            if (titleTextColor != 0) view.titleTextColor(titleTextColor);
            if (contentTextColor != 0) view.contentTextColor(contentTextColor);
            if (previousTextColor != 0) view.previousTextColor(previousTextColor);
            if (nextTextColor != 0) view.nextTextColor(nextTextColor);
            if (closeImageColor != 0) view.closeImageColor(closeImageColor);
            if (recentTextSize != 0) view.recentTextSize(recentTextSize);
            if (titleTextSize != 0) view.titleTextSize(titleTextSize);
            if (contentTextSize != 0) view.contentTextSize(contentTextSize);
            if (previousTextSize != 0) view.previousTextSize(previousTextSize);
            if (nextTextSize != 0) view.nextTextSize(nextTextSize);
            view.backgroundOverlay();
            view.build();
            return view;
        }
    }
}

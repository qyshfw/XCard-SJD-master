package com.x.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by X on 16/8/27.
 */
public class XButton extends ImageButton {
    private String _text = "";
    private int _color = 0;
    private float _textsize = 0f;

    public XButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        //获取手机屏幕密度。
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public XButton setText(String text){
        this._text = text;
//        Paint paint = new Paint();
//        Rect bounds = new Rect();
//        paint.getTextBounds(_text, 0, _text.length(), bounds);
        //invalidate()是用来刷新View的，必须是在UI线程中进行工作。
        this.invalidate();
        return this;
    }

    public XButton setColor(int color){
        this._color = color;
        this.invalidate();
        return  this;
    }

    public XButton setTextSize(float textsize){
        this._textsize = textsize;
        this.invalidate();
        return  this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas  画布
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(_color);
        paint.setTextSize(_textsize);
        System.out.println("text: "+_text+" w: "+canvas.getWidth()+" H: "+canvas.getHeight());

        //Rect 表示一个矩形
        Rect bounds = new Rect();
        paint.getTextBounds(_text, 0, _text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(_text,canvas.getWidth()/2, baseline, paint);
//        canvas.drawText(_text, canvas.getWidth()/2, (canvas.getHeight()/2), paint);

    }
}

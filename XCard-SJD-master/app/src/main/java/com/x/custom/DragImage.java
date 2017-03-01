package com.x.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.x.xcard.R;

/**
 * Created by X on 16/8/30.
 */
public class DragImage extends SurfaceView implements SurfaceHolder.Callback,Runnable,View.OnTouchListener {
    private Context context;
    private SurfaceHolder holder;
    private Bitmap icon;
    private Paint paint;
    private boolean running=true;

    //构造了一个方法； 自定义控件的AttributeSet属性；
    public DragImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("DragImage 000 ~~~~");
        this.context=context;
        holder = this.getHolder();//获取holder
        //添加回调函数
        holder.addCallback(this);
        //设置触摸监听
        this.setOnTouchListener(this);

    }

    public DragImage(Context context) {
        super(context);
        System.out.println("DragImage 111 ~~~~");
        this.context=context;
        holder = this.getHolder();//获取holder
        holder.addCallback(this);
        this.setOnTouchListener(this);
    }

    //在创建时激发，一般在这里调用画图的线程。
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        System.out.println("surfaceCreated !!!!!!!!!!!");

        paint = new Paint();
        rect = new Rect();
        rect.top = 300;
        rect.left = 50;
        rect.bottom = 500;
        rect.right = 120;
        icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_white);
        paint=new Paint();
        paint.setColor(Color.RED);

        //Canvas canvas = holder.lockCanvas();//获取画布
        //canvas.drawColor(Color.BLACK);
//        canvas.drawBitmap(icon, 100,100,paint);


//        Paint  p =new Paint();
//        p.setColor(Color.rgb( (int)(Math.random() * 255),
//                (int)(Math.random() * 255) ,  (int)(Math.random() * 255)));
//        Rect aa  =  new Rect( (int)(Math.random() * 100) ,
//                (int)(Math.random() * 100)
//                ,(int)(Math.random() * 500)
//                ,(int)(Math.random() * 500) );
//        canvas.drawRect(aa, p);


        //holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        //结束的时间   long end=System.currentTimeMillis();

        //running = false;




//        running=true;
//        new Thread(this).start();

        update();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running=false;
    }

    @Override
    public void run() {
        int SLEEP_TIME=100;
        while (running) {
            //开始画的时间    long start=System.currentTimeMillis();
            Canvas canvas = holder.lockCanvas(null);//获取画布
            canvas.drawColor(Color.parseColor("#f3f4f5"));

            paint.setColor(Color.RED);
            canvas.drawCircle(40, 40, 30, paint);

            paint.setColor(Color.BLUE);
            canvas.drawCircle(110+20, 40, 30, paint);
            canvas.drawCircle(180+40, 40, 30, paint);
            canvas.drawCircle(250+60, 40, 30, paint);


            paint.setColor(Color.RED);
            canvas.drawCircle(rect.left, 40, 30, paint);

            canvas.drawBitmap(icon, rect.left,rect.top,paint);
            holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
            //结束的时间   long end=System.currentTimeMillis();


        }
    }

    private void update()
    {
        //开始画的时间    long start=System.currentTimeMillis();
        Canvas canvas = holder.lockCanvas(null);//获取画布
        canvas.drawColor(Color.parseColor("#f3f4f5"));

        paint.setColor(Color.RED);
        canvas.drawCircle(40, 40, 30, paint);

        paint.setColor(Color.BLUE);
        canvas.drawCircle(110+20, 40, 30, paint);
        canvas.drawCircle(180+40, 40, 30, paint);
        canvas.drawCircle(250+60, 40, 30, paint);


        paint.setColor(Color.RED);
        canvas.drawCircle(rect.left, 40, 30, paint);

        canvas.drawBitmap(icon, rect.left,rect.top,paint);
        holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        //结束的时间   long end=System.currentTimeMillis();
    }




    //      Region region=new Region();
    private Point point=new Point();//点击点
    private Rect rect;//图片的rect
    private boolean canDrag=false;//判断是否点击在图片上，否则拖动无效
    private int offsetX=0,offsetY=0;//点击点离图片左上角的距离
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        System.out.println("onTouch !!!!!!!!!!!");


        switch (event.getAction()) {

            //手按下的时候
            case MotionEvent.ACTION_DOWN:
                point.x=(int)event.getX();
                point.y=(int)event.getY();
                if(rect.contains(point.x, point.y)){
                    canDrag=true;
                    offsetX=point.x-rect.left;
                    offsetY=point.y-rect.top;
                }
                break;

            //移动的时候
            case MotionEvent.ACTION_MOVE:
                if(canDrag){
                    rect.left=(int)event.getX()-offsetX;
                    rect.top=(int)event.getY()-offsetY;
                    rect.right=rect.left+icon.getWidth();
                    rect.bottom=rect.top+icon.getHeight();
                    if (rect.left < 0) {
                        rect.left = 0;
                        rect.right =  rect.left+icon.getWidth();
                    }
                    if (rect.right >  getMeasuredWidth()) {
                        rect.right =  getMeasuredWidth();
                        rect.left = rect.right-icon.getWidth();
                    }
                    if (rect.top < 0) {
                        rect.top = 0;
                        rect.bottom = rect.top+icon.getHeight();
                    }
                    if (rect.bottom > getMeasuredHeight()) {
                        rect.bottom = getMeasuredHeight();
                        rect.top = rect.bottom-icon.getHeight();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                canDrag=false;
                break;

            default:
                break;
        }

        update();

        return true;
    }

}


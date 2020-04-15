package com.xp.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xp.R;

import java.io.File;

public class ImageViewTouch extends Fragment {

    private static String path;
    public static  ImageViewTouch getImageViewTouch(String path){
        ImageViewTouch imageViewTouch = new ImageViewTouch();

        ImageViewTouch.path =path;
        return  imageViewTouch;
    }



    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_image_view_touch, container, false);

        imageView = view.findViewById(R.id.imageView);
        Uri imageUri = FileProvider.getUriForFile(AndroidTool.getMainActivity(), "com.xp.fileprovider", new File(path));
        imageView.setImageURI(imageUri);
        imageView.setOnTouchListener(new  ImageTouchListener(imageView) );
        return view;
    }
    private   ImageView imageView;

    /**
     * 图片 手势操作监听
     */
    public  class ImageTouchListener implements View.OnTouchListener {

        private   ImageView imageView;

        public    ImageTouchListener(ImageView imageView) {
            this.imageView = imageView;
        }
        //声明一个坐标点
        private PointF startPoint;
        //声明并实例化一个Matrix来控制图片
        private Matrix matrix = new Matrix();
        //声明并实例化当前图片的Matrix
        private Matrix mCurrentMatrix = new Matrix();


        //缩放时初始的距离
        private float startDistance;
        //拖拉的标记
        private static final int DRAG = 1;
        //缩放的标记
        private static final int ZOOM = 2;
        //标识记录
        private int mode;
        //缩放的中间点
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("ACTION_DOWN");
                    Log.w("Drag", "ACTION_DOWN");
                    //此时处于拖拉方式下
                    mode = DRAG;
                    //获得当前按下点的坐标
                    startPoint = new PointF(event.getX(), event.getY());
                    //把当前图片的Matrix设置为按下图片的Matrix
                    mCurrentMatrix.set(imageView.getImageMatrix());
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.w("Drag", "ACTION_MOVE");
                    //根据不同的模式执行相应的缩放或者拖拉操作
                    switch (mode) {
                        case DRAG:
                            //移动的x坐标的距离
                            float dx = event.getX() - startPoint.x;
                            //移动的y坐标的距离
                            float dy = event.getY() - startPoint.y;
                            //设置Matrix当前的matrix
                            matrix.set(mCurrentMatrix);
                            //告诉matrix要移动的x轴和Y轴的距离
                            matrix.postTranslate(dx, dy);
                            break;
                        case ZOOM:
                            //计算缩放的距离
                            float endDistance = distance(event);
                            //计算缩放比率
                            float scale = endDistance / startDistance;
                            //设置当前的Matrix
                            matrix.set(mCurrentMatrix);
                            //设置缩放的参数
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            break;
                        default:
                            break;
                    }

                    break;
                //已经有一个手指按住屏幕，再有一个手指按下屏幕就会触发该事件
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.w("Drag", "ACTION_POINTER_DOWN");
                    //此时为缩放模式
                    mode = ZOOM;
                    //计算开始时两个点的距离
                    startDistance = distance(event);
                    //当两个点的距离大于10时才进行缩放操作
                    if (startDistance > 10) {
                        //计算中间点
                        midPoint = mid(event);
                        //得到进行缩放操作之前，照片的绽放倍数
                        mCurrentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
                //已经有一个手指离开屏幕，还有手指在屏幕上时就会触发该事件
                case MotionEvent.ACTION_POINTER_UP:
                    Log.w("Drag", "ACTION_POINTER_UP");
                    mode = 0;
                    break;
                case MotionEvent.ACTION_UP:
                    Log.w("Drag", "ACTION_UP");
                    mode = 0;
                    break;
                default:
                    break;
            }
            //按照Matrix的要求移动图片到某一个位置
            imageView.setImageMatrix(matrix);
            //返回true表明我们会消费该动作，不需要父控件进行进一步的处理
            return true;
        }

        public float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);

            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        public PointF mid(MotionEvent event) {
            float x = (event.getX(1) - event.getX(0)) / 2;
            float y = (event.getY(1) - event.getY(0)) / 2;
            return new PointF(x, y);
        }

    }

}

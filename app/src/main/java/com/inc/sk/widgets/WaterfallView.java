package com.inc.sk.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.sk.R;
import com.inc.sk.imgload.ImageLoader;
import com.inc.sk.modules.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spec_Inc on 6/17/2016.
 */

public class WaterfallView extends ScrollView {

    /**
     * Item点击监听回调
     */
    private OnItemClickListener mItemClickListener = null;

    /**
     * 第1列的长度
     */
    private int mFirstColumnHeight = 0;

    /**
     * 第1列对象
     */
    private LinearLayout mFirstColumn = null;

    /**
     * 第2列的长度
     */
    private int mSecondColumnHeight = 0;

    /**
     * 第2列对象
     */
    private LinearLayout mSecondColumn = null;


    /**
     * 列的宽度
     */
    private int mColumnWidth = 0;


    /**
     * 加载一次的图片数
     */
    private static final int PAGE_SIZE = 12;

    /**
     * 图片高度
     * 备注;不采用不定高度，因为根据需求，需要保持最大成都的公平性
     */
    private int mImgHeight = 800;

    /**
     * 数据源：图片URL链接
     */
    private String[] mDatas ;

    /**
     * 每次加载的数据位置头指针
     */
    private int pHeader = 0;

    /**
     * 每次加载的数据位置尾指针
     */
    private int pFooter = 0;

    /**
     * 首次布局初始化标志
     */
    private boolean isOnceLoaded = false;

    /**
     * 滑动位置指针
     */
    private int lastScrollY = -1;
    /**
     * 手指按下的位置指针
     */
    private int downY = -1;

    /**
     * 手指抬起的位置指针
     */
    private int upY = -1;

    /**
     *滑到底部的标志
     */
    private boolean isBottomed = false;

    /**
     * item缓存容器
     */
    private List<View> mItemContainer = new ArrayList<>();

    /**
     * 图片下载器
     */
    private ImageLoader mLoader = ImageLoader.newInstance(getContext());

    /**
     * 底部更新状态指示器
     */
    private TextView mIndicator = null;

    /**
     * 圆形进度圈
     */
    private ProgressBar mProgress = null;

    /**
     * 内容加载完成标志
     */
    private boolean isFinished = false;


    public static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){

            WaterfallView wsv = (WaterfallView) msg.obj;

            /**
             * 响应底部更新加载动作
             */
            if(msg.what == 0x123){
                wsv.mProgress.setVisibility(GONE);
                wsv.mIndicator.setText(LOAD_MORE);
                wsv.loadItems();
            }
        }

    };

    public WaterfallView(Context context) {
        super(context);
    }


    public WaterfallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaterfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public static final String LOAD_MORE = "上拉加载更多";
    public static final String LOAD_FINISH = "已经到最后";
    public static final String LOADING = "正在加载中..";
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        /**
         * 第一次加载初始化
         */
        if(changed && !isOnceLoaded){
            mFirstColumn = (LinearLayout) findViewById(R.id.column_first);
            mSecondColumn = (LinearLayout) findViewById(R.id.column_second);
            mIndicator = (TextView) findViewById(R.id.indicator);
            mProgress = (ProgressBar) findViewById(R.id.progressbar);
            mIndicator.setText(this.LOAD_MORE);
            mColumnWidth = mFirstColumn.getWidth();
            mImgHeight = mColumnWidth/7*8;
            isOnceLoaded = true;
            loadItems();
        }
    }

    public static final int LL_MARGIN_LEFT = 5;
    public static final int LL_MARGIN_TOP = 30;
    public static final int LL_MARGIN_RIGHT = 15;
    public static final int LL_MARGIN_BOTTOM = 0;
    public static final int LL_ELEVATION = 30;
    public void loadItems(){

        int size = mDatas.length;
        LinearLayout ll ;
        if(pHeader +PAGE_SIZE > size){
            pFooter = size;
        }else {
            pFooter = pHeader +PAGE_SIZE;
        }

        if(pHeader == pFooter){
            mIndicator.setText(LOAD_FINISH);
            isFinished = true;
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = pHeader; i< pFooter; i++){

            if(mFirstColumnHeight <= mSecondColumnHeight){
                mFirstColumnHeight += mImgHeight;
                ll = mFirstColumn;

            }else {
                mSecondColumnHeight += mImgHeight;
                ll = mSecondColumn;
            }

            LinearLayout item = new LinearLayout(getContext());
            item.setBackgroundResource(R.drawable.waterfall_item_bg);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mColumnWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            lp.setMargins(LL_MARGIN_LEFT,LL_MARGIN_TOP,LL_MARGIN_RIGHT,LL_MARGIN_BOTTOM);
            item.setOrientation(LinearLayout.VERTICAL);
            if (Build.VERSION.SDK_INT >= 21){
                 item.setElevation(LL_ELEVATION);
            }
            item.setLayoutParams(lp);
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams ivLp = new LinearLayout.LayoutParams(mColumnWidth, mImgHeight);
            iv.setLayoutParams(ivLp);
            item.addView(iv);
            View v = inflater.inflate(R.layout.view_waterfall_item,null);
            TextView name = (TextView)v.findViewById(R.id.title) ;
            mItemContainer.add(iv);
            name.setText("NO."+i+" Starking Girl ");
            LinearLayout tail = (LinearLayout) v.findViewById(R.id.waterfall_item);
            item.addView(tail);
            ll.addView(item);
            mLoader.display(mDatas[i],iv);
            item.setId(i);
            item.setOnClickListener(mItemClickListener);
        }
        pHeader = pFooter;
    }


    public void showToast(String str){
        Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("ALL")
    public void loadImages() {
        ImageLoader loader = ImageLoader.newInstance(getContext());
        int size = mDatas.length;
        LinearLayout ll ;
        if(pHeader +PAGE_SIZE > size){
            pFooter = size;
        }else {
            pFooter = pHeader +PAGE_SIZE;
        }
        for (int i = pHeader; i< pFooter; i++){

            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mColumnWidth,
                    mImgHeight);
            lp.setMargins(0,30,0,0);
            iv.setLayoutParams(lp);
            mItemContainer.add(iv);

            if(mFirstColumnHeight <= mSecondColumnHeight){
                mFirstColumnHeight += mImgHeight;
                ll = mFirstColumn;

            }else {
                mSecondColumnHeight += mImgHeight;
                ll = mSecondColumn;
            }

            TextView tv = new TextView(getContext());
            tv.setBackgroundColor(Color.WHITE);
            tv.setText("Starking Girls"+i);
            ll.addView(iv);
            ll.addView(tv);
            loader.display(mDatas[i],iv);
        }
        pHeader = pFooter;

    }

    public void setDatas(String[] datas){
        this.mDatas = datas;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(ev.getAction() == MotionEvent.ACTION_UP){

            View cv = getChildAt(0);
            /*滑动布局的高度—即目前所有数据的总高度*/
            int mh = cv.getMeasuredHeight();
            /*滑动的宽度*/
            int sY = getScrollY();
            /*屏幕可视的宽度*/
            int h = getHeight();

            if(mh == sY+h){
                if(lastScrollY != sY +h){
                    lastScrollY = sY +h;
                    isBottomed = true;
                    // view.checkVisibility();
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downY = (int) ev.getY();
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                upY = (int) ev.getY();
            }
            if(isBottomed) {
                isBottomed = false;

                if (upY - downY >= 40 && !isFinished) {
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = this;
                    mProgress.setVisibility(VISIBLE);
                    mIndicator.setText(LOADING);

                    //move to bottom ,but unable use
                    int offset = mh - this.getHeight();
                    if (offset < 0) {
                        offset = 0;
                    }

                    this.scrollTo(0, offset);

                    mHandler.sendMessageDelayed(msg,3000);
                }
            }

        }

        return super.onTouchEvent(ev);
    }

    public void cleanDiskCache(){

        mLoader.cleanDiskCache();
    }



   /*
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，
     * 则将图片替换成一张空图
    */
    @SuppressWarnings("ALL")
    public  void checkVisibility() {

        Rect scrollBounds = new Rect();
        this.getHitRect(scrollBounds);

        for(int i = 0; i< mItemContainer.size(); i++){
            ImageView iv = (ImageView) mItemContainer.get(i);
            if (iv.getLocalVisibleRect(scrollBounds)) {
                System.out.println("abc=iv is visivable  "+i);
            } else {
                System.out.println("-->>iv is not visivable  "+i);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }


    public interface OnItemClickListener extends OnClickListener{
        @Override
        void onClick(View v);
    }

}

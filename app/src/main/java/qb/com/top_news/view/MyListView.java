package qb.com.top_news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import qb.com.top_news.R;


public class MyListView extends ListView implements AbsListView.OnScrollListener {
    View footer, header;
    int footerHeight, headerHeight;
    IReflashListener iRefalshListener;
    int scrollState;//当前滚动状态
    int first;
    int last;
    public boolean bottomFlag;//标记是否在lv最底端按下
    public boolean topFlag;//标记是否在lv最顶端按下
    int startY;
    int state_bottom;//当前的状态
    int state_up;//当前的状态
    final int NONE_BOTTOM = 0;//正常状态
    final int PULL_BOTTOM = 1;//提示上拉状态
    final int RELESE_BOTTOM = 2;//提示释放状态
    final int RELESING_BOTTOM = 3;//正在刷新

    final int NONE_UP = 7;//正常状态
    final int PULL_UP = 4;//提示上拉状态
    final int RELESE_UP = 5;//提示释放状态
    final int RELESING_UP = 6;//正在刷新

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footer_layout, null);
        header = inflater.inflate(R.layout.header_layout, null);
        measureView(footer);
        measureView(header);
        // 设置内边距，正好距离底部为一个负的整个布局的高度，正好把头部隐藏
        footerHeight = footer.getMeasuredHeight();
        footer.setPadding(0, 0, 0, -1 * footerHeight);
        footer.invalidate();
        this.addFooterView(footer, null, false);

        // 设置内边距，正好距离顶部为一个负的整个布局的高度，正好把头部隐藏
        headerHeight = header.getMeasuredHeight();
        header.setPadding(0, -1 * headerHeight, 0, 0);
        header.invalidate();
        this.addHeaderView(header, null, false);
        this.setOnScrollListener(this);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    //设置下边距
    private void bottomPadding(int bottom) {
        footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(), footer.getPaddingRight(), bottom);
        footer.invalidate();
    }

    //设置上边距
    private void topPadding(int top) {
        header.setPadding(header.getPaddingLeft(), top, header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.first = firstVisibleItem;
        this.last = firstVisibleItem + visibleItemCount - totalItemCount;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (last == 0) {
                    bottomFlag = true;
                    startY = (int) ev.getY();
                    reflashViewBottomByState();
                }
                if (first == 0) {
                    topFlag = true;
                    startY = (int) ev.getY();
                    reflashViewTopByState();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMoveBottom(ev);
                onMoveTop(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state_bottom == RELESE_BOTTOM) {
                    state_bottom = RELESING_BOTTOM;
                    //加载数据
                    reflashViewBottomByState();
                    iRefalshListener.onReflash();
                } else if (state_bottom == PULL_BOTTOM) {
                    state_bottom = NONE_BOTTOM;
                    bottomFlag = false;
                    reflashViewBottomByState();
                }
                if (state_up == RELESE_UP) {
                    state_up = RELESING_UP;
                    //加载数据
                    reflashViewTopByState();
                    iRefalshListener.onReflash();
                } else if (state_up == PULL_UP) {
                    state_up = NONE_UP;
                    topFlag = false;
                    reflashViewTopByState();
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onMoveBottom(MotionEvent ev) {
        if (!bottomFlag) {
            return;
        }
        int tempY = (int) ev.getY();
        int bottomSpace = startY - tempY;//判断上拉滑动距离
        int bottomPadding = bottomSpace - footerHeight;
        switch (state_bottom) {
            case NONE_BOTTOM:
                if (bottomSpace > 0) {
                    state_bottom = PULL_BOTTOM;
                    reflashViewBottomByState();
                }

                break;
            case PULL_BOTTOM:
                bottomPadding(bottomPadding);
                if (bottomSpace > footerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state_bottom = RELESE_BOTTOM;
                    reflashViewBottomByState();
                }

                break;
            case RELESE_BOTTOM:
                bottomPadding(bottomPadding);
                if (bottomSpace < footerHeight + 30) {
                    state_bottom = PULL_BOTTOM;
                    reflashViewBottomByState();
                } else if (bottomSpace <= 0) {
                    state_bottom = NONE_BOTTOM;
                    bottomFlag = false;
                    reflashViewBottomByState();
                }

                break;
            case RELESING_BOTTOM:
                reflashViewBottomByState();
                break;
        }

    }

    private void onMoveTop(MotionEvent ev) {
        if (!topFlag) {
            return;
        }
        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;
        switch (state_up) {
            case NONE_UP:
                if (space > 0) {
                    state_up = PULL_UP;
                    reflashViewTopByState();
                }
                break;
            case PULL_UP:
                topPadding(topPadding);
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state_up = RELESE_UP;
                    reflashViewTopByState();
                }
                break;
            case RELESE_UP:
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    state_up = PULL_UP;
                    reflashViewTopByState();
                } else if (space <= 0) {
                    state_up = NONE_UP;
                    topFlag = false;
                    reflashViewTopByState();
                }
                break;
            case RELESING_UP:
                reflashViewTopByState();
                break;
        }

    }

    private void reflashViewBottomByState() {
        TextView tip1 = (TextView) footer.findViewById(R.id.tip);
        ImageView arrow1 = (ImageView) footer.findViewById(R.id.arrow);
        ProgressBar pb1 = (ProgressBar) footer.findViewById(R.id.progress);
        RotateAnimation anim1 = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        RotateAnimation anim2 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(500);
        anim2.setFillAfter(true);
        switch (state_bottom) {
            case NONE_BOTTOM:
//                bottomPadding(-footerHeight);
                pb1.setVisibility(View.GONE);
                tip1.setText(R.string.down);
                arrow1.clearAnimation();
                break;
            case PULL_BOTTOM:
                arrow1.setVisibility(View.VISIBLE);
                pb1.setVisibility(View.GONE);
                tip1.setText(R.string.down);
                arrow1.clearAnimation();
                arrow1.setAnimation(anim2);


                break;
            case RELESE_BOTTOM:
                arrow1.setVisibility(View.VISIBLE);
                pb1.setVisibility(View.GONE);
                tip1.setText(R.string.loose);
                arrow1.clearAnimation();
                arrow1.setAnimation(anim1);
                break;
            case RELESING_BOTTOM:
                bottomPadding(50);
                arrow1.clearAnimation();
                arrow1.setVisibility(View.GONE);
                pb1.setVisibility(View.VISIBLE);
                tip1.setText(R.string.refreshing);

                break;
        }
    }

    private void reflashViewTopByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar pb = (ProgressBar) header.findViewById(R.id.progress);
        RotateAnimation anim1 = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(500);
        anim1.setFillAfter(true);
        RotateAnimation anim2 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim2.setDuration(500);
        anim2.setFillAfter(true);
        switch (state_up) {
            case NONE_UP:
                topPadding(-headerHeight);
                arrow.clearAnimation();
                break;
            case PULL_UP:
                arrow.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                tip.setText(R.string.up);
                arrow.clearAnimation();
                arrow.setAnimation(anim2);
                break;
            case RELESE_UP:
                arrow.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                tip.setText(R.string.loose);
                arrow.clearAnimation();
                arrow.setAnimation(anim1);
                break;
            case RELESING_UP:
                topPadding(50);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                tip.setText(R.string.refreshing);
                break;
        }
    }

    public void reflashCompleted() {
        state_up = NONE_UP;
        state_bottom = NONE_BOTTOM;
        bottomFlag = false;
        topFlag = false;
        reflashViewBottomByState();
        reflashViewTopByState();


    }

    public void setInterface(IReflashListener iRefalshListener) {
        this.iRefalshListener = iRefalshListener;
    }

    public interface IReflashListener {
        public void onReflash();
    }

}

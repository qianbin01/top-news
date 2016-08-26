package qb.com.top_news.view;

import android.content.Context;
import android.graphics.Typeface;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "xinwei.TTF");
        setTypeface(tf);
    }
}

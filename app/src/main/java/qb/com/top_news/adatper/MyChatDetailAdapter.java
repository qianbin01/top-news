package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.utils.ImageUtils;
import qb.com.top_news.vo.ChatDetail;
import qb.com.top_news.vo.SystemChatInfo;

public class MyChatDetailAdapter extends BaseAdapter {
    public Context mContext;
    private LayoutInflater mInflater;
    private List<ChatDetail> mList;

    public MyChatDetailAdapter(Context mContext, List<ChatDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).isComing()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            if (mList.get(position).isComing()) {
                convertView = mInflater.inflate(R.layout.message_left, null);
            } else {
                convertView = mInflater.inflate(R.layout.message_right, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.ivHead);
            viewHolder.isComing = mList.get(position).isComing();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTime.setText(mList.get(position).getTime());
        viewHolder.tvContent.setText(mList.get(position).getContent());
        if (mList.get(position).getHead().equals("1")) {
            viewHolder.ivHead.setImageResource(R.drawable.men);
        } else if (mList.get(position).getHead().equals("2")) {
            viewHolder.ivHead.setImageResource(R.drawable.icon);
        } else {
            viewHolder.ivHead.setImageBitmap(ImageUtils.String2Bitmap(mList.get(position).getHead()));
        }
        return convertView;
    }

    class ViewHolder {
        ImageView ivHead;
        TextView tvContent;
        TextView tvTime;
        boolean isComing = true;
    }
}

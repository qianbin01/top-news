package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.vo.ChatList;

public class MyChatListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ChatList> mList;
    private LayoutInflater mInflater;

    public MyChatListAdapter(Context Context, List<ChatList> mList) {
        this.mContext = Context;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
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
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.message_item, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.ivHead = (ImageView) convertView.findViewById(R.id.ivHead);
            viewHolder.badgeView = (BadgeView) convertView.findViewById(R.id.badgeView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(mList.get(position).getName());
        viewHolder.tvContent.setText(mList.get(position).getContent());
        viewHolder.tvTime.setText(mList.get(position).getTime());
        if (mList.get(position).getIsRead() == 0) {
            viewHolder.badgeView.setVisibility(View.VISIBLE);
            viewHolder.badgeView.setBadgeCount(mList.get(position).getCount());
        } else {
            viewHolder.badgeView.setVisibility(View.GONE);
        }
        if(mList.get(position).getHead().equals("1")){
            viewHolder.ivHead.setImageResource(R.drawable.men);
        }else{
            viewHolder.ivHead.setImageResource(R.drawable.icon);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        ImageView ivHead;
        BadgeView badgeView;
    }
}

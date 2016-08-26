package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.vo.Contact;


public class MyContactAdapter extends BaseAdapter {
    public static HashMap hmAlpha;
    public static String[] sections;
    private LayoutInflater inflater;
    private List<Contact> mLists;

    public MyContactAdapter(Context context, List<Contact> lists) {

        this.inflater = LayoutInflater.from(context);
        this.mLists = lists;
        hmAlpha = new HashMap<String, Integer>();
        sections = new String[mLists.size()];

        for (int i = 0; i < mLists.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = mLists.get(i).getSortKey();
            // 上一个汉语拼音首字母，如果不存在为“ ”
            String previewStr = i >= 1 ? mLists.get(i - 1)
                    .getSortKey() : " ";
            if (!previewStr.equals(currentStr)) {
                String name = mLists.get(i).getSortKey();
                hmAlpha.put(name, i);
                sections[i] = name;
            }
        }


    }

    public void updateListView(List<Contact> list) {
        this.mLists = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, null);
            holder = new ComViewHolder();
            holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ComViewHolder) convertView.getTag();
        }

        holder.name.setText(mLists.get(position).getName());
        String currentStr = mLists.get(position).getSortKey();
        String previewStr = position >= 1 ? mLists.get(position - 1)//判断是不是第一个，来显示标题
                .getSortKey() : " ";
        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ComViewHolder {
        TextView alpha;
        TextView name;
    }
}

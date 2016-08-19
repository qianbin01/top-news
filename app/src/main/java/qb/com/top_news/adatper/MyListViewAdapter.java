package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.News;

public class MyListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<News> newsList;
    private DbUtils db;
    public int count;
    public int MAX_ITEM;

    public MyListViewAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.newsList = newsList;
        db = MyApplication.getDb();
        MAX_ITEM = newsList.size();
        count = MAX_ITEM / 3;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.realtype = (TextView) convertView.findViewById(R.id.realtype);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(newsList.get(position).getTitle());
        viewHolder.author.setText(newsList.get(position).getAuthor_name());
        if (newsList.get(position).getRealtype() != null) {
            viewHolder.realtype.setText(newsList.get(position).getRealtype());
        } else {
            viewHolder.realtype.setText(newsList.get(position).getCategory());
        }
        viewHolder.date.setText(newsList.get(position).getDate());
        Picasso.with(mContext).load(newsList.get(position).getThumbnail_pic_s03()).into(viewHolder.pic);
        try {
            News news = db.findFirst(Selector.from(News.class).where("title", "=", newsList.get(position).getTitle()));
            if (news == null) {
                db.save(newsList.get(position));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView author;
        TextView realtype;
        TextView date;
        ImageView pic;
    }
}

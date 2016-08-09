package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.vo.News;

/**
 * Created by qianbin on 16/8/9.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<News> newsList;

    public MyListViewAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.newsList = newsList;
    }

    public MyListViewAdapter() {
    }

    @Override
    public int getCount() {
        return newsList.size();
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
        if(newsList.get(position).getRealtype()!=null) {
            viewHolder.realtype.setText(newsList.get(position).getRealtype());
        }else{
            viewHolder.realtype.setText(newsList.get(position).getCategory());
        }
        viewHolder.date.setText(newsList.get(position).getDate());
        Picasso.with(mContext).load(newsList.get(position).getThumbnail_pic_s03()).into(viewHolder.pic);

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

package qb.com.top_news.adatper;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.squareup.picasso.Picasso;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.News;

public class MyLikeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<News> newsList;
    private DbUtils db;

    public MyLikeAdapter(Context mContext, List<News> newsList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.newsList = newsList;
        db = MyApplication.getDb();

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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.realtype = (TextView) convertView.findViewById(R.id.realtype);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.like = (ImageView) convertView.findViewById(R.id.like);
            viewHolder.tvZan = (TextView) convertView.findViewById(R.id.tvZan);
            viewHolder.tvCai = (TextView) convertView.findViewById(R.id.tvCai);
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
        viewHolder.tvCai.setText(String.valueOf(newsList.get(position).getCai()));
        viewHolder.tvCai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "无法在收藏页面做差评操作", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvZan.setText(String.valueOf(newsList.get(position).getZan()));
        viewHolder.tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "无法在收藏页面做点赞操作", Toast.LENGTH_SHORT).show();
            }
        });
        final News news = newsList.get(position);
        final int flag = news.getLike();
        viewHolder.like.setImageResource(R.drawable.like_press);
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    viewHolder.like.setImageResource(R.drawable.like_press);
                    news.setLike(1);
                } else {
                    viewHolder.like.setImageResource(R.drawable.like_normol);
                    news.setLike(0);
                }
                try {
                    db.saveOrUpdate(news);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            db.saveOrUpdate(news);
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
        TextView tvCai;
        TextView tvZan;
        ImageView like;
    }

}


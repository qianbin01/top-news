package qb.com.top_news.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.vo.AppInfo;


public class MyAppAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<AppInfo> mList;

    public MyAppAdapter(Context Context, List<AppInfo> mList) {
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
            convertView = mInflater.inflate(R.layout.app_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.tips = (TextView) convertView.findViewById(R.id.tips);
            viewHolder.star = (TextView) convertView.findViewById(R.id.star);
            viewHolder.download = (Button) convertView.findViewById(R.id.download);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mList.get(position).getName());
        viewHolder.tips.setText(mList.get(position).getTips());
        viewHolder.star.setText("评分:" + mList.get(position).getStars());
        Picasso.with(mContext).load(mList.get(position).getImgPath()).into(viewHolder.img);
        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("下载还在测试，别慌张");
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView tips;
        TextView star;
        Button download;
        ImageView img;
    }
}

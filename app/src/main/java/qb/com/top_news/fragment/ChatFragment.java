package qb.com.top_news.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.activity.ChatDetailActivity;
import qb.com.top_news.adatper.MyChatListAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.utils.DateUtils;
import qb.com.top_news.vo.ChatList;


public class ChatFragment extends BaseFragment {
    private View view;
    private ListView lvMessage;
    private List<ChatList> mList;
    private DbUtils db;
    private MyChatListAdapter mAdapter;
    private IntentFilter intentFilter;
    private String[] times;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        initData();
        initEvent();
        return view;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.chat_fragment, null);
        lvMessage = (ListView) view.findViewById(R.id.lvMessage);
        return view;
    }

    @Override
    public void initData() {
        lvMessage.setDivider(null);
        mList = new ArrayList<>();
        db = MyApplication.getDb();
        try {
            mList = db.findAll(Selector.from(ChatList.class));

            if (mList.size() == 0) {
                ChatList chatList = new ChatList("今日头条", "2", DateUtils.getNowTime(), "系统通知，欢迎您的使用", 0, 1);
                mList.add(chatList);
                ChatList chatList2 = new ChatList("橄榄", "1", DateUtils.getNowTime(), "犯我德邦者，虽远必诛", 0, 1);
                mList.add(chatList2);
                mAdapter = new MyChatListAdapter(getActivity(), mList);
                lvMessage.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                db.saveOrUpdateAll(mList);
            } else {
                times = new String[mList.size()];
                for (int i = 0; i < mList.size(); i++) {
                    times[i] = mList.get(i).getTime();
                    System.out.println(times[i]);
                    mList.get(i).setTime(DateUtils.getTimesToNow(mList.get(i).getTime()));
                }
                mAdapter = new MyChatListAdapter(getActivity(), mList);
                lvMessage.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        intentFilter = new IntentFilter("sendMs");
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    protected void initEvent() {
        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                mList.get(position).setIsRead(1);
                mAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putInt("listId", mList.get(position).getId());
                bundle.putString("head", mList.get(position).getHead());
                bundle.putString("name", mList.get(position).getName());
                bundle.putString("content", mList.get(position).getContent());
                bundle.putString("time", times[index]);
                openActivity(ChatDetailActivity.class, bundle);
            }
        });
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("content");
            String time = intent.getStringExtra("time");
            mList.get(index).setContent(content);
            mList.get(index).setTime(time);
            mAdapter.notifyDataSetChanged();
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setTime(times[i]);
            }
            try {
                db.saveOrUpdateAll(mList);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}

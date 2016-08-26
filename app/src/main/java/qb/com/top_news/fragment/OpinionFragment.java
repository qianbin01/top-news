package qb.com.top_news.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyChatAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.utils.DateUtils;
import qb.com.top_news.vo.SystemChatInfo;
import qb.com.top_news.vo.User;


public class OpinionFragment extends BaseFragment {
    private View view;
    private ListView lvOpinion;
    private List<SystemChatInfo> mList;
    private MyChatAdapter mAdapter;


    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private DbUtils db;
    private User user;
    private String head;
    private String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        initData();
        return view;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.opinion_fragment, null);
        lvOpinion = (ListView) view.findViewById(R.id.lvOpinion);
        return view;
    }

    @Override
    public void initData() {
        time = DateUtils.getNowTime();
        db = MyApplication.getDb();
        try {
            mList = db.findAll(Selector.from(SystemChatInfo.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (mList.size() == 0) {
            mList = new ArrayList<>();
        }
        mAdapter = new MyChatAdapter(getActivity(), mList);
        lvOpinion.setAdapter(mAdapter);
        lvOpinion.setSelection(0);
        lvOpinion.setSelector(new ColorDrawable());
        lvOpinion.setDivider(null);
        lvOpinion.setClickable(false);
        try {
            SetHeadByDb();
            setList();
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void SetHeadByDb() throws DbException {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                SHAREDPREFERENCES_NAME, 0x0000);
        user = db.findFirst(Selector.from(User.class).where("id", "=", String.valueOf(preferences.getInt("LoginId", -1))));
        head = user.getHeadPath();
    }

    private void setList() throws DbException {
        if (mList.size() == 0) {
            SystemChatInfo m1 = new SystemChatInfo("", "1", time, "欢迎反馈，反馈时请详细描述你的问题，我们会在收到后第一时间回复您", true);
            mList.add(m1);
            mAdapter.notifyDataSetChanged();
            db.saveAll(mList);
        } else {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setTime(DateUtils.getTimesToNow(mList.get(i).getTime()));
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public void addItem(SystemChatInfo systemChatInfo) throws DbException {
        if (systemChatInfo.getHead().equals("")) {
            systemChatInfo.setHead(head);
        }
        systemChatInfo.setTime(time);
        mList.add(systemChatInfo);
        mAdapter.notifyDataSetChanged();
        db.save(systemChatInfo);
        lvOpinion.setSelection(lvOpinion.getBottom());
    }
}

package qb.com.top_news.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyChatDetailAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.utils.DateUtils;
import qb.com.top_news.view.MyTextView;
import qb.com.top_news.vo.ChatDetail;
import qb.com.top_news.vo.User;

public class ChatDetailActivity extends BaseActivity {
    private ImageView ivReturn, ivSend;
    private EditText etMessage;
    private ListView lvChat;
    private MyTextView today_title;
    private Bundle bundle;
    private List<ChatDetail> mList;
    private DbUtils db;
    private MyChatDetailAdapter mAdapter;
    private int lId;

    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private User user;
    private String head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatdetail_layout);
        initView();
        initData();
        setAdapter();
        initEvent();
    }

    @Override
    protected void initView() {
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        lvChat = (ListView) findViewById(R.id.lvChat);
        today_title = (MyTextView) findViewById(R.id.today_title);
    }

    @Override
    protected void initData() {
        lvChat.setDivider(null);
        bundle = getIntent().getExtras();
        lId = bundle.getInt("listId");
        mList = new ArrayList<>();
        db = MyApplication.getDb();
        try {
            mList = db.findAll(Selector.from(ChatDetail.class).where("listId", "=", lId));
            if (mList.size() == 0) {
                mList.add(new ChatDetail(lId, bundle.getString("name"), bundle.getString("head"), bundle.getString("time"), bundle.getString("content"), true));
                db.saveAll(mList);
            } else {
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setTime(DateUtils.getTimesToNow(mList.get(i).getTime()));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        today_title.setText(bundle.getString("name"));
        mAdapter = new MyChatDetailAdapter(this, mList);
    }

    @Override
    protected void setAdapter() {
        lvChat.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MessageActivity.class);
                finish();
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString() != null & !etMessage.getText().toString().equals("")) {
                    ChatDetail chatDetail = new ChatDetail(lId, bundle.getString("name"), "3", DateUtils.getNowTime(), etMessage.getText().toString(), false);
                    try {
                        addItem(chatDetail);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    etMessage.setText("");
                    Intent intent = new Intent("sendMs");
                    intent.putExtra("content", chatDetail.getContent());
                    intent.putExtra("time", DateUtils.getNowTime());
                    sendBroadcast(intent);
                } else {
                    Toast.makeText(ChatDetailActivity.this, "消息不可为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addItem(ChatDetail chatDetail) throws DbException {
        SetHeadByDb();
        chatDetail.setHead(head);
        mList.add(chatDetail);
        mAdapter.notifyDataSetChanged();
        db.save(chatDetail);
        lvChat.setSelection(lvChat.getBottom());
    }

    private void SetHeadByDb() throws DbException {
        SharedPreferences preferences = this.getSharedPreferences(
                SHAREDPREFERENCES_NAME, 0x0000);
        user = db.findFirst(Selector.from(User.class).where("id", "=", String.valueOf(preferences.getInt("LoginId", -1))));
        head = user.getHeadPath();
    }
}

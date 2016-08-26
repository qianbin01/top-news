package qb.com.top_news.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyContactAdapter;
import qb.com.top_news.utils.CharacterParser;
import qb.com.top_news.utils.PinyinCompare;
import qb.com.top_news.view.RightSlider;
import qb.com.top_news.vo.Contact;


public class ContactFragment extends BaseFragment {
    private View view;
    private ListView lvContact;
    private EditText etContact;
    private RightSlider mSlider;
    private TextView overlay;
    private ContentResolver mResolver;
    private List<Contact> mList, newList;
    private MyContactAdapter mAdapter;
    private CharacterParser mParser;
    private Handler mHandler;
    private OverlayRunnable mRunnable;
    private WindowManager windowManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        initData();
        setAdapter();
        initEvent();
        return view;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.contact_fragment, null);
        lvContact = (ListView) view.findViewById(R.id.ivContact);
        mSlider = (RightSlider) view.findViewById(R.id.lvRight);
        etContact = (EditText) view.findViewById(R.id.etContact);
        overlay= (TextView) view.findViewById(R.id.tvOverlay);
        return view;
    }

    @Override
    public void initData() {
        overlay.setVisibility(View.INVISIBLE);
        mResolver = getActivity().getContentResolver();
        mList = new ArrayList<>();
        mParser = new CharacterParser();
        mHandler = new Handler();
        mRunnable = new OverlayRunnable();
        getPhoneContact();
        mAdapter = new MyContactAdapter(getActivity(), mList);
    }

    public void getPhoneContact() {
        //若系统为4.4及以上使用phonebook_label字段获取sortkey
        Cursor mCursor = mResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "phonebook_label", "contact_id",
                "data1"}, null, null, "sort_key");
        while (mCursor.moveToNext()) {
            //读取通讯录的姓名
            String name = mCursor.getString(mCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String phone = mCursor.getString(mCursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String sortKey = mCursor.getString(1);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setSortKey(sortKey);
            contact.setPhone(phone);
            mList.add(contact);
        }
        mCursor.close();
    }

    public void setAdapter() {
        lvContact.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mSlider.setOnTouchingLetterChangedListener(new RightSlider.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                if (MyContactAdapter.hmAlpha.get(s) != null) {
                    int position = (int) MyContactAdapter.hmAlpha.get(s);
                    lvContact.setSelection(position);
                    overlay.setText(MyContactAdapter.sections[position]);
                    overlay.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(mRunnable, 1500);

                }

            }
        });
        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;
                new AlertDialog.Builder(getActivity()).setTitle("分享应用链接给通讯录好友").setItems(new String[]{"360手机助手", "安卓市场"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent mIntent = new Intent(Intent.ACTION_VIEW);
                        mIntent.setType("vnd.android-dir/mms-sms");
                        switch (which) {
                            case 0:
                                mIntent.putExtra("sms_body", "分享链接，赶快点击下载 www.baidu.com(360手机助手)");
                                if (newList != null) {
                                    mIntent.putExtra("address", newList.get(index).getPhone());
                                    getActivity().startActivity(mIntent);
                                } else {
                                    mIntent.putExtra("address", mList.get(index).getPhone());
                                    getActivity().startActivity(mIntent);
                                }
                                break;
                            case 1:
                                mIntent.putExtra("sms_body", "分享链接，赶快点击下载 www.baidu.com(安卓市场)");
                                if (newList != null) {
                                    mIntent.putExtra("address", newList.get(index).getPhone());
                                    getActivity().startActivity(mIntent);
                                } else {
                                    mIntent.putExtra("address", mList.get(index).getPhone());
                                    getActivity().startActivity(mIntent);
                                }
                                break;
                        }
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });
    }

    private void filterData(String filterStr) {
        newList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            newList = mList;
        } else {
            newList.clear();
            for (Contact contact : mList) {
                String name = contact.getName();
                if (name.toUpperCase().contains(filterStr.toUpperCase())
                        || mParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toUpperCase())) {
                    newList.add(contact);
                }
            }
        }
        Collections.sort(newList, new PinyinCompare());
        lvContact.setAdapter(mAdapter);
        mAdapter.updateListView(newList);
    }

    private class OverlayRunnable implements Runnable {

        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

}

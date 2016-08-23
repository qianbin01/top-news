package qb.com.top_news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.activity.WebActivity;
import qb.com.top_news.adatper.MyListViewAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.view.MyListView;
import qb.com.top_news.vo.News;

public class NewsFragment extends BaseFragment implements MyListView.IReflashListener {

    private MyListView lvNews;
    private View view;
    private List<News> newsList;
    private MyListViewAdapter mAdapter;
    private String baseUrl = "http://v.juhe.cn/toutiao/index?key=6585a4c79cf4cc7d032d0cd3d4c400f5&type=";
    private String mUrl;
    public String url;
    private Gson gson;
    private RequestQueue mQueue;
    private Bundle mBundle;
    private int itemCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        initData();
        initEvent();
        return view;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.news_fragment, null);
        lvNews = (MyListView) view.findViewById(R.id.lvNews);
        return view;
    }

    @Override
    public void initData() {
        mQueue = MyApplication.getHttpQueue();
        newsList = new ArrayList<>();
        mBundle = new Bundle();
        gson = new GsonBuilder().setPrettyPrinting().create();
        switch (getArguments().getInt("url")) {
            case 0:
                mUrl = "top";
                mBundle.putString("flag", mUrl);
                break;
            case 1:
                mUrl = "shehui";
                mBundle.putString("flag", mUrl);
                break;
            case 2:
                mUrl = "guonei";
                mBundle.putString("flag", mUrl);
                break;
            case 3:
                mUrl = "guoji";
                mBundle.putString("flag", mUrl);
                break;
            case 4:
                mUrl = "yule";
                mBundle.putString("flag", mUrl);
                break;

        }
        url = baseUrl + mUrl;
        doVolley(url);
    }

    private void setAdapter() {
        lvNews.setAdapter(mAdapter);
    }

    public void doVolley(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray data = result.getJSONArray("data");
                    newsList = gson.fromJson(String.valueOf(data), new TypeToken<List<News>>() {
                    }.getType());
                    mAdapter = new MyListViewAdapter(getActivity(), newsList);
                    mAdapter.notifyDataSetChanged();
                    setAdapter();
                    System.out.println("网络无脑测试中");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.toString());
            }
        });
        jsonObjectRequest.setTag(mUrl);
        mQueue.add(jsonObjectRequest);
    }

    protected void initEvent() {
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBundle.putString("url", newsList.get(position-1).getUrl());//因为加了header position顺序移动1位
                openActivity(WebActivity.class, mBundle);
            }
        });

        lvNews.setInterface(this);

    }

    public String getUrl() {
        return url;
    }

    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lvNews.bottomFlag) {
                    itemCount = mAdapter.getCount();
                    if (itemCount + 10 > mAdapter.MAX_ITEM) {
                        mAdapter.count = mAdapter.MAX_ITEM;
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "对不起，暂时没有新数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mAdapter.count = itemCount + 10;
                        mAdapter.notifyDataSetChanged();
                    }

                }else if(lvNews.topFlag){
                    Toast.makeText(getActivity(), "下拉刷新测试中", Toast.LENGTH_SHORT).show();
                }
                lvNews.reflashCompleted();
            }
        }, 2000);

    }

}

package qb.com.top_news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import qb.com.top_news.vo.News;

/**
 * Created by qianbin on 16/8/9.
 */
public class NewsFragment extends BaseFragment {

    private ListView lvNews;
    private View view;
    private List<News> newsList;
    private MyListViewAdapter mAdapter;
    private String baseUrl = "http://v.juhe.cn/toutiao/index?key=6585a4c79cf4cc7d032d0cd3d4c400f5&type=";
    private String mUrl, url;
    private Gson gson;
    private RequestQueue mQueue;

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
        lvNews = (ListView) view.findViewById(R.id.lvNews);
        return view;
    }

    @Override
    public void initData() {
        mQueue = MyApplication.getHttpQueue();
        newsList = new ArrayList<News>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        switch (getArguments().getInt("flag")) {
            case 0:
                mUrl = "top";
                break;
            case 1:
                mUrl = "shehui";
                break;
            case 2:
                mUrl = "guonei";
                break;
            case 3:
                mUrl = "guoji";
                break;
            case 4:
                mUrl = "yule";
                break;

        }
        url = baseUrl + mUrl;
        doVolley(url);
    }

    private void setAdapter() {
        lvNews.setAdapter(mAdapter);
    }

    private void doVolley(String url) {
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
                Bundle bundle = new Bundle();
                bundle.putString("url", newsList.get(position).getUrl());
                openActivity(WebActivity.class, bundle);
            }
        });
    }


}

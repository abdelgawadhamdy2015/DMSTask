package com.abdelgawad.DMS.Task.mData;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.abdelgawad.DMS.Task.mRecycler.HttpHandler;
import com.abdelgawad.DMS.Task.mRecycler.MyAdapter;
import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Oclemy on 12/8/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class Paginator {

    private JSONArray array;
    private Context c;
    private PullToLoadView pullToLoadView;
    private MyAdapter adapter;
    private boolean isLoading = false;
    private boolean hasLoadedAll = false;
    private int nextPage;
    private int i = 0;

    public Paginator(final Context c, final PullToLoadView pullToLoadView) {
        this.c = c;
        this.pullToLoadView = pullToLoadView;
        RecyclerView rv = pullToLoadView.getRecyclerView();
        rv.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.VERTICAL, false));
        adapter = new MyAdapter(c);
        rv.setAdapter(adapter);
        new GetReposArray().execute();
        isLoading = true;
        initializePaginator();
    }

    /*
    PAGE DATA
     */
    public void initializePaginator() {
        pullToLoadView.isLoadMoreEnabled(true);
        pullToLoadView.setPullCallback(new PullCallback() {
            //LOAD MORE DATA
            @Override
            public void onLoadMore() {
                if (i < array.length()) {
                    loadData(nextPage);
                } else {
                    Snackbar.make(pullToLoadView, "no more repos", Snackbar.LENGTH_SHORT).show();
                    isLoading = false;
                    pullToLoadView.setComplete();
                }
            }

            //REFRESH AND TAKE US TO FIRST PAGE
            @Override
            public void onRefresh() {
                new GetReposArray().execute();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        i = 0;
                        adapter.clear();
                        hasLoadedAll = false;
                        loadData(1);
                    }
                }, 3000);

            }

            //IS LOADING
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            //CURRENT PAGE LOADED
            @Override
            public boolean hasLoadedAllItems() {
                return hasLoadedAll;
            }
        });

        pullToLoadView.initLoad();
    }

    /*
     LOAD MORE DATA
     SIMULATE USING HANDLERS
     */
    public void loadData(final int page) {

        isLoading = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (gitRepos(i) == null) {
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 2000);
                    } else {
                        break;
                    }
                }

                int j = 0;
                while (true) {

                    if (j < 10) {

                        adapter.add(gitRepos(i));
                        i++;
                        j++;
                    } else {
                        i++;
                        break;
                    }
                }
                //UPDATE PROPETIES
                pullToLoadView.setComplete();
                isLoading = false;
                nextPage = page + 1;

            }
        }, 3000);
    }

    // handle git repos
    private HashMap<String, String> gitRepos(int i) {

        try {
            if (i < array.length()) {
                //  save repos in hashListList
                JSONObject repo = array.getJSONObject(i);
                String name = repo.getString(Constants.REPO_NAME);
                String desc = repo.getString(Constants.REPO_DESC);
                String repoUrl = repo.getString(Constants.REPO_URL);
                boolean fork = repo.getBoolean(Constants.REPO_FORK);
                Log.e("fork main", fork + "");
                JSONObject owner = repo.getJSONObject(Constants.REPO_OWNER);
                String userName = owner.getString(Constants.OWNER_LOGIN);
                String ownerUrl = owner.getString(Constants.OWNER_URL);
                // save data in HashMap
                HashMap<String, String> repoMap = new HashMap<>();
                repoMap.put(Constants.REPO_NAME, name);
                repoMap.put(Constants.REPO_DESC, desc);
                repoMap.put(Constants.OWNER_LOGIN, userName);
                repoMap.put(Constants.REPOSITORY_URL, repoUrl);
                repoMap.put(Constants.OWNER_URL, ownerUrl);
                repoMap.put(Constants.REPO_FORK, fork + "");
                return repoMap;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetReposArray extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            // send request url to handler
            String reqUrl = "https://api.github.com/users/square/repos";

            String jsonStr = handler.makeServiceCall(reqUrl);
            try {
                array = new JSONArray(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

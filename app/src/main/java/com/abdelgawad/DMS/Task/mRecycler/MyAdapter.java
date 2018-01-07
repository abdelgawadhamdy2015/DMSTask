package com.abdelgawad.DMS.Task.mRecycler;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.abdelgawad.DMS.Task.mData.Constants;
import com.abdelgawad.DMS.Task.R;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Oclemmy on 12/9/2016 for ProgrammingWizards Channel and http://www.Camposha.info.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    int pos;
    private Context c;
    private ArrayList<HashMap<String, String>> repoList;
    private View v;
    /*
    CONSTRUCTOR
     */
    public MyAdapter(Context c) {
        this.c = c;
        this.repoList = new ArrayList<>();
    }

    //INITIALIE VH
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder ;
         v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        if (repoList.size() != 0) {
            holder = new MyHolder(v);
            return holder;
        }else {
            Snackbar.make(v," no other data",Snackbar.LENGTH_SHORT).show();
            return null ;
        }
    }

    //BIND DATA
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        pos = position;
        if (holder!=null&&repoList!=null&&repoList.size()!=0) {
            String forkstr = repoList.get(pos).get(Constants.REPO_FORK);
            boolean fork = Boolean.parseBoolean(forkstr);
            if (!fork) {
                v.setBackgroundColor(c.getResources().getColor(R.color.White_green));
            } else {
                v.setBackgroundColor(Color.WHITE);
            }
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Dialog dialog = new Dialog(c);
                    dialog.setCancelable(true);
                    dialog.setTitle("alert");
                    dialog.setContentView(R.layout.dialog);
                    Button repository = (Button) dialog.findViewById(R.id.repository);
                    // handle repository click
                    repository.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = repoList.get(pos).get(Constants.REPOSITORY_URL);
                            Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            c.startActivity(intentBrowser);
                            Toast.makeText(c, url, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Button owner = (Button) dialog.findViewById(R.id.owner);
                    // handle owner click
                    owner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = repoList.get(pos).get(Constants.OWNER_URL);
                            Intent intentBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            c.startActivity(intentBrowser);
                            Toast.makeText(c, url, Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.show();

                    return true;
                }
            });


            holder.nametxt.setText(repoList.get(position).get(Constants.REPO_NAME));
            holder.login.setText(repoList.get(position).get(Constants.OWNER_LOGIN));
            holder.description.setText(repoList.get(position).get(Constants.REPO_DESC));

        }

    }

    /*
    TOTAL ITEMS
     */
    @Override
    public int getItemCount() {
        return repoList.size();

    }

    /*
    ADD DATA TO ADAPTER
     */
    public void add(HashMap<String, String> s) {
        if (s!=null){
            if (s.size()!=0){
                repoList.add(s);
                notifyDataSetChanged();
            }
        }

    }

    /*
    CLEAR DATA FROM ADAPTER
     */
    public void clear() {
        repoList.clear();
        notifyDataSetChanged();
    }

    /*
    VIEW HOLDER CLASS
     */
    class MyHolder extends RecyclerView.ViewHolder {

        TextView nametxt, login, description;

        public MyHolder(View itemView) {
            super(itemView);
            this.nametxt = (TextView) itemView.findViewById(R.id.name);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.login = (TextView) itemView.findViewById(R.id.user_name);

        }
    }

}

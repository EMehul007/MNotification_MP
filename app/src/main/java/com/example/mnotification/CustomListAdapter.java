package com.example.mnotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by mukesh on 18/5/15.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.UserViewHolder> {

    private Context context;
    private List<Model> arModelList;

    public CustomListAdapter(Context context, List<Model> arModelList) {
        this.context = context;
        this.arModelList = arModelList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    //here error will come in get so just clear it and then write getLeave_List_Month.this will directly give the suggestion
    public void onBindViewHolder(final UserViewHolder holder, final int position) {


        Model m = arModelList.get(position);

        holder.Itemname.setText(m.getText());


        holder.Itemname_title.setText(m.getName());

        holder.icon.setImageBitmap(m.getImage());
    }

    @Override
    public int getItemCount() {
        return arModelList.size();

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView Itemname, Itemname_title;

        ImageView icon;

        public UserViewHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.icon);
            Itemname = (TextView) v.findViewById(R.id.Itemname);
            Itemname_title = (TextView) v.findViewById(R.id.Itemname_title);


        }
    }


}
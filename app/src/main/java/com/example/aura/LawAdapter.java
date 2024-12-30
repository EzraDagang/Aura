package com.example.aura;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


class LawAdapter extends RecyclerView.Adapter<LawAdapter.MyViewHolder>{
    Context context;
    ArrayList<LawItem> lawFullItem;

    public LawAdapter(Context context, ArrayList<LawItem> lawFullItem){
        this.context = context;
        this.lawFullItem = lawFullItem;

    }

    @NonNull
    @Override
    public LawAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if(viewType % 2 == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_even_layout,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_even_layout,parent,false);
        }
        return new LawAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LawAdapter.MyViewHolder holder, int position) {
           holder.title.setText(lawFullItem.get(position).getTitle());
           holder.description.setText(lawFullItem.get(position).getDescription());
           holder.year.setText(lawFullItem.get(position).getYear());

    }

    @Override
    public int getItemCount() {
        return lawFullItem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,year;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            year = itemView.findViewById(R.id.year);
        }
    }
}
/*

public class LawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<LawItem> lawList;
    private final List<LawItem> lawListFull; // Backup list for search filtering

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ALTERNATE = 1;

    public LawAdapter(List<LawItem> lawList) {
        this.lawList = new ArrayList<>(lawList);
        this.lawListFull = new ArrayList<>(lawList);
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 2 == 0) ? TYPE_ALTERNATE : TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ALTERNATE) {
            View view = inflater.inflate(R.layout.item_alternate_layout, parent, false);
            return new AlternateViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_normal_layout, parent, false);
            return new NormalViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LawItem item = lawList.get(position);
        if (holder instanceof NormalViewHolder) {
            ((NormalViewHolder) holder).bind(item);
        } else if (holder instanceof AlternateViewHolder) {
            ((AlternateViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return lawList.size();
    }

    public void filter(String query) {
        lawList.clear();
        if (query.isEmpty()) {
            lawList.addAll(lawListFull);
        } else {
            for (LawItem item : lawListFull) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    lawList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class NormalViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, description, year;

        NormalViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            year = itemView.findViewById(R.id.year);
        }

        void bind(LawItem item) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            year.setText(item.getYear());
        }
    }

    static class AlternateViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, description, year;

        AlternateViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            year = itemView.findViewById(R.id.year);
        }

        void bind(LawItem item) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            year.setText(item.getYear());
        }
    }
}


 */
package adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemoncatcherscatalogue.NewDeckActivity;
import com.example.pokemoncatcherscatalogue.R;

import java.util.List;

import models.ParseCard;
import models.DragListener;
import models.Listener;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder>
        implements View.OnTouchListener {

    public static final String TAG = "ListAdapter";
    private List<ParseCard> list;
    private Listener listener;
    private Context context;

    public ListAdapter(List<ParseCard> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.deck_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Log.i(TAG, list.get(position).getUrl());
        holder.tvCardName.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getUrl()).into(holder.ivCardView);
        holder.rlCardItem.setTag(position);
        holder.rlCardItem.setOnTouchListener(this);
        holder.rlCardItem.setOnDragListener(new DragListener(listener));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && NewDeckActivity.scroll) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            } else {
                v.startDrag(data, shadowBuilder, v, 0);
            }
            return true;
        }
        return false;
    }

    public List<ParseCard> getList() {
        return list;
    }

    public void updateList(List<ParseCard> list) {
        this.list = list;
    }

    public DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCardName;
        public ImageView ivCardView;
        public RelativeLayout rlCardItem;

        ListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            // Link up views
            tvCardName = itemView.findViewById(R.id.tvCardName);
            ivCardView = itemView.findViewById(R.id.ivCardView);
            rlCardItem = itemView.findViewById(R.id.rlCardItem);
        }
    }
}

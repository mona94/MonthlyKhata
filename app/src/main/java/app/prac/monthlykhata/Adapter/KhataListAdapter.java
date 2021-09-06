package app.prac.monthlykhata.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.prac.monthlykhata.BeanFile.Khata;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class KhataListAdapter extends RecyclerView.Adapter<KhataListAdapter.ViewHolder> {

    private ArrayList<Khata> list;
    Context mContext;
    callAction mcallAction;

    public KhataListAdapter(ArrayList<Khata> list, Context mContext, callAction mcallAction) {
        this.list = list;
        this.mcallAction = mcallAction;
        this.mContext = mContext;
    }

    public void setlist(ArrayList<Khata> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.khata_list_lyt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, @SuppressLint("RecyclerView") int position) {
        vh.txtName.setText(list.get(position).getName());
        vh.txtKhataDate.setText(list.get(position).getkhataDate());
        vh.lytContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcallAction != null) {
                    mcallAction.openTransaction(list.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }


    @Override
    public int getItemCount() {
        int size = 0;
        if (list != null)
            size = list.size();
        return size;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtKhataDate)
        TextView txtKhataDate;
        @BindView(R.id.lytContent)
        LinearLayout lytContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface callAction {
        public void openTransaction(String id);
    }
}
package app.prac.monthlykhata.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.prac.monthlykhata.BeanFile.TransactionBean;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TransanctionListAdapeter extends RecyclerView.Adapter<TransanctionListAdapeter.ViewHolder> {

    private ArrayList<TransactionBean> list;
    Context mContext;
    callAction mcallAction;

    public TransanctionListAdapeter(ArrayList<TransactionBean> list, Context mContext, callAction mcallAction) {
        this.list = list;
        this.mcallAction = mcallAction;
        this.mContext = mContext;
    }

    public void setlist(ArrayList<TransactionBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_list_lyt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, @SuppressLint("RecyclerView") int position) {
        vh.txtAmount.setText("Amount: " + list.get(position).getAmount());
        vh.txtGdate.setText(list.get(position).getDate());
        vh.txtGnote.setText(list.get(position).getNote());

        if (list.get(position).getType().matches("received")) {
            vh.lytCredit.setVisibility(View.VISIBLE);
            vh.lytDebit.setVisibility(View.GONE);
        } else if (list.get(position).getType().matches("paid")) {
            vh.lytCredit.setVisibility(View.GONE);
            vh.lytDebit.setVisibility(View.VISIBLE);
        }
        vh.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcallAction != null) {
                    mcallAction.callDelete(list.get(position).getId());
                }
            }
        });
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
        @BindView(R.id.txtGnote)
        TextView txtGnote;
        @BindView(R.id.txtGdate)
        TextView txtGdate;
        @BindView(R.id.txtAmount)
        TextView txtAmount;
        @BindView(R.id.lytCredit)
        RelativeLayout lytCredit;
        @BindView(R.id.lytDebit)
        RelativeLayout lytDebit;
        @BindView(R.id.imgdelete)
        ImageView imgdelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface callAction {
        public void callDelete(String id);
    }
}
package app.prac.monthlykhata.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import app.prac.monthlykhata.Adapter.TransanctionListAdapeter;
import app.prac.monthlykhata.BeanFile.Khata;
import app.prac.monthlykhata.BeanFile.TransactionBean;
import app.prac.monthlykhata.Extra.Utils;
import app.prac.monthlykhata.MonthlyKhataApp;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionList extends AppCompatActivity implements TransanctionListAdapeter.callAction {

    @BindView(R.id.txt_Customer)
    TextView txt_Customer;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.txt_paid)
    TextView txt_paid;
    @BindView(R.id.txtFinal)
    TextView txtFinal;
    @BindView(R.id.txt_received)
    TextView txt_received;
    private Khata mkhata;
    private TransanctionListAdapeter mAdapter;
    private ArrayList<TransactionBean> mList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private QuerySnapshot mqueryDocumentSnapshots;
    private String TAG = " Transaction List";
    private TransactionBean mGive;
    int paid = 0, received = 0, finalamt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();
        mAuth = MonthlyKhataApp.getFireBaseAuth();

        if (getIntent().hasExtra("khata")) {
            Gson gson = new Gson();
            mkhata = gson.fromJson(getIntent().getStringExtra("khata"), Khata.class);
        } else {
            return;
        }
        txt_Customer.setText(mkhata.getName());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionList.this, AddTransaction.class);
                intent.putExtra("id", mkhata.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TransanctionListAdapeter(mList, this, this);
        mRecyclerView.setAdapter(mAdapter);
        getTransactionList();
    }

    @OnClick(R.id.txt_Report)
    void txt_Report() {
        // TODO call server...
        Gson gson = new Gson();
        String mKhata = gson.toJson(mkhata, Khata.class);
        Intent intent = new Intent(TransactionList.this, ReportActivity.class);
        intent.putExtra("khata", mKhata);
//        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getTransactionList();
    }

    private void getTransactionList() {
        db.collection("Account").document(mAuth.getCurrentUser().getUid()).collection("Khata").document(mkhata.getId()).collection("Transaction")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    mList = new ArrayList<>();
                    mqueryDocumentSnapshots = queryDocumentSnapshots;
                    for (int i = 0; i < mqueryDocumentSnapshots.size(); i++) {
                        mGive = new TransactionBean();
                        mGive.setAmount(mqueryDocumentSnapshots.getDocuments().get(i).get("amount").toString());
                        mGive.setDate(mqueryDocumentSnapshots.getDocuments().get(i).get("date").toString());
                        mGive.setNote(mqueryDocumentSnapshots.getDocuments().get(i).get("tag").toString());
                        mGive.setType(mqueryDocumentSnapshots.getDocuments().get(i).get("type").toString());
                        mGive.setId(mqueryDocumentSnapshots.getDocuments().get(i).getId());
                        mList.add(mGive);
                    }
                    mAdapter.setlist(mList);
                    mAdapter.notifyDataSetChanged();
                    setRPAmount();
                    Log.e(TAG, "Error getting documents. ");
                }
            }
        });

    }

    private void setRPAmount() {
        paid = 0;
        received = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getType().matches("received")) {
                received = received + Integer.parseInt(mList.get(i).getAmount());
            } else if (mList.get(i).getType().matches("paid")) {
                paid = paid + Integer.parseInt(mList.get(i).getAmount());
            }
        }

        if (paid > received) {
            finalamt = paid - received;
            txtFinal.setText("You will get Rs. "+finalamt);
        } else if (received > paid) {
            finalamt = received - paid;
            txtFinal.setText("You will pay Rs. "+finalamt);
        }
        txt_paid.setText("Amount Paid: " + paid);
        txt_received.setText("Amount Received: " + received);
    }

    @Override
    public void callDelete(String id) {
        openDialogBox(id);
    }
    public void openDialogBox(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this transaction?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                        db.collection("Account")
                                .document(MonthlyKhataApp.getFireBaseAuth().getCurrentUser().getUid())
                                .collection("Khata")
                                .document(mkhata.getId())
                                .collection("Transaction")
                                .document(id).delete();
                        getTransactionList();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(this.getResources().getDrawable(R.drawable.delete))
                .show();
    }



}

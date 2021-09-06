package app.prac.monthlykhata.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import app.prac.monthlykhata.Extra.Utils;
import app.prac.monthlykhata.MonthlyKhataApp;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkArgument;


public class AddTransaction extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.edt_note)
    EditText edt_note;
    @BindView(R.id.edt_date)
    EditText edt_date;
    @BindView(R.id.btnProceed)
    Button btnProceed;
    @BindView(R.id.rd_received)
    RadioButton rd_received;
    @BindView(R.id.rd_paid)
    RadioButton rd_paid;
    String date;
    private DatePickerDialog datePickerDialog;
    private String obj_id;
    private String TAG = "Transaction Amount Class";
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        if (getIntent().hasExtra("id")) {
            obj_id = getIntent().getExtras().getString("id");
        } else {
            return;
        }
        ButterKnife.bind(this);
        btnProceed.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        int mYear = newCalendar.get(Calendar.YEAR);
        int mMonth = newCalendar.get(Calendar.MONTH);
        int mDate = newCalendar.get(Calendar.DAY_OF_MONTH);
        date = mDate + getDayOfMonthSuffix(mDate) + " " + formatMonth(mMonth) + " , " + mYear;
        edt_date.setText(mDate + getDayOfMonthSuffix(mDate) + " " + formatMonth(mMonth) + " , " + mYear);
        datePickerDialog = new DatePickerDialog(
                this, this, mYear, mMonth, mDate);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public String formatMonth(int mMonth) {
        switch (mMonth) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return "Month";
    }

    @Override
    public void onClick(View v) {
        if (v == edt_date) {
            datePickerDialog.show();
        }
        if (v == btnProceed) {
            if (edt_amount.getText().toString().isEmpty()) {
                Utils.snackBarText(AddTransaction.this, "Enter Amount!").show();
            } else if (edt_note.getText().toString().isEmpty()) {
                Utils.snackBarText(AddTransaction.this, "Enter Tag!").show();
            } else if (date.isEmpty()) {
                Utils.snackBarText(AddTransaction.this, "Select Date!").show();
            } else {
                saveTransaction();
            }

        }
    }

    private void saveTransaction() {
        if (rd_received.isChecked()) {
            type = "received";
        } else if (rd_paid.isChecked()) {
            type = "paid";
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("amount", edt_amount.getText().toString());
        user.put("tag", edt_note.getText().toString());
        user.put("date", date);
        user.put("type", type);

        db.collection("Account").document(MonthlyKhataApp.getFireBaseAuth().getCurrentUser().getUid()).collection("Khata").document(obj_id).collection("Transaction")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Utils.snackBarText(AddTransaction.this, "Transaction Added Successfully!").show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document" + e.getMessage());
                    }
                });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + getDayOfMonthSuffix(dayOfMonth) + " " + formatMonth(month) + " , " + year;
        edt_date.setText(dayOfMonth + getDayOfMonthSuffix(dayOfMonth) + " " + formatMonth(month) + " , " + year);
    }
}

package app.prac.monthlykhata.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.prac.monthlykhata.Extra.Utils;
import app.prac.monthlykhata.MonthlyKhataApp;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddKhata extends AppCompatActivity {

    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    private String name;
    String TAG = "Firebase Message";
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_khata);
        ButterKnife.bind(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edt_name.getText().toString();
                if (name.isEmpty()) {
                    Utils.snackBarText(AddKhata.this, "Please enter name!").show();
                } else {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    currentDate = df.format(c);
                    add_customer();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void add_customer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("khata_date", currentDate);
        db.collection("Account").document(MonthlyKhataApp.getFireBaseAuth().getCurrentUser().getUid()).collection("Khata")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Utils.snackBarText(AddKhata.this, "Khata added successfully!").show();
                        finish();
                        Log.e(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding document" + e.getMessage());
                    }
                });
    }
}

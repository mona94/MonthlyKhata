package app.prac.monthlykhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.prac.monthlykhata.Extra.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBusinessDetail extends AppCompatActivity {

    @BindView(R.id.edt_bus_name)
    EditText edt_bus_name;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.btnProceed)
    Button btnProceed;
    private String b_name, name;
    String TAG = "Firebase Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_detail);
        ButterKnife.bind(this);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_name = edt_bus_name.getText().toString();
                name = edt_name.getText().toString();
                if (b_name.isEmpty()) {
                    Utils.snackBarText(AddBusinessDetail.this, "Please enter Business name!").show();
                } else if (name.isEmpty()) {
                    Utils.snackBarText(AddBusinessDetail.this, "Please enter Name!").show();
                } else {
                    FirebaseUser mCurrentAcount = MonthlyKhataApp.getFireBaseAuth().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> account = new HashMap<>();
                    account.put("name", name);
                    account.put("b_name", b_name);
                    account.put("phone", mCurrentAcount.getPhoneNumber());

                    db.collection("Account").document(mCurrentAcount.getUid())
                            .set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "DocumentSnapshot added ");

                            Intent intent = new Intent(AddBusinessDetail.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "DocumentSnapshot error ");
                        }
                    });
                }
            }
        });
    }
}

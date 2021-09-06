package app.prac.monthlykhata.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.prac.monthlykhata.Extra.Utils;
import app.prac.monthlykhata.MonthlyKhataApp;
import app.prac.monthlykhata.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingScreen extends AppCompatActivity {

    @BindView(R.id.txtBName)
    TextView txtBName;
    @BindView(R.id.txtName)
    TextView txtName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        ButterKnife.bind(this);
        mAuth = MonthlyKhataApp.getFireBaseAuth();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Account").document(mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                txtBName.setText(documentSnapshot.getString("b_name"));
                txtName.setText(documentSnapshot.getString("name"));
            }
        });
    }

    @OnClick(R.id.btnLogout)
    public void btnLogout() {
        MonthlyKhataApp.getFireBaseAuth().signOut();
        Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
        Intent mIntent = new Intent(SettingScreen.this, PhoneAuth.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        finish();
    }


}
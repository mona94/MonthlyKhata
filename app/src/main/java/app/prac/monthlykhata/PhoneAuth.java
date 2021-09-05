package app.prac.monthlykhata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import app.prac.monthlykhata.Extra.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneAuth extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edtPhone)
    TextView edtPhone;

    @BindView(R.id.btnProceed)
    Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ButterKnife.bind(this);
        btnProceed.setOnClickListener(this);
    }


    public void phoneAuthMethod() {
        String phoneNumber = edtPhone.getText().toString();
        if (phoneNumber.isEmpty()) {
            Utils.snackBarText(PhoneAuth.this, "Please enter phone number!").show();
        } else {
            Intent intent = new Intent(PhoneAuth.this, VerifyPhone.class);
            intent.putExtra("mobile", phoneNumber);
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        phoneAuthMethod();
    }
}

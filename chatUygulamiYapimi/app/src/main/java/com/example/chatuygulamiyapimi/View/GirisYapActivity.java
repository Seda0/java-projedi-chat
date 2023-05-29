package com.example.chatuygulamiyapimi.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chatuygulamiyapimi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisYapActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgress;
    private TextInputLayout inputEmail,inputSifre;
    private EditText editEmail,editSifre;
    private String txtEmail,txtSifre;
    private LinearLayout mLinear;
    private void init(){
        mLinear=findViewById(R.id.girisYaplinear);

        inputEmail=findViewById(R.id.girisYapEmail);
        inputSifre=findViewById(R.id.girisYapSifre);
        editEmail=findViewById(R.id.girisYapeditEmail);
        editSifre=findViewById(R.id.girisYapeditSifre);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);
        init();

    }

    public void btnGirisYap(View view) {
        txtEmail=editEmail.getText().toString();
        txtSifre=editSifre.getText().toString();

        if(!TextUtils.isEmpty(txtEmail)){
            if(!TextUtils.isEmpty(txtSifre)){

                mProgress=new ProgressDialog(this);
                mProgress.setTitle("giriş yapılıyor");
                mProgress.show();
                mAuth.signInWithEmailAndPassword(txtEmail,txtSifre).addOnCompleteListener(GirisYapActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ProgressAyar();
                            Toast.makeText(GirisYapActivity.this, "giris basarılı", Toast.LENGTH_SHORT).show();
                              finish();
                              startActivity(new Intent(GirisYapActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }else{
                            ProgressAyar();
                            Snackbar.make(mLinear,task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });

            }else inputSifre.setError("dogru şifreyi giriniz");
        }else
            inputEmail.setError("dogru email giriniz");
    }

    public void btnGitKayitOl(View view) {
        startActivity(new Intent(GirisYapActivity.this,KayitOlActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }
    private void ProgressAyar(){
      if(mProgress.isShowing())
          mProgress.dismiss();

    }
}
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

import com.example.chatuygulamiyapimi.Model.Kullanici;
import com.example.chatuygulamiyapimi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class KayitOlActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
     private ProgressDialog mProgress;
     private Kullanici mKullanici;
     private LinearLayout mLinear;
     private FirebaseAuth mAuth;
    private TextInputLayout inputisim,inputEmail,inputSifre,inputSifre1,inputYil;
    private EditText editIsim,editEmail,editSifre,editSifreTekrar,editYil;
    private String txtIsim,txtEmail,txtSifre,txtSifre1,txtYil;
    private FirebaseUser mUser;
    private void init(){
        mAuth=FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mLinear=findViewById(R.id.kayitOllinear);
        inputisim=findViewById(R.id.kayitOlisim);
        inputSifre=findViewById(R.id.kayitOlSifre);
        inputEmail=findViewById(R.id.kayitOlEmail);
        inputSifre1=findViewById(R.id.kayitOlSifre1);
        inputYil=findViewById(R.id.kayitOlYil);
        editIsim=findViewById(R.id.kayitOleditisim);
        editEmail=findViewById(R.id.kayitOleditEmail);
        editSifre=findViewById(R.id.kayitOleditSifre);
        editSifreTekrar=findViewById(R.id.kayitOleditSifre1);
        editYil=findViewById(R.id.kayitOleditYil);





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        init();

    }

    public void btnKayitOl(View view) {
        txtIsim=editIsim.getText().toString();
        txtEmail=editEmail.getText().toString();
        txtSifre=editSifre.getText().toString();
        txtSifre1=editSifreTekrar.getText().toString();
        txtYil=editYil.getText().toString();
        if(!TextUtils.isEmpty(txtIsim)){
            if(!TextUtils.isEmpty(txtEmail)){
                if(!TextUtils.isEmpty(txtSifre)){
                    if(!TextUtils.isEmpty(txtSifre1)){
                        if(!TextUtils.isEmpty(txtYil)){

                          if(txtSifre.equals(txtSifre1)){
                              mProgress=new ProgressDialog(this);
                              mProgress.setTitle("kayit olunuyor");
                              mProgress.show();

                              mAuth.createUserWithEmailAndPassword(txtEmail,txtSifre).addOnCompleteListener(KayitOlActivity.this, new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        mUser=mAuth.getCurrentUser();
                                       if(mUser !=null){
                                           mKullanici=new Kullanici(txtIsim,txtEmail,mUser.getUid(),"default");
                                           mFirestore.collection("kullanicilar").document(mUser.getUid()).set(mKullanici)
                                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                              if(task.isSuccessful()){
                                                                  ProgressAyar();
                                                                  Toast.makeText(KayitOlActivity.this, "kayit oldunuz", Toast.LENGTH_SHORT).show();
                                                                  finish();
                                                                  startActivity(new Intent(KayitOlActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                              }else{
                                                                  ProgressAyar();

                                                                  Snackbar.make(mLinear,task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();
                                                              }



                                                       }
                                                   });
                                       }

                                    }else
                                    {
                                        ProgressAyar();

                                        Snackbar.make(mLinear,task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();

                                    }
                                  }
                              });

                          }else
                              Snackbar.make(mLinear,"şifreler aynı değil",Snackbar.LENGTH_SHORT).show();




                        }else
                            inputYil.setError("dogum tarihi boş bırakılamaz");
                    }
                    else
                        inputSifre1.setError("şifre boş bırakılamaz");
                }else
                    inputSifre.setError("şifre boş bırakılamaz");

            }
            else
                inputEmail.setError("e mail boş bırakılamaz");
        }else
            inputisim.setError("isim boş bırakılamaz");
    }
    private void ProgressAyar(){
        if(mProgress.isShowing())

        mProgress.dismiss();
    }
}
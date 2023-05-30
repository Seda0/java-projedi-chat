package com.example.chatuygulamiyapimi.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatuygulamiyapimi.Model.Kullanici;
import com.example.chatuygulamiyapimi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilFragment extends Fragment {
    private EditText editIsim,editEmail;
    private CircleImageView imgProfil;
    private View v;
    private FirebaseFirestore mFireStore;
    private DocumentReference mRef;
    private FirebaseUser mUser;
    private Kullanici user;
    private ImageView imgYeniResim;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       v = inflater.inflate(R.layout.fragment_profil, container, false);
       editIsim = v.findViewById(R.id.profil_fragment_editIsim);
        editEmail = v.findViewById(R.id.profil_fragment_editEmail);
        imgProfil = v.findViewById(R.id.profil_fragment_imgUserProfil);
        imgYeniResim = v.findViewById(R.id.profil_fragment_imgYeniResim);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFireStore = FirebaseFirestore.getInstance();

        mRef = mFireStore.collection("kullanicilar").document(mUser.getUid());
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){

                    Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if( value != null && value.exists()){
                    user = value.toObject(Kullanici.class);

                    if(user != null){
                        editIsim.setText(user.getKullaniciIsmi());
                        editEmail.setText(user.getKullaniciEmail());
                        if(user.getKullaniciProfil().equals("default"))
                            imgProfil.setImageResource(R.mipmap.ic_launcher);
                        else
                            Picasso.get().load(user.getKullaniciProfil()).resize(156,156).into(imgProfil);

                    }
                }
            }
        });
        imgYeniResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}
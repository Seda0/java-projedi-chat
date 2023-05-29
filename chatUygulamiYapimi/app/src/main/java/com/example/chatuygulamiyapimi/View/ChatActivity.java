package com.example.chatuygulamiyapimi.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatuygulamiyapimi.Adapter.ChatAdapter;
import com.example.chatuygulamiyapimi.Model.Chat;
import com.example.chatuygulamiyapimi.Model.Kullanici;
import com.example.chatuygulamiyapimi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseUser mUser;
    private HashMap<String,Object>mData;
 private RecyclerView mRecyclerView;
 private EditText editMesaj;
 private String txtMesaj,docId;
 private CircleImageView hedefProfil;
 private TextView hedefIsim;
 private Intent gelenInten;
 private String hedefId,kanalId,hedefProfilUrl;
 private DocumentReference hedefRef;
 private Kullanici hedefKullanici;
 private FirebaseFirestore mFireStore;
 private Query chatQuery;
 private ArrayList<Chat>mChatList;
 private Chat mChat;
 private ChatAdapter chatAdapter;


 private void init(){

     mRecyclerView=(RecyclerView)findViewById(R.id.chat_activity_recyclerView);
     editMesaj=(EditText)findViewById(R.id.chat_activity_editMesaj);
     hedefProfil=(CircleImageView)findViewById(R.id.chat_activity_imgHedefProfil);
     hedefIsim=(TextView) findViewById(R.id.chat_activity_txtHedefIsim);
     mUser= FirebaseAuth.getInstance().getCurrentUser();
     mFireStore=FirebaseFirestore.getInstance();
     gelenInten=getIntent();
     hedefId=gelenInten.getStringExtra("hedefId");
     kanalId=gelenInten.getStringExtra("kanalId");
     hedefProfilUrl=gelenInten.getStringExtra("hedefProfil");

     mChatList=new ArrayList<>();

 }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        hedefRef=mFireStore.collection("kullanicilar").document(hedefId);
        hedefRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
             if(error !=null){

                 Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                 return;
             }
             if(value !=null && value.exists()){
                 hedefKullanici=value.toObject(Kullanici.class);
                 if(hedefKullanici !=null){
                     hedefIsim.setText(hedefKullanici.getKullaniciIsmi());
                     if(hedefKullanici.getKullaniciProfil().equals("default"))
                         hedefProfil.setImageResource(R.mipmap.ic_launcher);
                     else
                         Picasso.get().load(hedefKullanici.getKullaniciProfil()).resize(66,66).into(hedefProfil);
                 }

             }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        chatQuery=mFireStore.collection("ChatKanallari").document(kanalId).collection("Mesajlar")
                .orderBy("mesajTarihi", Query.Direction.ASCENDING);
        chatQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if(error !=null){
                         Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                         return;
                     }
                     if(value !=null){
                         mChatList.clear();
                         for(DocumentSnapshot snapshot: value.getDocuments()){
                             mChat=snapshot.toObject(Chat.class);
                             assert mChat !=null;
                                 mChatList.add(mChat);
                         }
                         chatAdapter=new ChatAdapter(mChatList,ChatActivity.this,mUser.getUid(),hedefProfilUrl);
                         mRecyclerView.setAdapter(chatAdapter);

                     }
            }
        });
    }
    public void btnMesajGonder(View v){
     txtMesaj=editMesaj.getText().toString();
     if(!TextUtils.isEmpty(txtMesaj)){
         docId= UUID.randomUUID().toString();
         mData=new HashMap<>();
         mData.put("mesajIçerigi",txtMesaj);
         mData.put("gonderen",mUser.getUid());
         mData.put("alici",hedefId);
         mData.put("mesajTipi","text");
         mData.put("mesajTarihi", FieldValue.serverTimestamp());
         mData.put("docId",docId);
         mFireStore.collection("ChatKanallari").document(kanalId).collection("Mesajlar").document(docId)
                 .set(mData).addOnCompleteListener(ChatActivity.this, new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful())
                             editMesaj.setText("");
                         else Toast.makeText(ChatActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                     }
                 });

     }else Toast.makeText(ChatActivity.this, "bir şey yaz ", Toast.LENGTH_SHORT).show();

    }

    public void btnChatKapat(View view) {
     finish();
    }
}
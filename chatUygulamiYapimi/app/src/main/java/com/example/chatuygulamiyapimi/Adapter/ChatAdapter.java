package com.example.chatuygulamiyapimi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatuygulamiyapimi.Model.Chat;
import com.example.chatuygulamiyapimi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private static final int MESAJ_SOL=1;
    private static final int MESAJ_SAG=0;
    private ArrayList<Chat>mChatList;
    private Context mContext;
    private String mUID;
    private String hedefProfil;
    private View v;
    private Chat mChat;

    public ChatAdapter(ArrayList<Chat>mChatList,Context mContext,String mUID,String hedafProfil){

        this.mChatList=mChatList;
        this.mContext=mContext;
        this.mUID=mUID;
        this.hedefProfil=hedafProfil;
    }
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MESAJ_SOL)
            v = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
        else if (viewType==MESAJ_SAG)
            v=LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        mChat=mChatList.get(position);
        holder.txtMesaj.setText(mChat.getMesajIçerigi());
        if(!mChat.getGonderen().equals(mUID)){
            if(hedefProfil.equals("default"))
                holder.imgProfil.setImageResource(R.mipmap.ic_launcher);
            else Picasso.get().load(hedefProfil).resize(56,56).into(holder.imgProfil);
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder{
      CircleImageView imgProfil;
      TextView txtMesaj;


      public ChatHolder(@NonNull View itemView) {
          super(itemView);
          imgProfil=itemView.findViewById(R.id.chat_item_imgProfil);
          txtMesaj=itemView.findViewById(R.id.chat_item_txtMesaj);
      }
  }

    @Override
    public int getItemViewType(int position) {
        if(mChatList.get(position).getGonderen().equals(mUID))
            return MESAJ_SAG;
        else
            return MESAJ_SOL;
    }
}

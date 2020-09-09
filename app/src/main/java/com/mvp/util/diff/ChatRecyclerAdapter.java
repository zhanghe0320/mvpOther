package com.mvp.util.diff;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChatRecyclerAdapter extends RecyclerView.Adapter <ChatRecyclerAdapter.ChatItemViewHolder>{

    private ArrayList<ChatItemBean> chats;
    private Context context;

    public ChatRecyclerAdapter(ArrayList<ChatItemBean> chats, Context context) {
        this.chats = new ArrayList<>();
        cloneChats(chats);
        this.context = context;
    }

    public ArrayList getChats() {
        return chats;
    }

    public void setChats(ArrayList chats){
        cloneChats(chats);
    }

    public static class ChatItemViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public RelativeLayout mRlChatItem;
        //public RoundImageView mRivAvatar;
        public TextView mTvNickname;
        public TextView mTvMsg;
        public TextView mTvTime;


        public ChatItemViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
//            this.mRlChatItem = (RelativeLayout)itemView.findViewById(R.id.rl_chat_item);
//            this.mRivAvatar = (RoundImageView)itemView.findViewById(R.id.riv_chat_item_avatar);
//            this.mTvNickname = (TextView)itemView.findViewById(R.id.tv_chat_item_nickname);
//            this.mTvTime = (TextView) itemView.findViewById(R.id.tv_chat_item_time);
//            this.mTvMsg = (TextView) itemView.findViewById(R.id.tv_chat_item_msg);
        }
    }

    @Override
    public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        //ChatItemViewHolder viewHolder = new ChatItemViewHolder(v, context);
        return null;
    }

    @Override
    public void onBindViewHolder(ChatItemViewHolder holder, int position) {
        ChatItemBean chatItemBean = chats.get(position);
        holder.mTvMsg.setText(chatItemBean.getMsg());
        holder.mTvTime.setText(chatItemBean.getTime());
        holder.mTvNickname.setText(chatItemBean.getNickname());

    }

    @Override
    public void onBindViewHolder(ChatItemViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, position);
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            for (String key: bundle.keySet()){
                switch (key){
                    case "avatarPath":
                        break;
                    case "nickname":
                        holder.mTvNickname.setText((CharSequence) bundle.get(key));
                        break;
                    case "time":
                        holder.mTvTime.setText((CharSequence) bundle.get(key));
                        break;
                    case "msg":
                        holder.mTvMsg.setText((CharSequence) bundle.get(key));
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (chats == null)
            return 0;
        return chats.size();
    }

    private void cloneChats(ArrayList<ChatItemBean> newChats){
        chats.clear();
        if (!newChats.isEmpty())
        {
            for (int i = 0; i < newChats.size(); i ++)
            {
                ChatItemBean bean = new ChatItemBean();
                bean.setUserId(newChats.get(i).getUserId());
                bean.setNickname(newChats.get(i).getNickname());
                bean.setAvatarPath(newChats.get(i).getAvatarPath());
                bean.setMsg(newChats.get(i).getMsg());
                bean.setTime(newChats.get(i).getTime());
                chats.add(bean);
            }
        }
    }


    /**
     * activity使用
     */

//    private void updateChats(){
//
////为了防止数据量过大，比对算法耗时，将算法放入新线程执行
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<ChatItemBean> oldChats = adapter.getChats();
//                DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ChatListCallback(oldChats, chats));
//                Message message = updateChatsHandler.obtainMessage(MSG_WHAT_CHAT_UPDATE);
//                message.obj = result;
//                message.sendToTarget();
//            }
//        }).start();
//
//    }
//
//    private Handler updateChatsHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case MSG_WHAT_CHAT_UPDATE:
//                    DiffUtil.DiffResult result = (DiffUtil.DiffResult)msg.obj;
//                    //界面更新
//                    result.dispatchUpdatesTo(adapter);
//                    adapter.setChats(chats);
//
//            }
//        }
//    };
}
package com.mvp.util.diff;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

/**
 *DiffUtil内部使用Eugene W. Myers’s difference算法来进行两个数据集的对比，
 * 找出新数据与旧数据之间最小的变化部分，
 * 和RecyclerView一起使用可以实现列表的局部更新，而不像以前使用notifyDataSetChanged来更改整个列表。
 */
public class ChatListCallback extends DiffUtil.Callback {
    private ArrayList<ChatItemBean> old_chats, new_chats;

    public ChatListCallback(ArrayList<ChatItemBean> old_chats, ArrayList<ChatItemBean> new_chats) {
        this.old_chats = old_chats;
        this.new_chats = new_chats;
    }

    @Override
    public int getOldListSize() {
        return old_chats.size();
    }

    @Override
    public int getNewListSize() {
        return new_chats.size();
    }

    /**
     * 判断此id的用户消息是否已存在
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        String newId = old_chats.get(oldItemPosition).getUserId();
        String oldId = new_chats.get(newItemPosition).getUserId();
        if (oldId == null || newId == null)
            return false;
        else if(oldId.trim().equals("") || newId.trim().equals("") || (!oldId.equals(newId)))
            return false;
        else
            return true;
    }

    /**
     * 若此id的用户消息已存在，则判断内容是否一致
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        String newAvatarPath = new_chats.get(newItemPosition).getAvatarPath();
        String oldAvatarPath = old_chats.get(oldItemPosition).getAvatarPath();
        String oldNickname = old_chats.get(oldItemPosition).getNickname();
        String newNickname = new_chats.get(newItemPosition).getNickname();
        String newTime = new_chats.get(newItemPosition).getTime();
        String oldTime = old_chats.get(oldItemPosition).getTime();
        String newMsg = new_chats.get(newItemPosition).getMsg();
        String oldMsg = old_chats.get(oldItemPosition).getMsg();

        if ((newAvatarPath == null && oldAvatarPath != null) || (oldAvatarPath == null && newAvatarPath != null)||( oldAvatarPath != null && !oldAvatarPath.equals(newAvatarPath)) ||
                (newNickname == null && oldNickname != null) || (oldNickname == null && newNickname != null)||( oldNickname != null && !oldNickname.equals(newNickname)) ||
                (newTime == null && oldTime != null) || (oldTime == null && newTime != null)||( oldTime != null && !oldTime.equals(newTime)) ||
                (newMsg == null && oldMsg != null) || (oldMsg == null && newMsg != null)||( oldMsg != null && !oldMsg.equals(newMsg)))
            return false;
        return true;
    }
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        String newAvatarPath = new_chats.get(newItemPosition).getAvatarPath();
        String oldAvatarPath = old_chats.get(oldItemPosition).getAvatarPath();
        String oldNickname = old_chats.get(oldItemPosition).getNickname();
        String newNickname = new_chats.get(newItemPosition).getNickname();
        String newTime = new_chats.get(newItemPosition).getTime();
        String oldTime = old_chats.get(oldItemPosition).getTime();
        String newMsg = new_chats.get(newItemPosition).getMsg();
        String oldMsg = old_chats.get(oldItemPosition).getMsg();

        Bundle bundle = new Bundle();

        if ((newAvatarPath == null && oldAvatarPath !=null) || (newAvatarPath != null && oldAvatarPath == null) || (oldAvatarPath != null && !oldAvatarPath.equals(newAvatarPath)))
            bundle.putString("avatarPath", newAvatarPath);
        if((newNickname == null && oldNickname != null) || (oldNickname == null && newNickname != null)||( oldNickname != null && !oldNickname.equals(newNickname)))
            bundle.putString("nickname", newNickname);
        if((newTime == null && oldTime != null) || (oldTime == null && newTime != null)||( oldTime != null && !oldTime.equals(newTime)))
            bundle.putString("time", newTime);
        if ((newMsg == null && oldMsg != null) || (oldMsg == null && newMsg != null)||( oldMsg != null && !oldMsg.equals(newMsg)))
            bundle.putString("msg", newMsg);
        if (bundle.size() == 0)
            return null;
        return bundle;
    }
}
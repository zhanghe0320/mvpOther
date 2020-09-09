package com.mvp.activity.main;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mvp.R;
import com.mvp.util.DiskLruCacheUtils;
import com.mvp.util.ThreadUtilExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class RxAdapter extends RecyclerView.Adapter<RxAdapter.RxViewHolder> {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ItemModel> mDatas = new ArrayList<>();
    int mPosition = -1;
    private RxPresenter mPresenter;
    private DiskLruCacheUtils mDiskLruCacheUtils;
    private RecyclerView mRecyclerView;
    private boolean sIsScrolling = false;
    private ItemTouchHelper mItemTouchHelper;
    private String mess = "mDatas.get(mPosition).mText";

    public RxAdapter(final RecyclerView recyclerView, Activity activity, ArrayList arrayList, RxPresenter rxPresenter, DiskLruCacheUtils diskLruCacheUtils) {
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(mActivity);
        this.mDatas = arrayList;
        this.mPresenter = rxPresenter;
        this.mDiskLruCacheUtils = diskLruCacheUtils;
        this.mRecyclerView = recyclerView;
        createItemhelper(this, recyclerView);

//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, newDatas), true);
//        //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter，轻松成为文艺青年
//        diffResult.dispatchUpdatesTo(this);
//        //别忘了将新数据给Adapter
//        mDatas = newDatas;
//        mAdapter.setDatas(mDatas);
    }


    public void createItemhelper(RecyclerView.Adapter adapter, RecyclerView recyclerView) {
        //拖动更换位置
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            //获取事件
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                LogUtils.e("hsjkkk", "getMovementFlags()");
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    final int swipeFlags = 0;
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            }

            //当item移动的到位置的时候
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                LogUtils.e("hsjkkk", "onMove()");


                //得到当拖拽的viewHolder的Position
                int fromPosition = viewHolder.getAdapterPosition();
                //拿到当前拖拽到的item的viewHolder
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mDatas, i, i + 1);//进行更换位置
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mDatas, i, i - 1);
                    }
                }
                //mPosition = toPosition;


                adapter.notifyItemMoved(fromPosition, toPosition);

                for (int i = 0; i < mDatas.size(); i++) {//修改背景改变位置
                    if (mDatas.get(i).mText.equals(mess)) {
                        mPosition = i;
                    }
                }


                ThreadUtilExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemMoved(fromPosition, toPosition);
                    }
                });
                return true;
            }

            //滑动
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                Toast.makeText(MainActivity.this, "拖拽完成 方向" + direction, Toast.LENGTH_SHORT).show();
                LogUtils.e("hsjkkk", "拖拽完成 方向" + direction);

            }

            //被选中之后进行背景的修改
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                LogUtils.e("hsjkkk", "onSelectedChanged()");
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder.itemView.setBackgroundColor(Color.LTGRAY);//背景修改
                }

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                LogUtils.e("hsjkkk", "clearView()");
                viewHolder.itemView.setBackgroundColor(0);//背景
                //拖拽完成需要修改刷新全部数据


            }

            //重写拖拽不可用
            @Override
            public boolean isLongPressDragEnabled() {
                LogUtils.e("hsjkkk", "isLongPressDragEnabled()");
                return false;
            }


        });


        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @NonNull
    @Override
    public RxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = mInflater.inflate(R.layout.rx_item, parent, false);
        return new RxAdapter.RxViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RxViewHolder holder, int position) {
        //holder.itemView.setBackground();
        ItemModel itemModel = mDatas.get(position);
        holder.rxTextView.setText(itemModel.mText);

        //设置tag 是防止图片加载时 滚动产生的图片错位折叠问题
//        ThreadUtilExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                //do something
//                //if(mRecyclerView.getScrollState() == 0){
//                    //mDiskLruCacheUtils.loadBitmap("http://www.nj-lsj.net/Taste/upload/1567583023388.jpg",holder.rxImageView);
//               // }
//               // if(mRecyclerView.getScrollState() == 0 ){
//                if(position != 1){
//                    holder.rxImageView.setTag("http://www.nj-lsj.net/Taste/upload/1567583023388.jpg");//先进行设置tag
//                    mDiskLruCacheUtils.loadBitmap("http://www.nj-lsj.net/Taste/upload/1567583023388.jpg", holder.rxImageView);
//
//                }
//
////                RequestOptions options = RequestOptions.circleCropTransform();//圆形图片  好多的图片形式都是这么设置的
////                options.placeholder(R.drawable.ic_launcher);//占位图
//
//
//               // }
//            }
//        });
        //if(position == 1 ){
        //holder.rxImageView.setTag(null);//先进行设置tag
        Glide.with(mActivity).load("http://www.nj-lsj.net/Taste/upload/1565164462028.png").
                apply(RequestOptions.centerInsideTransform().placeholder(R.drawable.ic_launcher)).
                into(holder.rxImageView).
                onLoadFailed(mActivity.getResources().getDrawable(R.drawable.ic_launcher));
        //  }

        ScheduledFuture<?> scheduledFuture = ThreadUtilExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {
            @Override
            public void run() {
                // do something
            }
        }, 3, TimeUnit.SECONDS);


        if (scheduledFuture != null) {//取消任务
            if (!scheduledFuture.isCancelled() && !scheduledFuture.isDone()) {
                //取消定时器(等待当前任务结束后，取消定时器)
                scheduledFuture.cancel(false);
                // 取消定时器(不等待当前任务结束，取消定时器)
                scheduledFuture.cancel(true);
            }
        }
        if (itemModel.mIsSelect) {
            mess = itemModel.mText;
            holder.rxTextView.setSelected(true);
        } else {
            holder.rxTextView.setSelected(false);
        }
        holder.rxLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemOnClick(itemModel.mPosition, v);
                // mPosition = position;
                // notifyDataSetChanged();
                for (int i = 0; i < mDatas.size(); i++) {//点击事件发生的时候进行更改某一个数据 而不是全部刷新
                    if (position == i) {
                        mDatas.get(i).mIsSelect = true;
                        notifyItemChanged(position, mDatas.get(i));
                    } else {
                        mDatas.get(i).mIsSelect = false;
                        if (mPosition == i) {
                            notifyItemChanged(mPosition, mDatas.get(i));
                        }
                    }
                }
                mPosition = position;

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemTouchHelper.startDrag(holder);
                return false;
            }
        });
    }

//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position, List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//        if (payloads.isEmpty()){
//            //全部刷新
//        }else {
//            //局部刷新
//        }
//    }


    private void ItemOnClick(int position, View v) {
        mPresenter.ListItemOnClick(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public void onBindViewHolder(@NonNull RxViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        final RxViewHolder contact = (RxViewHolder) holder;
        if (payloads.isEmpty()) {
            //payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            //在这里进行初始化item全部控件
//            contact.rxLinearLayout.setText(mList.get(position).getName());
//            contact.userId.setText(mList.get(position).getId());
//            contact.userImg.setImageResources(mList.get(position).getImg());

        } else {//payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的

            //在这里可以获取payloads中的数据  进行局部刷新
            //假设是int类型
         //   int type = (int) payloads.get(0);// 刷新哪个部分 标志位
//            switch(type){
//                case 0:
//                    contact.userName.setText(mList.get(position).getName());//只刷新userName
//                    break;
//                case 1:
//                    contact.userId.setText(mList.get(position).getId());//只刷新userId
//                    break;
//                case 2:
//                    contact.userImg.setImageResources(mList.get(position).getImg());//只刷新userImg
//                    break;
//            }
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class RxViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rxLinearLayout;
        private TextView rxTextView;
        private ImageView rxImageView;

        public RxViewHolder(@NonNull View itemView) {
            super(itemView);
            rxLinearLayout = itemView.findViewById(R.id.rx_item_linearLayout);
            rxTextView = itemView.findViewById(R.id.rx_item_text);
            rxImageView = itemView.findViewById(R.id.rx_item_img);

        }

    }

    static class ItemModel {
        String mText;
        int mPosition;
        boolean mIsSelect;


        ItemModel(String text, int position, boolean isSelect) {
            this.mText = text;
            this.mPosition = position;
            this.mIsSelect = false;
        }

        public String getmText() {
            return mText;
        }

        public void setmText(String mText) {
            this.mText = mText;
        }

        public int getmPosition() {
            return mPosition;
        }

        public void setmPosition(int mPosition) {
            this.mPosition = mPosition;
        }

        public boolean ismIsSelect() {
            return mIsSelect;
        }

        public void setmIsSelect(boolean mIsSelect) {
            this.mIsSelect = mIsSelect;
        }
    }
}

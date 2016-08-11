package com.dgk.wifichat.module.main.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgk.wifichat.R;
import com.dgk.wifichat.app.MyApplication;
import com.dgk.wifichat.model.bean.HeartBean;
import com.dgk.wifichat.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kevin on 2016/8/11.
 */
public class MainPersonAdapter extends RecyclerView.Adapter<MainPersonAdapter.MyViewHolder> {

    private static String tag = "【MainPersonAdapter】";

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /** 条目点击响应的监听类 */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private ArrayList<HeartBean> list;

    /**
     * 添加一条数据
     *  - 默认添加在队尾
     */
    public void addData(HeartBean heartBean) {
        int position = list.size();
        list.add(position, heartBean);
        notifyItemInserted(position);   // 只有设置了该方法，才会有添加和删除一条数据的动画效果
    }

    /**
     * 添加一条数据
     *  - position：添加位置
     */
    public void addData(int position, HeartBean heartBean) {
        list.add(position, heartBean);
        notifyItemInserted(position);   // 只有设置了该方法，才会有添加和删除一条数据的动画效果
    }

    /**
     * 移除一条数据
     */
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 移除一条数据
     */
    public void removeData(HeartBean heartBean) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(heartBean.getId())) {
                position = i;
                break;
            }
        }
        if (position>=0) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 添加一个集合全部数据
     */
    public void addAllData(ArrayList<HeartBean> data) {

        if (list == null) {
            list = new ArrayList<>();
        }

        list.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 获取所有数据
     */
    public ArrayList<HeartBean> getData() {
        return list;
    }

    /**
     * 清除所有数据
     */
    public void removeAllData() {
        if (list != null && list.size() > 0) {
            int size = list.size();
            list.clear();
//            notifyDataSetChanged();
            LogUtil.i(tag,"removeAllData：清除所有的数据:" + size);
            notifyItemRangeRemoved(0,size);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MyApplication.getContext())
                .inflate(R.layout.item_main_person, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvId.setText(list.get(position).getId());
        holder.tvName.setText(list.get(position).getName());

//        if (mOnItemClickLitener != null) {
//            // 如果设置了回调，则设置点击事件，例如本例中给图片设置点击响应
//            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickLitener.onItemClick(holder.ivIcon, holder.getLayoutPosition());
//                }
//            });
//            holder.ivIcon.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickLitener.onItemLongClick(holder.ivIcon, holder.getLayoutPosition());
//                    return false;
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return (list == null || list.isEmpty()) ? 0 : list.size();
    }

    /**
     * ViewHolder类
     * - 使用ButterKnife，代替了findviewbyid
     */
    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_id)
        TextView tvId;
        @BindView(R.id.tv_name)
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}

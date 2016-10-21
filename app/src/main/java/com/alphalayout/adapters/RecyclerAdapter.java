package com.alphalayout.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalayout.R;
import com.alphalayout.utils.Constants;

import java.util.List;
import java.util.Map;

/**
 * @author glority - lu.meng
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AlphaHolder> {

    private List<Map<String, Object>> mDatas;

    public RecyclerAdapter(List<Map<String, Object>> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public AlphaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new AlphaHolder(view);
    }

    @Override
    public void onBindViewHolder(AlphaHolder holder, int position) {
        Map<String, Object> data = mDatas.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class AlphaHolder extends RecyclerView.ViewHolder {
        private View mRootView;
        private ImageView mIcon;
        private TextView mText;

        private Map<String, Object> mData;

        public AlphaHolder(View itemView) {
            super(itemView);
            mRootView = itemView;
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
            mText = (TextView) itemView.findViewById(R.id.title_item);
        }

        public void bindData(Map<String, Object> data) {
            mData = data;

            mRootView.setBackgroundResource((int) mData.get(Constants.COLOR));
            mIcon.setImageResource((int) mData.get(Constants.ICON));
            mText.setText((String) mData.get(Constants.TITLE));
        }
    }
}

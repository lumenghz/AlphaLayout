package com.alphalayout.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalayout.R;
import com.alphalayout.utils.Constants;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author lu.meng
 */
public class ListAdapter extends ArrayAdapter<Map<String, Object>> {

    private final LayoutInflater mInflater;
    private final List<Map<String, Object>> mDatas;

    public ListAdapter(Context context, int resource, List<Map<String, Object>> mDatas) {
        super(context, resource, mDatas);
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ListAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        final Map<String, Object> model = mDatas.get(position);

        viewHolder.icon.setImageResource((int) model.get(Constants.ICON));
        convertView.setBackgroundResource((int) model.get(Constants.COLOR));
        viewHolder.title.setText((String) model.get(Constants.TITLE));

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.image_view_icon)
        ImageView icon;
        @BindView(R.id.title_item)
        TextView title;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

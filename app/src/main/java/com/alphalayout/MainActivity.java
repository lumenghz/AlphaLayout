package com.alphalayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alhpalayout.AlphaLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author lu.meng
 */
public class MainActivity extends BaseActivity implements AlphaLayout.OnRefreshListener {

    @BindView(R.id.alpha_layout)
    protected AlphaLayout alphaLayout;

    @BindView(R.id.title_header)
    TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new SimpleAdapter(this, R.layout.list_item, mSampleDatas));

        alphaLayout.setOnRefreshListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }

    @Override
    public void onRefresh() {
        alphaLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                alphaLayout.setRefreshing(false);
            }
        }, REFRESH_DELAY);
    }

    @Override
    public void onScroll(int direction, float percent) {
        if (direction == AlphaLayout.DIRECTION_DOWN) {
            ViewCompat.setAlpha(alphaLayout.getHeaderLayout(), 1.0f - percent);
        } else {
            alphaLayout.getHeaderLayout().getBackground().setAlpha((int) (255 * percent));
            mTitleView.getBackground().mutate().setAlpha((int) (255 * (1 - percent)));
        }
    }

    class SimpleAdapter extends ArrayAdapter<Map<String, Integer>> {
        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";

        private final LayoutInflater mInflater;
        private final List<Map<String, Integer>> mDatas;

        public SimpleAdapter(Context context, int resource, List<Map<String, Integer>> mDatas) {
            super(context, resource, mDatas);
            this.mDatas = mDatas;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item, parent, false);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.icon.setImageResource(mDatas.get(position).get(KEY_ICON));
            convertView.setBackgroundResource(mDatas.get(position).get(KEY_COLOR));

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
        }
    }
}

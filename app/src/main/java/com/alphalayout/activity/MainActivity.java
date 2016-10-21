package com.alphalayout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alphalayout.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lu.meng
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_information) {
            startActivity(new Intent(this, InformationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.card_list, R.id.card_recycler, R.id.card_scroll})
    void onCardClicked(View view) {
        switch (view.getId()) {
            case R.id.card_list:
                startActivity(new Intent(this, ListSampleActivity.class));
                break;
            case R.id.card_recycler:
                startActivity(new Intent(this, RecyclerActivity.class));
                break;
            case R.id.card_scroll:
                startActivity(new Intent(this, ScrollSampleActivity.class));
                break;
            default:
                break;
        }
    }
}

package com.alphalayout.activity;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.alphalayout.BuildConfig;
import com.alphalayout.R;

import butterknife.BindView;

/**
 * @author lu.meng
 */
public class InformationActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.chrome)
    WebView chrome;

    @BindView(R.id.version_information)
    TextView versionView;

    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialToolbar();

        final String template = getString(R.string.about_page)
                .replace("{{star_me_on_github}}", getString(R.string.start_me_on_github))
                .replace("{{contact_me}}", getString(R.string.contact_me))
                .replace("{{contact_me_text}}", getString(R.string.contact_me_text))
                .replace("{{dependence}}", getString(R.string.dependence))
                .replace("{{me}}", getString(R.string.me))
                .replace("{{me_text}}", getString(R.string.me_text))
                .replace("{{twitter}}", getString(R.string.twitter))
                .replace("{{thanks}}", getString(R.string.thanks))
                .replace("{{thanks_text}}", getString(R.string.thanks_text))
                .replace("{{license}}", getString(R.string.license))
                .replace("{{license_text}}", getString(R.string.license_text));
        chrome.setWebViewClient(new InformationClient());
        chrome.loadData(template, "text/html; charset=utf-8", null);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final String versionName = BuildConfig.VERSION_NAME;
        final String buildType = BuildConfig.BUILD_TYPE;
        versionView.setText(getString(R.string.version, versionName, buildType));
    }

    private void initialToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_infomation;
    }

    @TargetApi(11)
    private void copy(String text) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(text, text));
        Toast.makeText(this, getString(R.string.about_copied, text), Toast.LENGTH_SHORT).show();
    }

    private void open(Uri uri) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception ignore) {
        }
    }

    private class InformationClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);

            if ("copy".equals(uri.getScheme())) {
                copy(uri.getSchemeSpecificPart());
            } else {
                open(uri);
            }

            return true;
        }
    }
}

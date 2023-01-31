package com.example.dynamic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ResourceActivity extends BaseActivity{

    private TextView textView;
    private ImageView imView;
    private LinearLayout llayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        textView = (TextView) findViewById(R.id.text);
        imView = (ImageView) findViewById(R.id.imageview);
        llayout = (LinearLayout) findViewById(R.id.layout);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginInfo pluginInfo = plugins.get("plugin1.apk");
                loadResources(pluginInfo.getDexPath());
                doSomething(pluginInfo.getClassLoader());
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginInfo pluginInfo = plugins.get("plugin2.apk");
                loadResources(pluginInfo.getDexPath());
                doSomething(pluginInfo.getClassLoader());
            }
        });
    }

    private void doSomething(ClassLoader cl) {
        try {

            Class  clazz = cl.loadClass("com.example.plugin1.UIUtil");
            String str = (String) RefInvoke.invokeStaticMethod(clazz, "getTextString", Context.class, this);
            textView.setText(str);
            Drawable drawable = (Drawable) RefInvoke.invokeStaticMethod(clazz, "getImageDrawable", Context.class, this);
            imView.setBackground(drawable);

            llayout.removeAllViews();
            View view = (View) RefInvoke.invokeStaticMethod(clazz, "getLayout", Context.class, this);
            llayout.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DEMO", "msg:" + e.getMessage());
        }

    }
}

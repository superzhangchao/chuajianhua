package com.example.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class BaseActivity extends Activity {
    private AssetManager mAssetManager;
    private Resources mResources;
    private  Resources.Theme mTheme;

    protected HashMap<String,PluginInfo> plugins = new HashMap<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Utils.extractAssets(newBase, "plugin1.apk");
        Utils.extractAssets(newBase, "plugin2.apk");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPluginInfo("plugin1.apk");
        loadPluginInfo("plugin2.apk");
    }

    private void loadPluginInfo(String pluginName) {
        File extractPath = this.getFileStreamPath(pluginName);
        File fileRelease = getDir("dex", 0);
        String dexPath = extractPath.getPath();
        DexClassLoader classLoader = new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), null, getClassLoader());
        plugins.put(pluginName,new PluginInfo(dexPath,classLoader));
    }

    protected void loadResources(String dexPath){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager==null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources==null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme ==null ? super.getTheme() : mTheme ;
    }
}

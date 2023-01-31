package com.example.mypluginlibrary;

import android.content.Context;

public abstract interface IDynamic {
    void methodWithCallBack(YKCallBack paramYKCallBack);
    void showPluginWindow(Context paramContext);
    void startPluginActivity(Context context);
    void startPluginActivity(Context contxt,Class<?>cls);
    String getStringForResId(Context context);
}

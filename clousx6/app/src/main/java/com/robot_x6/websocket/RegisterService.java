package com.robot_x6.websocket;

import android.app.IntentService;
import android.content.*;

public class RegisterService extends IntentService
{
    public RegisterService(){
        super("dfiig");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
		SuperServersocket server = new SuperServersocket(this,intent,8888);
		server.start();
        registerApp(getApplicationContext(),"TICK TOCK","websocket server");
    }
    protected void registerApp(Context context, String author, String info) {
        Intent intent = new Intent("com.xioce.register");
        intent.setPackage("com.example.robort.clousx6");
        intent.putExtra("name", getAppName(context));
        intent.putExtra("pack", context.getPackageName());
        intent.putExtra("info", info);
        intent.putExtra("version", getVersionName(context));
        intent.putExtra("author", author);
        context.startService(intent);
    }
    private String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (Exception e) {
            e.printStackTrace();
            return "未知插件";
        }
    }
    private String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}

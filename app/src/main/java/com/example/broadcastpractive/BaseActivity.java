package com.example.broadcastpractive;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ForceOfflineReceiver receiver;
    protected void onCreate(Bundle saveInstancesState){
        super.onCreate(saveInstancesState);
        ActivityCollector.addActivity(this);
    }
    protected void onResume(){
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcastpractive.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);

    }
    protected void onPause(){
        super.onPause();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }
    class ForceOfflineReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("warning");
            builder.setMessage("You are forced to be offline");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finishAll();//销毁所有活动
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.startActivity(intent);//重新启动LoginActivity
                }
            });
            builder.show();
        }

    }
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

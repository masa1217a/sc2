package com.example.student001.sc2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kii.cloud.storage.Kii;

import java.security.Permissions;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private KiiManager kiiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent top = new Intent(this, Top.class);
        startActivityForResult(top, 0);

        // Android6.0+ はパーミッションの要求が必要
        boolean execReqPermissions = requestAppPermissions();
        if(!execReqPermissions) {
            //　パーミッションが認証済みなのでログイン開始
            loginWithStoredCredentials();
        }
    }

    //　戻り値はリクエストを実行したか
    private boolean requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        ArrayList<String> reqList = new ArrayList<>();

        PackageManager pm = getPackageManager();
        int checkPermission = pm.checkPermission(Manifest.permission.INTERNET, getPackageName());
        if (checkPermission == PackageManager.PERMISSION_DENIED) {
            reqList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (reqList.size() <= 0) {
            return true;
        }
        requestPermissions(reqList.toArray(new String[reqList.size()]), 0);
        return true;
    }

        @Override
        public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults) {
            for (int grantResult : grantResults) {
                if(grantResult == PackageManager.PERMISSION_DENIED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("エラー");
                    builder.setMessage("許可しなければアプリは動きません");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestAppPermissions();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                    return;
                }
            }

            // 承認が得られたらログインを開始
            loginWithStoredCredentials();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        private void loginWithStoredCredentials() {
            kiiManager = KiiManager.getInstance(this);
            kiiManager.loginWithStoredCredentials(new KiiManager.OnFinishActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("loginWithSC", "success");
                        // TODO データのやり取りのサンプル
                    }

                    @Override
                    public void onFail(Exception e){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
            });
        }

}

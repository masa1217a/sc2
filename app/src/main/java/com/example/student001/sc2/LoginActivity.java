package com.example.student001.sc2;

/**
 * Created by Nyamura on 2015/12/11.
 */
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Nyamura on 2015/12/11.
 */
public class LoginActivity extends AppCompatActivity {

    private KiiManager kiiManager;

    private EditText editUsername;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kiiManager = KiiManager.getInstance(this);

        setupViews();
    }

    private void setupViews() {

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
    }

    public void onLoginClick(View v) {
        kiiManager.login(editUsername.getText().toString(), editPassword.getText().toString(), new KiiManager.OnFinishActionListener() {
            @Override
            public void onSuccess() {
                // ログイン成功時の処理
                finish();
            }
            @Override
            public void onFail(Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("エラー");
                builder.setMessage("ログインに失敗しました");
                builder.setPositiveButton("OK", null);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    public void onSignupClick(View v) {
        kiiManager.signup(editUsername.getText().toString(), editPassword.getText().toString(), new KiiManager.OnFinishActionListener() {
            @Override
            public void onSuccess() {
                showDialog(true);
            }
            @Override
            public void onFail(Exception e) {
                showDialog(false);
            }
            private void showDialog(boolean success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setPositiveButton("OK", null);
                builder.setCancelable(false);
                if(success) {
                    builder.setTitle("サインアップ成功");
                    builder.setMessage("サインアップに成功しました");
                } else {
                    builder.setTitle("エラー");
                    builder.setMessage("サインアップに失敗しました");
                }
                builder.show();
            }

        });
    }
}


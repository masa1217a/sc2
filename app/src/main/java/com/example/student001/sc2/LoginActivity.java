package com.example.student001.sc2;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import jp.itnav.derushio.kiimanager.KiiManager;

public class LoginActivity extends AppCompatActivity {

	private KiiManager kiiManager;

	private EditText usernameEdit;
	private EditText passwordEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		kiiManager = KiiManager.getInstance();

		setupViews();
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private void setupViews() {
		usernameEdit = (EditText) findViewById(R.id.usernameEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
	}

	// ******************** Kii Cloud Control Start ********************
	public void onLoginClick(View v) {
		kiiManager.login(usernameEdit.getText().toString(), passwordEdit.getText().toString(), new KiiManager.OnFinishActionListener() {
			@Override
			public void onSuccess(JSONObject data) {
				setResult(RESULT_OK);
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
		kiiManager.signup(usernameEdit.getText().toString(), passwordEdit.getText().toString(), new KiiManager.OnFinishActionListener() {
			@Override
			public void onSuccess(JSONObject data) {
				showDialog(true);
			}

			@Override
			public void onFail(Exception e) {
				showDialog(false);
			}

			private void showDialog(boolean success) {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setCancelable(false);
				builder.setPositiveButton("OK", null);
				if (success) {
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
	// ******************** Kii Cloud Control End **********************
}

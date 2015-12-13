package com.example.student001.sc2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiUser;


/**
 * Created by derushio on 2015/12/08.
 * Kii管理用 シングルトンオブジェクト
 */
public class KiiManager {
    // Kii情報
    private static final String APP_ID = "f6ebbe80";
    private static final String APP_KEY = "ecad51fa1cc703b9467ac4deaa4ea145";

    // シングルトンインスタンス
    private static KiiManager instance;

    private Context context;
    private Handler uiThreadHandler;

    // コンストラクタを閉じる
    private KiiManager() {
        throw new IllegalStateException("KiiManager(); is ban");
    }

    private KiiManager(Context context) {
        Kii.initialize(context, APP_ID, APP_KEY, Kii.Site.JP);
        this.context = context;
        this.uiThreadHandler = new Handler(Looper.getMainLooper());
    }

    // シングルトンインスタンスを取得
    public static KiiManager getInstance(Context context) {
        if (instance == null) {
            instance = new KiiManager(context);
        }

        return instance;
    }

    // サインアップ
    public void signup(final String username, final String password, final OnFinishActionListener onFinishActionListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KiiUser.Builder builder = KiiUser.builderWithName(username);
                KiiUser user = builder.build();

                try {
                    user.register(password);
                    returnOnUIThread(onFinishActionListener, true, null);
                } catch (Exception e) {
                    returnOnUIThread(onFinishActionListener, false, e);
                }
            }
        }).start();
    }

    // 保存情報からログインを実行戻り値で成功失敗を返す
    public void loginWithStoredCredentials(final OnFinishActionListener onFinishActionListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KiiUser user = KiiUser.loginWithStoredCredentials();
                    user.refresh();
                    returnOnUIThread(onFinishActionListener, true, null);
                } catch (Exception e) {
                    // ログイン失敗
                    returnOnUIThread(onFinishActionListener, false, e);
                }
            }
        }).start();
    }

    // ユーザーネームとパスワードからログイン
    public void login(final String username, final String password, final OnFinishActionListener onFinishActionListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    KiiUser.logIn(username, password);
                    returnOnUIThread(onFinishActionListener, true, null);
                } catch (Exception e) {
                    // ログイン失敗
                }
            }
        }).start();
    }

    // UIThreadでリスナを呼ぶ（終了後にUI部分の変更を加えることがあるため）
    private void returnOnUIThread(final OnFinishActionListener onFinishActionListener, final boolean success, final Exception e) {
        if (onFinishActionListener == null) {
            return;
        }

        uiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    onFinishActionListener.onSuccess();
                } else {
                    onFinishActionListener.onFail(e);
                }
            }
        });
    }

    // KiiManagerのアクションが終了したら呼ばれるリスナ
    public interface OnFinishActionListener {

        void onSuccess();

        void onFail(Exception e);
    }
}

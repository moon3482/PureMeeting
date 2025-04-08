package com.example.mana;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

import timber.log.Timber;

public class ManaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        KakaoSdk.init(ManaApplication.this, BuildConfig.KAKAO_TOKEN);
    }

//    public class KakaoSDKAdapter extends KakaoAdapter {
//
//        @Override
//        public ISessionConfig getSessionConfig() {
//            return new ISessionConfig() {
//                @Override
//                public AuthType[] getAuthTypes() {
//                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
//                }
//
//                @Override
//                public boolean isUsingWebviewTimer() {
//                    return false;
//                }
//
//                @Override
//                public boolean isSecureMode() {
//                    return false;
//                }
//
//                @Override
//                public ApprovalType getApprovalType() {
//                    return ApprovalType.INDIVIDUAL;
//                }
//
//                @Override
//                public boolean isSaveFormData() {
//                    return true;
//                }
//            };
//        }
//
//        // Application이 가지고 있는 정보를 얻기 위한 인터페이스
//        @Override
//        public IApplicationConfig getApplicationConfig() {
//            return new IApplicationConfig() {
//                @Override
//                public Context getApplicationContext() {
//                    return GlobalApplication.getGlobalApplicationContext();
//                }
//            };
//        }
//    }
}
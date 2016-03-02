package com.hana053.micropost.presentation.usershow;

import android.support.v4.app.FragmentActivity;

import com.hana053.micropost.presentation.core.base.BaseApplication;
import com.hana053.micropost.presentation.core.di.ActivityModule;
import com.hana053.micropost.presentation.core.di.HasComponent;

public class UserShowTestActivity extends FragmentActivity implements HasComponent<UserShowComponent> {
    @Override
    public UserShowComponent getComponent() {
        return DaggerUserShowComponent.builder()
                .appComponent(BaseApplication.component(this))
                .activityModule(new ActivityModule(this))
                .userShowModule(new UserShowModule())
                .build();
    }
}
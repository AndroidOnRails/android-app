package com.hana053.micropost.presentation.relateduserlist.followerlist;

import com.hana053.micropost.presentation.core.base.BaseApplication;
import com.hana053.micropost.presentation.core.di.ActivityModule;
import com.hana053.micropost.presentation.core.di.ExplicitHasComponent;
import com.hana053.micropost.presentation.relateduserlist.RelatedUserListBaseActivity;

import lombok.Getter;

public class FollowerListActivity extends RelatedUserListBaseActivity implements ExplicitHasComponent<FollowerListComponent> {

    @Getter
    private FollowerListComponent component;

    @Override
    protected void prepareComponent() {
        component = DaggerFollowerListComponent.builder()
                .appComponent(BaseApplication.component(this))
                .activityModule(new ActivityModule(this))
                .followerListModule(new FollowerListModule())
                .build();
        component.inject(this);
    }

    @Override
    public Class<FollowerListComponent> getComponentType() {
        return FollowerListComponent.class;
    }
}
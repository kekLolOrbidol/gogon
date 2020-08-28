package com.vushnevskiy.gogon.ui;

import android.app.Application;

import com.vushnevskiy.gogon.injection.DaggerGoApplicationComponent;
import com.vushnevskiy.gogon.injection.GoApplicationComponent;
import com.vushnevskiy.gogon.injection.GoApplicationModule;

/**
 * Top level application for Game of Go.
 */
public class GoApplication extends Application {

  private GoApplicationComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerGoApplicationComponent.builder()
        .goApplicationModule(new GoApplicationModule(this))
        .build();
  }

  public GoApplicationComponent getComponent() {
    return component;
  }

}

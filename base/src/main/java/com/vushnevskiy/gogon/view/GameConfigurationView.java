package com.vushnevskiy.gogon.view;

import com.vushnevskiy.gogon.presenter.ConfigurationEventListener;
import com.vushnevskiy.gogon.viewmodel.ConfigurationViewModel;

public interface GameConfigurationView {
  void setConfigurationModel(ConfigurationViewModel configurationViewModel);
  void setConfigurationViewListener(ConfigurationEventListener configurationEventListener);
}

package com.vushnevskiy.gogon.ui;

import androidx.fragment.app.Fragment;

import com.vushnevskiy.gogon.injection.GoApplicationComponent;

/**
 * Base Fragment implementing common behaviour.
 */
public abstract class GoBlobBaseFragment extends Fragment {
  protected MainActivity getGoBlobActivity() {
    return ((MainActivity)getActivity());
  }

  public void updateFromConnectionStatus(boolean isSignInComplete) {}

  protected GoApplicationComponent getComponent() {
    return ((GoApplication)getActivity().getApplication()).getComponent();
  }
}

// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1.ui.mainui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;

import com.refresh.chotusalesv1.R;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class DashboardScreen_ViewBinding implements Unbinder {
  private DashboardScreen target;

  @UiThread
  public DashboardScreen_ViewBinding(DashboardScreen target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DashboardScreen_ViewBinding(DashboardScreen target, View source) {
    this.target = target;

    target.loadChotusales = Utils.findRequiredViewAsType(source, R.id.loadChotuSales, "field 'loadChotusales'", ImageButton.class);
    target.loadSettings = Utils.findRequiredViewAsType(source, R.id.loadSettings, "field 'loadSettings'", ImageButton.class);
    target.loadtaxSettings = Utils.findRequiredViewAsType(source, R.id.loadtaxsettings, "field 'loadtaxSettings'", ImageButton.class);
    target.dbSettings = Utils.findRequiredViewAsType(source, R.id.dbsettings, "field 'dbSettings'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DashboardScreen target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.loadChotusales = null;
    target.loadSettings = null;
    target.loadtaxSettings = null;
    target.dbSettings = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1.ui.mainui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.refresh.chotusalesv1.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TaxSettingActivity_ViewBinding implements Unbinder {
  private TaxSettingActivity target;

  @UiThread
  public TaxSettingActivity_ViewBinding(TaxSettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TaxSettingActivity_ViewBinding(TaxSettingActivity target, View source) {
    this.target = target;

    target.taxListView = Utils.findRequiredViewAsType(source, R.id.listtaxitems, "field 'taxListView'", ListView.class);
    target.btnAddtaxitem = Utils.findRequiredViewAsType(source, R.id.btnaddtaxitem, "field 'btnAddtaxitem'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TaxSettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.taxListView = null;
    target.btnAddtaxitem = null;
  }
}

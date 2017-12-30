// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import org.achartengine.GraphicalView;

public class ShowSalesGraph_ViewBinding implements Unbinder {
  private ShowSalesGraph target;

  @UiThread
  public ShowSalesGraph_ViewBinding(ShowSalesGraph target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShowSalesGraph_ViewBinding(ShowSalesGraph target, View source) {
    this.target = target;

    target.salesGraph = Utils.findRequiredViewAsType(source, R.id.SalesgraphView, "field 'salesGraph'", GraphicalView.class);
    target.saleSpinner = Utils.findRequiredViewAsType(source, R.id.salesSpinner, "field 'saleSpinner'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowSalesGraph target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.salesGraph = null;
    target.saleSpinner = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1.ui.mainui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.refresh.chotusalesv1.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SaveDBtoDrive_Activity_ViewBinding implements Unbinder {
  private SaveDBtoDrive_Activity target;

  private View view2131689615;

  private View view2131689616;

  @UiThread
  public SaveDBtoDrive_Activity_ViewBinding(SaveDBtoDrive_Activity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SaveDBtoDrive_Activity_ViewBinding(final SaveDBtoDrive_Activity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.button_pull_cloud, "field 'mButtonPullCloud' and method 'pullCloud'");
    target.mButtonPullCloud = Utils.castView(view, R.id.button_pull_cloud, "field 'mButtonPullCloud'", Button.class);
    view2131689615 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pullCloud();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_push_local, "field 'mButtonPushLocal' and method 'pushLocal'");
    target.mButtonPushLocal = Utils.castView(view, R.id.button_push_local, "field 'mButtonPushLocal'", Button.class);
    view2131689616 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pushLocal();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SaveDBtoDrive_Activity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mButtonPullCloud = null;
    target.mButtonPushLocal = null;

    view2131689615.setOnClickListener(null);
    view2131689615 = null;
    view2131689616.setOnClickListener(null);
    view2131689616 = null;
  }
}

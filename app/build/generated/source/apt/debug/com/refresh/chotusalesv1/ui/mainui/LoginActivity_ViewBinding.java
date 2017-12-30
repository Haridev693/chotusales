// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1.ui.mainui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.refresh.chotusalesv1.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.mEmailView = Utils.findRequiredViewAsType(source, R.id.email, "field 'mEmailView'", EditText.class);
    target.mPasswordView = Utils.findRequiredViewAsType(source, R.id.password, "field 'mPasswordView'", EditText.class);
    target.mProgressView = Utils.findRequiredView(source, R.id.login_progress, "field 'mProgressView'");
    target.mLoginFormView = Utils.findRequiredView(source, R.id.login_form, "field 'mLoginFormView'");
    target.mEmailSignInButton = Utils.findRequiredViewAsType(source, R.id.email_sign_in_button, "field 'mEmailSignInButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mEmailView = null;
    target.mPasswordView = null;
    target.mProgressView = null;
    target.mLoginFormView = null;
    target.mEmailSignInButton = null;
  }
}

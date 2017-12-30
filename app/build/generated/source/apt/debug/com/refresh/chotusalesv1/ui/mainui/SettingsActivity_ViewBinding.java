// Generated code from Butter Knife. Do not modify!
package com.refresh.chotusalesv1.ui.mainui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.refresh.chotusalesv1.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingsActivity_ViewBinding implements Unbinder {
  private SettingsActivity target;

  @UiThread
  public SettingsActivity_ViewBinding(SettingsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingsActivity_ViewBinding(SettingsActivity target, View source) {
    this.target = target;

    target.bluetoothScan = Utils.findRequiredViewAsType(source, R.id.btnBluetoothscan, "field 'bluetoothScan'", Button.class);
    target.SaveSettings = Utils.findRequiredViewAsType(source, R.id.btnSaveSettings, "field 'SaveSettings'", Button.class);
    target.Cancel = Utils.findRequiredViewAsType(source, R.id.btnCancel, "field 'Cancel'", Button.class);
    target.enablePrinter = Utils.findRequiredViewAsType(source, R.id.CheckEnablePrinter, "field 'enablePrinter'", CheckBox.class);
    target.vatnumber = Utils.findRequiredViewAsType(source, R.id.VatNumber, "field 'vatnumber'", EditText.class);
    target.bluetoothAdress = Utils.findRequiredViewAsType(source, R.id.bluetoothAdress, "field 'bluetoothAdress'", TextView.class);
    target.printerFooter = Utils.findRequiredViewAsType(source, R.id.printerFooter, "field 'printerFooter'", EditText.class);
    target.printerHeader = Utils.findRequiredViewAsType(source, R.id.printerHeader, "field 'printerHeader'", EditText.class);
    target.SMSKey = Utils.findRequiredViewAsType(source, R.id.SMSKey, "field 'SMSKey'", EditText.class);
    target.SMSSenderID = Utils.findRequiredViewAsType(source, R.id.SMSSenderID, "field 'SMSSenderID'", EditText.class);
    target.SMSUserName = Utils.findRequiredViewAsType(source, R.id.SMSUsername, "field 'SMSUserName'", EditText.class);
    target.checkSMSenabled = Utils.findRequiredViewAsType(source, R.id.checkSMSenabled, "field 'checkSMSenabled'", CheckBox.class);
    target.btncheckUnits = Utils.findRequiredViewAsType(source, R.id.btncheckUnits, "field 'btncheckUnits'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bluetoothScan = null;
    target.SaveSettings = null;
    target.Cancel = null;
    target.enablePrinter = null;
    target.vatnumber = null;
    target.bluetoothAdress = null;
    target.printerFooter = null;
    target.printerHeader = null;
    target.SMSKey = null;
    target.SMSSenderID = null;
    target.SMSUserName = null;
    target.checkSMSenabled = null;
    target.btncheckUnits = null;
  }
}

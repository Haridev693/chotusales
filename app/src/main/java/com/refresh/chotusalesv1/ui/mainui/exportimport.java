package com.refresh.chotusalesv1.ui.mainui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;
import com.rustamg.filedialogs.SaveFileDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class exportimport extends AppCompatActivity implements FileDialog.OnFileSelectedListener {

//    @BindView(R.id.btnimport)
//    protected EditText mExtensionText;
    @BindView(R.id.btnimport)
    protected Button mOpenFileButton;
    @BindView(R.id.btnexport)
    protected Button mSaveFileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportimport);
        ButterKnife.bind(this);
    }

//    private void requestStoragePermissions() {
//
//        new (this)
//                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Action1<Boolean>() {
//
//                    @Override
//                    public void call(Boolean granted) {
//
//                        mStoragePermissionGranted = granted;
//                    }
//                });
//    }

    @OnClick(R.id.btnimport)
    protected void onOpenDialogClick() {

//        if (mStoragePermissionGranted) {
            showFileDialog(new OpenFileDialog(), OpenFileDialog.class.getName());
//        }
//        else {
//            showPermissionError();
//        }
    }

    @OnClick(R.id.btnexport)
    protected void onSaveDialogClick() {

//        if (mStoragePermissionGranted) {
            showFileDialog(new SaveFileDialog(), SaveFileDialog.class.getName());
//        }
//        else {
//            showPermissionError();
//        }
    }

    private void showPermissionError() {

        Toast.makeText(this, "Storage permission is not granted", Toast.LENGTH_LONG).show();
    }

    private void showFileDialog(FileDialog dialog, String tag) {

        Bundle args = new Bundle();
        args.putString(FileDialog.EXTENSION, ".xlsx");
        dialog.setArguments(args);
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog);
        dialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onFileSelected(FileDialog dialog, File file) {

        Toast.makeText(this, getString(R.string.fileselected)+ file.getName(), Toast.LENGTH_LONG).show();
    }

}

//package com.refresh.chotusalesv1.ui.mainui;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.github.athingunique.ddbs.DriveSyncController;
//import com.github.athingunique.ddbs.NewerDatabaseCallback;
//import com.refresh.chotusalesv1.R;
//import com.refresh.chotusalesv1.techicalservices.AndroidDatabase;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class SaveDBtoDrive_Activity1 extends AppCompatActivity implements NewerDatabaseCallback{
//
//
//
//    private AndroidDatabase mDbHelper;
//
//    /**
//     * Reference to the library DriveSyncController that handles interfacing the local SQLite DB and
//     * the Drive backup.
//     */
//    private DriveSyncController mSyncController;
//
//
//    @BindView(R.id.button_pull_cloud)
//    Button mButtonPullCloud;
//
//    /**
//     * Pressing this button triggers a pull from the cloud db that overwrites the local db.
//     */
//    @OnClick(R.id.button_pull_cloud)
//    public void pullCloud() {
//
//        mSyncController.pullDbFromDrive();
//        toaster("Pulling DB from Google Drive");
//    }
//
//
//    @BindView(R.id.button_push_local)
//    Button mButtonPushLocal;
//
//    /**
//     * Pressing this button triggers a push of the local db to the cloud db.
//     */
//    @OnClick(R.id.button_push_local)
//    public void pushLocal() {
//        mSyncController.putDbInDrive();
//        toaster("posting local DB to Google Drive");
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        connectivity.globalDB =  database;
//
//        mDbHelper = new AndroidDatabase(getApplicationContext());
//        mSyncController = DriveSyncController.get(this, mDbHelper, this).setDebug(true);
//        setContentView(R.layout.activity_save_dbto_drive_);
//
//        ButterKnife.bind(this);
//        Toast.makeText(this,"Please make choices carefully as this will overwrite existing DB",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void driveNewer() {
//        mSyncController.pullDbFromDrive();
//        toaster("Cloud newer");
//
//    }
//
//    @Override
//    public void localNewer() {
//        mSyncController.putDbInDrive();
//        toaster("Local newer");
//    }
//
//
//    private void toaster(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}

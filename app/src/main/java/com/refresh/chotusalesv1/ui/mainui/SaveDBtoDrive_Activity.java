package com.refresh.chotusalesv1.ui.mainui;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.techicalservices.AndroidDatabase;
import com.refresh.chotusalesv1.techicalservices.DatabaseContents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.github.athingunique.ddbs.DriveSyncController;

public class SaveDBtoDrive_Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private AndroidDatabase mDbHelper;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "<< DRIVE >>";
    protected static final int REQUEST_CODE_RESOLUTION = 1337;
    private String FOLDER_NAME = "ChotuSales";
    com.google.android.gms.common.api.GoogleApiClient GAC;

    /**
     * Reference to the library DriveSyncController that handles interfacing the local SQLite DB and
     * the Drive backup.
     */
//    private DriveSyncController mSyncController;


    @BindView(R.id.button_pull_cloud)
    Button mButtonPullCloud;

    /**
     * Pressing this button triggers a pull from the cloud db that overwrites the local db.
     */
    @OnClick(R.id.button_pull_cloud)
    public void pullCloud() {

        readfilefromDrive();
//        mSyncController.pullDbFromDrive();
        toaster("Pulling DB from Google Drive");
    }


    @BindView(R.id.button_push_local)
    Button mButtonPushLocal;

    /**
     * Pressing this button triggers a push of the local db to the cloud db.
     */
    @OnClick(R.id.button_push_local)
    public void pushLocal() {
        upload_to_drive();
//        mSyncController.putDbInDrive();
        toaster("posting local DB to Google Drive");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        connectivity.globalDB =  database;

//        mDbHelper = new AndroidDatabase(getApplicationContext());
//        mSyncController = DriveSyncController.get(this, mDbHelper, this).setDebug(true);
        setContentView(R.layout.activity_save_dbto_drive_);

        ButterKnife.bind(this);
        Toast.makeText(this,"Please make choices carefully as this will overwrite existing DB",Toast.LENGTH_SHORT).show();
    }


    private void toaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    private void upload_to_drive() {

        //async check if folder exists... if not, create it. continue after with create_file_in_folder(driveId);
        check_folder_exists();
    }

    private void check_folder_exists() {
        Query query =
                new Query.Builder().addFilter(Filters.and(Filters.eq(SearchableField.TITLE, FOLDER_NAME), Filters.eq(SearchableField.TRASHED, false)))
                        .build();
        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult result) {
                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "Cannot create folder in the root.");
                } else {
                    boolean isFound = false;
                    for (Metadata m : result.getMetadataBuffer()) {
                        if (m.getTitle().equals(FOLDER_NAME)) {
                            Log.e(TAG, "Folder exists");
                            isFound = true;
                            DriveId driveId = m.getDriveId();
                            create_file_in_folder(driveId);
                            break;
                        }
                    }
                    if (!isFound) {
                        Log.i(TAG, "Folder not found; creating it.");
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(FOLDER_NAME).build();
                        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                .createFolder(mGoogleApiClient, changeSet)
                                .setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
                                    @Override
                                    public void onResult(DriveFolder.DriveFolderResult result) {
                                        if (!result.getStatus().isSuccess()) {
                                            Log.e(TAG, "U AR A MORON! Error while trying to create the folder");
                                        } else {
                                            Log.i(TAG, "Created a folder");
                                            DriveId driveId = result.getDriveFolder().getDriveId();
                                            create_file_in_folder(driveId);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private void create_file_in_folder(final DriveId driveId) {

        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    Log.e(TAG, "U AR A MORON! Error while trying to create new file contents");
                    return;
                }

                OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();

                //------ THIS IS AN EXAMPLE FOR FILE --------
                Toast.makeText(SaveDBtoDrive_Activity.this, "Uploading to drive...", Toast.LENGTH_LONG).show();


                final File theFile = getApplicationContext().getDatabasePath(DatabaseContents.DATABASE.toString());//new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Hello/IKONNECTRETAIL.db"); //>>>>>> WHAT FILE ?
                try {
                    FileInputStream fileInputStream = new FileInputStream(theFile);
                    byte[] buffer = new byte[1024];
                    //long size = theFile.length();
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                } catch (IOException e1) {
                    Log.i(TAG, "U AR A MORON! Unable to write file contents.");
                }
                String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("application/x-sqlite3");
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(theFile.getName()).setMimeType(mimeType).setStarred(false).build();
                DriveFolder folder =driveId.asDriveFolder();
                folder.createFile(mGoogleApiClient, changeSet, driveContentsResult.getDriveContents())
                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                            @Override
                            public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
                                if (!driveFileResult.getStatus().isSuccess()) {
                                    Log.e(TAG, "U AR A MORON!  Error while trying to create the file");
                                    return;
                                }
                                Log.v(TAG, "Created a file: " + driveFileResult.getDriveFile().getDriveId());
                            }
                        });


            }
        });
    }



    public void readfilefromDrive()
    {


//        Filters.eq(SearchableField.MIME_TYPE, mimeType)
//        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("application/x-sqlite3");
        Query query =
                new Query.Builder().addFilter(Filters.and(Filters.eq(SearchableField.TITLE, DatabaseContents.DATABASE.toString())))
                        .build();




        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {

                if (!metadataBufferResult.getStatus().isSuccess()) {
                    Log.e(TAG, "Cannot create folder in the root.");
                } else {
                    boolean isFound = false;
                    for (Metadata m : metadataBufferResult.getMetadataBuffer()) {
                        if (m.getTitle().equals(DatabaseContents.DATABASE.toString())) {
                            Log.e(TAG, "Folder exists");
                            isFound = true;
                            DriveId driveId = m.getDriveId();
                            DriveFile file = driveId.asDriveFile();
                            int mode = DriveFile.MODE_READ_ONLY;
                            file.open(mGoogleApiClient, mode, null).setResultCallback(readFilecontent);
//                            writeCloudStreamToLocalDb(driveId.asDriveFile().);
                            break;
                        }
                    }
                }
            }});




//        Drive.DriveApi.fetchDriveId(mGoogleApiClient, DatabaseContents.DATABASE.toString()).setResultCallback(readFile);


    }


//    /*callback on getting the drive id, contained in result*/
//    final private ResultCallback<DriveApi.DriveIdResult> readFile = new
//            ResultCallback<DriveApi.DriveIdResult>() {
//                @Override
//                public void onResult(@NonNull DriveApi.DriveIdResult driveIdResult) {
//                    if (driveIdResult.getStatus().isSuccess()) {
//                        DriveFile file = driveIdResult.getDriveId().asDriveFile();
//                        int mode = DriveFile.MODE_READ_ONLY;
//                        file.open(mGoogleApiClient, mode, null).setResultCallback(readFilecontent);
//                    }
//                }
//            };



    final private ResultCallback<DriveApi.DriveContentsResult> readFilecontent= new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {

            writeCloudStreamToLocalDb(driveContentsResult.getDriveContents().getInputStream());
            //driveContentsResult.getDriveContents().


        }
    };


    private void writeCloudStreamToLocalDb(InputStream inputStream) {

        OutputStream localDbOutputStream = null;

        String DBpath = getApplicationContext().getDatabasePath(DatabaseContents.DATABASE.toString()).getAbsolutePath();

        File  f = getApplicationContext().getDatabasePath(DatabaseContents.DATABASE.toString());

        f.delete();

        // PLEASE IT WOULD BE SO MUCH NICER :(((  [yes, I know this isn't possible. I'm just complaining]
        try {

//            File fnew = new File(DBpath);
            localDbOutputStream = new FileOutputStream(DBpath);
            fileCopyHelper(inputStream, localDbOutputStream);
            Toast.makeText(this,"Successfully restored Database",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {

            Log.e("Controller", "Local Db file not found");

        } finally {

            if (localDbOutputStream != null) {
                try {
                    localDbOutputStream.close();
                } catch (IOException e) {
                    // Squash
                }
            }
        }
    }


    private void fileCopyHelper(InputStream in, OutputStream out) {
        byte[] buffer = new byte[4096];
        int n;

        // IT SURE WOULD BE NICE IF TRY-WITH-RESOURCES WAS SUPPORTED IN OLDER SDK VERSIONS :(
        try {
            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.e("IOException", "fileCopyHelper | a stream is null");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // Squash
            }
            try {
                out.close();
            } catch (IOException e) {
                // Squash
            }
        }
    }



                @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    public void writeToFile(String fileName, String body) {
        FileOutputStream fos = null;
        try {
            final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xtests/");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e("ALERT", "U AR A MORON!  could not create the directories. CHECK THE FUCKING PERMISSIONS SON!");
                }
            }
            final File myFile = new File(dir, fileName + "_" + String.valueOf(System.currentTimeMillis()) + ".txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            fos = new FileOutputStream(myFile);
            fos.write(body.getBytes());
            fos.close();
            Toast.makeText(SaveDBtoDrive_Activity.this, "File created ok! Let me give you a fucking congratulations!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "+++++++++++++++++++ onConnected +++++++++++++++++++");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended [" + String.valueOf(i) + "]");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "U AR A MORON! Exception while starting resolution activity", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }



}

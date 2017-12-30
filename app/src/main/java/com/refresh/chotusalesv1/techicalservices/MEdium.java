//package com.refresh.chotusalesv1.techicalservices;
//
//import com.google.android.gms.drive.Drive;
//import com.google.android.gms.drive.OpenFileActivityBuilder;
//
///**
// * Created by Lenovo on 12/17/2017.
// */
//
//public class MEdium {
//
//    private void openFilePicker() {
//        //        build an intent that we'll use to start the open file activity
//        IntentSender intentSender = Drive.DriveApi
//                .newOpenFileActivityBuilder()
////                these mimetypes enable these folders/files types to be selected
//                .setMimeType(new String[]{DriveFolder.MIME_TYPE, "text/plain"})
//                .build(mGoogleApiClient);
//        try {
//            startIntentSenderForResult(
//                    intentSender, REQUEST_CODE_SELECT, null, 0, 0, 0);
//        } catch (IntentSender.SendIntentException e) {
//            Log.e(TAG, "Unable to send intent", e);
//            showErrorDialog();
//        }
//    }
//
//    private void openFolderPicker() {
//        try {
//            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                if (intentPicker == null)
//                    intentPicker = buildIntent();
//                //Start the picker to choose a folder
//                startIntentSenderForResult(
//                        intentPicker, REQUEST_CODE_PICKER, null, 0, 0, 0);
//            }
//        } catch (IntentSender.SendIntentException e) {
//            Log.e(TAG, "Unable to send intent", e);
//            showErrorDialog();
//        }
//    }
//
//    private IntentSender buildIntent() {
//        return Drive.DriveApi
//                .newOpenFileActivityBuilder()
//                .setMimeType(new String[]{DriveFolder.MIME_TYPE})
//                .build(mGoogleApiClient);
//    }
//
//    private void downloadFromDrive(DriveFile file) {
//        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
//                .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
//                    @Override
//                    public void onResult(DriveApi.DriveContentsResult result) {
//                        if (!result.getStatus().isSuccess()) {
//                            showErrorDialog();
//                            return;
//                        }
//
//                        // DriveContents object contains pointers
//                        // to the actual byte stream
//                        DriveContents contents = result.getDriveContents();
//                        InputStream input = contents.getInputStream();
//
//                        try {
//                            File file = new File(realm.getPath());
//                            OutputStream output = new FileOutputStream(file);
//                            try {
//                                try {
//                                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
//                                    int read;
//
//                                    while ((read = input.read(buffer)) != -1) {
//                                        output.write(buffer, 0, read);
//                                    }
//                                    output.flush();
//                                } finally {
//                                    output.close();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                input.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        Toast.makeText(getApplicationContext(), R.string.activity_backup_drive_message_restart, Toast.LENGTH_LONG).show();
//
//                        // Reboot app
//                        Intent mStartActivity = new Intent(getApplicationContext(), MainActivity.class);
//                        int mPendingIntentId = 123456;
//                        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                        System.exit(0);
//                    }
//                });
//    }
//
//    private void uploadToDrive(DriveId mFolderDriveId) {
//        if (mFolderDriveId != null) {
//            //Create the file on GDrive
//            final DriveFolder folder = mFolderDriveId.asDriveFolder();
//            Drive.DriveApi.newDriveContents(mGoogleApiClient)
//                    .setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
//                        @Override
//                        public void onResult(DriveApi.DriveContentsResult result) {
//                            if (!result.getStatus().isSuccess()) {
//                                Log.e(TAG, "Error while trying to create new file contents");
//                                showErrorDialog();
//                                return;
//                            }
//                            final DriveContents driveContents = result.getDriveContents();
//
//                            // Perform I/O off the UI thread.
//                            new Thread() {
//                                @Override
//                                public void run() {
//                                    // write content to DriveContents
//                                    OutputStream outputStream = driveContents.getOutputStream();
//
//                                    FileInputStream inputStream = null;
//                                    try {
//                                        inputStream = new FileInputStream(new File(realm.getPath()));
//                                    } catch (FileNotFoundException e) {
//                                        showErrorDialog();
//                                        e.printStackTrace();
//                                    }
//
//                                    byte[] buf = new byte[1024];
//                                    int bytesRead;
//                                    try {
//                                        if (inputStream != null) {
//                                            while ((bytesRead = inputStream.read(buf)) > 0) {
//                                                outputStream.write(buf, 0, bytesRead);
//                                            }
//                                        }
//                                    } catch (IOException e) {
//                                        showErrorDialog();
//                                        e.printStackTrace();
//                                    }
//
//
//                                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
//                                            .setTitle("glucosio.realm")
//                                            .setMimeType("text/plain")
//                                            .build();
//
//                                    // create a file in selected folder
//                                    folder.createFile(mGoogleApiClient, changeSet, driveContents)
//                                            .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
//                                                @Override
//                                                public void onResult(DriveFolder.DriveFileResult result) {
//                                                    if (!result.getStatus().isSuccess()) {
//                                                        Log.d(TAG, "Error while trying to create the file");
//                                                        showErrorDialog();
//                                                        finish();
//                                                        return;
//                                                    }
//                                                    showSuccessDialog();
//                                                    finish();
//                                                }
//                                            });
//                                }
//                            }.start();
//                        }
//                    });
//        }
//    }
//
//    @Override
//    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    backup.start();
//                }
//                break;
//            // REQUEST_CODE_PICKER
//            case 2:
//                intentPicker = null;
//
//                if (resultCode == RESULT_OK) {
//                    //Get the folder drive id
//                    DriveId mFolderDriveId = data.getParcelableExtra(
//                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
//
//                    uploadToDrive(mFolderDriveId);
//                }
//                break;
//
//            // REQUEST_CODE_SELECT
//            case 3:
//                if (resultCode == RESULT_OK) {
//                    // get the selected item's ID
//                    DriveId driveId = data.getParcelableExtra(
//                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
//
//                    DriveFile file = driveId.asDriveFile();
//                    downloadFromDrive(file);
//
//                } else {
//                    showErrorDialog();
//                }
//                finish();
//                break;
//
//        }
//    }
//}

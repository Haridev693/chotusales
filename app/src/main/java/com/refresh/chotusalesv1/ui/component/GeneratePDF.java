package com.refresh.chotusalesv1.ui.component;

import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.view.View;

import com.refresh.chotusalesv1.techicalservices.PDFUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.refresh.chotusalesv1.techicalservices.PDFUtil.PDF_PAGE_HEIGHT;
import static com.refresh.chotusalesv1.techicalservices.PDFUtil.PDF_PAGE_WIDTH;

/**
 * Created by Lenovo on 12/20/2017.
 */

public class GeneratePDF  {//extends AsyncTask<Void, Void, File> {

    private static final String TAG ="PDFGenerator";
        // mContentViews.
        private List<View> mContentViews;

        // mFilePath.
        private String mFilePath;

        // mListener.
        private PDFUtil.PDFUtilListener mListener = null;

        // mException.
        private Exception mException;

        /**
         * Constructor.
         *
         * @param contentViews List of Content Views to be converted as PDF.
         * @param filePath     FilePath where the PDF has to be stored.
         * @param listener     PDFUtilListener to send callback for PDF generation.
         */
        public GeneratePDF(final List<View> contentViews, final String filePath, final PDFUtil.PDFUtilListener listener) {
            this.mContentViews = contentViews;
            this.mFilePath = filePath;
            this.mListener = listener;
        }


        /**
         * Do In Background.
         *
         * @return TRUE if PDF successfully generated else FALSE.
         */
//        @Override
//        protected File doInBackground(Void... params) {
        public File startwriting(){
            try {
                // Create PDF Document.
                PdfDocument pdfDocument = new PdfDocument();

                // Write content to PDFDocument.
                writePDFDocument(pdfDocument);

                // Save document to file.
                return savePDFDocumentToStorage(pdfDocument);
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
                return null;
            }
        }

//        /**
//         * On Post Execute.
//         *
//         * @param savedPDFFile Saved pdf file, null if not generated successfully
//         */
//        @Override
//        protected void onPostExecute(File savedPDFFile) {
//            super.onPostExecute(savedPDFFile);
//            if (savedPDFFile != null) {
//                //Send Success callback.
//                mListener.pdfGenerationSuccess(savedPDFFile);
//            } else {
//                //Send Error callback.
//                mListener.pdfGenerationFailure(mException);
//            }
//        }

        /**
         * Writes given PDFDocument using content views.
         *
         * @param pdfDocument PDFDocument to be written.
         */
        private void writePDFDocument(final PdfDocument pdfDocument) {

            for (int i = 0; i < mContentViews.size(); i++) {

                //Get Content View.
                View contentView = mContentViews.get(i);

                // crate a page description
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.
                        Builder((int) PDF_PAGE_WIDTH, (int) PDF_PAGE_HEIGHT, i + 1).create();

                // start a page
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                // draw view on the page
                Canvas pageCanvas = page.getCanvas();
                pageCanvas.scale(1f, 1f);
                int pageWidth = pageCanvas.getWidth();
                int pageHeight = pageCanvas.getHeight();
                int measureWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
                int measuredHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
                contentView.measure(measureWidth, measuredHeight);
                contentView.layout(0, 0, pageWidth, pageHeight);
                contentView.draw(pageCanvas);

                // finish the page
                pdfDocument.finishPage(page);

            }
        }

        /**
         * Save PDFDocument to the File in the storage.
         *
         * @param pdfDocument Document to be written to the Storage.
         * @throws IOException
         */
        private File savePDFDocumentToStorage(final PdfDocument pdfDocument) throws IOException {
            FileOutputStream fos = null;
            // Create file.
            File pdfFile = null;
            if (mFilePath == null || mFilePath.isEmpty()) {
                pdfFile = File.createTempFile(Long.toString(new Date().getTime()), "pdf");
            } else {
                pdfFile = new File(mFilePath);
            }

            //Create parent directories
            File parentFile = pdfFile.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new IllegalStateException("Couldn't create directory: " + parentFile);
            }
            boolean fileExists = pdfFile.exists();
            // If File already Exists. delete it.
            if (fileExists) {
                fileExists = !pdfFile.delete();
            }
            try {
                if (!fileExists) {
                    // Create New File.
                    fileExists = pdfFile.createNewFile();
                }

                if (fileExists) {
                    // Write PDFDocument to the file.
                    fos = new FileOutputStream(pdfFile);
                    pdfDocument.writeTo(fos);

                    //Close output stream
                    fos.close();

                    // close the document
                    pdfDocument.close();
                }
                return pdfFile;
            } catch (IOException exception) {
                exception.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
                throw exception;
            }
        }
    }

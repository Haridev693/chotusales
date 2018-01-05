package com.refresh.chotusalesv1.ui.component;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.refresh.chotusalesv1.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 12/19/2017.
 */

    public class PrintCustomContent extends PrintDocumentAdapter {

        private static final int MILS_IN_INCH = 1000;

        private int mRenderPageWidth;
        private int mRenderPageHeight;
        private SimpleAdapter adapter;

        private PrintAttributes mPrintAttributes;
        private PrintDocumentInfo mDocumentInfo;
        private Context mPrintContext;
        private SparseIntArray mWrittenPages;
        private View mView;
        private  List<Map<String, String>> items;
        private PrintedPdfDocument mPdfDocument;

        public PrintCustomContent(Context context, View view, List<Map<String, String>> saleList) {
            mPrintContext = context;
            mView = view;
            items = saleList;
//            mPdfDocument = new PrintedPdfDocument();
        }





        @Override
        public void onLayout(final PrintAttributes oldAttributes,
                                             final PrintAttributes newAttributes,
                                             final CancellationSignal cancellationSignal,
                                             final LayoutResultCallback callback,
                                             final Bundle metadata) {


//
////                newAttributes = new PrintAttributes.Builder().setMediaSize()
//
//            PrintAttributes printAttrs = new PrintAttributes.Builder().
//                    setColorMode(PrintAttributes.COLOR_MODE_COLOR).
//                    setMediaSize(PrintAttributes.MediaSize.ISO_A4).
//                    setResolution(new PrintAttributes.Resolution("zooey", PRINT_SERVICE, 300, 300)).
//                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
//                    build();

            mPdfDocument = new PrintedPdfDocument(mPrintContext, newAttributes);

            mWrittenPages = new SparseIntArray();


//            mDocumentInfo = new PrintDocumentInfo();

                            // If we are already cancelled, don't do any work.
                            if (cancellationSignal.isCanceled()) {
                                callback.onLayoutCancelled();
                                return;
                            }

                            // Now we determined if the print attributes changed in a way that
                            // would change the layout and if so we will do a layout pass.
                            boolean layoutNeeded = false;

                            final int density = Math.max(newAttributes.getResolution().getHorizontalDpi(),
                                    newAttributes.getResolution().getVerticalDpi());

                            // Note that we are using the PrintedPdfDocument class which creates
                            // a PDF generating canvas whose size is in points (1/72") not screen
                            // pixels. Hence, this canvas is pretty small compared to the screen.
                            // The recommended way is to layout the content in the desired size,
                            // in this case as large as the printer can do, and set a translation
                            // to the PDF canvas to shrink in. Note that PDF is a vector format
                            // and you will not lose data during the transformation.

                            // The content width is equal to the page width minus the margins times
                            // the horizontal printer density. This way we get the maximal number
                            // of pixels the printer can put horizontally.
                            final int marginLeft = (int) (density * (float) newAttributes.getMinMargins()
                                    .getLeftMils() / MILS_IN_INCH);
                            final int marginRight = (int) (density * (float) newAttributes.getMinMargins()
                                    .getRightMils() / MILS_IN_INCH);
                            final int contentWidth = (int) (density * (float) newAttributes.getMediaSize()
                                    .getWidthMils() / MILS_IN_INCH) - marginLeft - marginRight;
                            if (mRenderPageWidth != contentWidth) {
                                mRenderPageWidth = contentWidth;
                                layoutNeeded = true;
                            }

                            // The content height is equal to the page height minus the margins times
                            // the vertical printer resolution. This way we get the maximal number
                            // of pixels the printer can put vertically.
                            final int marginTop = (int) (density * (float) newAttributes.getMinMargins()
                                    .getTopMils() / MILS_IN_INCH);
                            final int marginBottom = (int) (density * (float) newAttributes.getMinMargins()
                                    .getBottomMils() / MILS_IN_INCH);
                            final int contentHeight = (int) (density * (float) newAttributes.getMediaSize()
                                    .getHeightMils() / MILS_IN_INCH) - marginTop - marginBottom;
                            if (mRenderPageHeight != contentHeight) {
                                mRenderPageHeight = contentHeight;
                                layoutNeeded = true;
                            }

                            // Create a context for resources at printer density. We will
                            // be inflating views to render them and would like them to use
                            // resources for a density the printer supports.
                            if (mPrintContext == null || mPrintContext.getResources()
                                    .getConfiguration().densityDpi != density) {
                                Configuration configuration = new Configuration();
                                configuration.densityDpi = density;
                                mPrintContext = mPrintContext.createConfigurationContext(configuration);
                                mPrintContext.setTheme(android.R.style.Theme_Holo_Light);
                            }

                            // If no layout is needed that we did a layout at least once and
                            // the document info is not null, also the second argument is false
                            // to notify the system that the content did not change. This is
                            // important as if the system has some pages and the content didn't
                            // change the system will ask, the application to write them again.
                            if (!layoutNeeded) {
                                callback.onLayoutFinished(mDocumentInfo, false);
                                return;
                            }

                            // For demonstration purposes we will do the layout off the main
                            // thread but for small content sizes like this one it is OK to do
                            // that on the main thread.

                            // Store the data as we will layout off the main thread.
//            List<Map<String, String>> items =
            // getListAdapter()).cloneItems();
//            items.add(MapitemsLabel());

            adapter = new SimpleAdapter(mPrintContext , items,
                    R.layout.listview_report, new String[] { "id","buyername", "startTime","tax", "total"},
                    new int[] { R.id.sid,R.id.bname, R.id.startTime ,R.id.taxR, R.id.total});
                                    try {
                                        // Create an adapter with the stats and an inflater
                                        // to load resources for the printer density.
//                                        MotoGpStatAdapter adapter = new MotoGpStatAdapter(items,
//                                                (LayoutInflater) mPrintContext.getSystemService(
//                                                        Context.LAYOUT_INFLATER_SERVICE));

                                        int currentPage = 0;
                                        int pageContentHeight = 0;
                                        int viewType = -1;
                                        View view = null;
                                        LinearLayout dummyParent = new LinearLayout(mPrintContext);
                                        dummyParent.setOrientation(LinearLayout.VERTICAL);

                                        final int itemCount = adapter.getCount();
                                        for (int i = 0; i < itemCount; i++) {
                                            // Be nice and respond to cancellation.

                                            // Get the next view.
                                            final int nextViewType = adapter.getItemViewType(i);
                                            if (viewType == nextViewType) {
                                                view = adapter.getView(i, view, dummyParent);
                                            } else {
                                                view = adapter.getView(i, null, dummyParent);
                                            }
                                            viewType = nextViewType;

                                            // Measure the next view
                                            measureView(view);

                                            // Add the height but if the view crosses the page
                                            // boundary we will put it to the next page.
                                            pageContentHeight += view.getMeasuredHeight();
                                            if (pageContentHeight > mRenderPageHeight) {
                                                pageContentHeight = view.getMeasuredHeight();
                                                currentPage++;
                                            }
                                        }

                                        // Create a document info describing the result.
                                        mDocumentInfo = new PrintDocumentInfo
                                                .Builder("SalesReport.pdf")
                                                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                                .setPageCount(currentPage + 1)
                                                .build();

                                        // We completed the layout as a result of print attributes
                                        // change. Hence, if we are here the content changed for
                                        // sure which is why we pass true as the second argument.
                                        callback.onLayoutFinished(mDocumentInfo, true);
//                                        return info;
                                    } catch (Exception e) {
                                        // An unexpected error, report that we failed and
                                        // one may pass in a human readable localized text
                                        // for what the error is if known.
                                        callback.onLayoutFailed(null);
                                        throw new RuntimeException(e);
                                    }
                                }


                        @Override
                        public void onWrite(final PageRange[] pages,
                                            final ParcelFileDescriptor destination,
                                            final CancellationSignal cancellationSignal,
                                            final WriteResultCallback callback) {

                            // If we are already cancelled, don't do any work.
                            if (cancellationSignal.isCanceled()) {
                                callback.onWriteCancelled();
                                return;
                            }

                            // Store the data as we will layout off the main thread.
//                            final List<Sale> items =

                                    // Go over all the pages and write only the requested ones.
                                    // Create an adapter with the stats and an inflater
                                    // to load resources for the printer density.
//                                    MotoGpStatAdapter adapter = new MotoGpStatAdapter(items,
//                                            (LayoutInflater) mPrintContext.getSystemService(
//                                                    Context.LAYOUT_INFLATER_SERVICE));

                                    int currentPage = 0;
                                    int pageContentHeight = 0;
                                    int viewType = -1;
                                    View view = null;
                                    PdfDocument.Page page = null;
                                    LinearLayout dummyParent = new LinearLayout(mPrintContext);
                                    dummyParent.setOrientation(LinearLayout.VERTICAL);

                                    // The content is laid out and rendered in screen pixels with
                                    // the width and height of the paper size times the print
                                    // density but the PDF canvas size is in points which are 1/72",
                                    // so we will scale down the content.
                                    final float scale =  Math.min(
                                            (float) mPdfDocument.getPageContentRect().width()
                                                    / mRenderPageWidth,
                                            (float) mPdfDocument.getPageContentRect().height()
                                                    / mRenderPageHeight);

                                    final int itemCount = adapter.getCount();
                                    for (int i = 0; i < itemCount; i++) {
                                        // Be nice and respond to cancellation.
//                                        if (isCancelled()) {

                                        // Get the next view.
                                        final int nextViewType = adapter.getItemViewType(i);
                                        if (viewType == nextViewType) {
                                            view = adapter.getView(i, view, dummyParent);
                                        } else {
                                            view = adapter.getView(i, null, dummyParent);
                                        }
                                        viewType = nextViewType;

                                        // Measure the next view
                                        measureView(view);

                                        // Add the height but if the view crosses the page
                                        // boundary we will put it to the next one.
                                        pageContentHeight += view.getMeasuredHeight();
                                        if (currentPage < 1 || pageContentHeight > mRenderPageHeight) {
                                            pageContentHeight = view.getMeasuredHeight();
                                            // Done with the current page - finish it.
                                            if (page != null) {
                                                mPdfDocument.finishPage(page);
                                            }
                                            // If the page is requested, render it.
                                            if (containsPage(pages, currentPage)) {
                                                page = mPdfDocument.startPage(currentPage);
                                                page.getCanvas().scale(scale, scale);
                                                // Keep track which pages are written.
                                                mWrittenPages.append(mWrittenPages.size(), currentPage);
                                            } else {
                                                page = null;
                                            }
                                            currentPage++;
                                        }

                                        // If the current view is on a requested page, render it.
                                        if (page != null) {
                                            // Layout an render the content.
                                            view.layout(0, 0, view.getMeasuredWidth(),
                                                    view.getMeasuredHeight());
                                            view.draw(page.getCanvas());
                                            // Move the canvas for the next view.
                                            page.getCanvas().translate(0, view.getHeight());
                                        }
                                    }

                                    // Done with the last page.
                                    if (page != null) {
                                        mPdfDocument.finishPage(page);
                                    }

                                    // Write the data and return success or failure.
                                    try {
                                        mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                                        // Compute which page ranges were written based on
                                        // the bookkeeping we maintained.
                                        PageRange[] pageRanges = computeWrittenPageRanges(mWrittenPages);
                                        callback.onWriteFinished(pageRanges);
                                    } catch (IOException ioe) {
                                        callback.onWriteFailed(null);
                                    } finally {
                                        mPdfDocument.close();
                                    }

//                                    return null;
                                }





        private void measureView(View view) {
            final int widthMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(mRenderPageWidth,
                            View.MeasureSpec.EXACTLY), 0, view.getLayoutParams().width);
            final int heightMeasureSpec = ViewGroup.getChildMeasureSpec(
                    View.MeasureSpec.makeMeasureSpec(mRenderPageHeight,
                            View.MeasureSpec.EXACTLY), 0, view.getLayoutParams().height);
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        private PageRange[] computeWrittenPageRanges(SparseIntArray writtenPages) {
            List<PageRange> pageRanges = new ArrayList<PageRange>();

            int start = 0;
            int end = 0;
            final int writtenPageCount = writtenPages.size();
            for (int i = 0; i < writtenPageCount; i++) {
                if (start < 1) {
                    start = writtenPages.valueAt(i);
                }
                int oldEnd = end = start;
                while (i < writtenPageCount && (end - oldEnd) <= 1) {
                    oldEnd = end;
                    end = writtenPages.valueAt(i);
                    i++;
                }
                PageRange pageRange = new PageRange(start, end);
                pageRanges.add(pageRange);
                start = end = 0;
            }

            PageRange[] pageRangesArray = new PageRange[pageRanges.size()];
            pageRanges.toArray(pageRangesArray);
            return pageRangesArray;
        }

        private boolean containsPage(PageRange[] pageRanges, int page) {
            final int pageRangeCount = pageRanges.length;
            for (int i = 0; i < pageRangeCount; i++) {
                if (pageRanges[i].getStart() <= page
                        && pageRanges[i].getEnd() >= page) {
                    return true;
                }
            }
            return false;
        }
    }

//        private List<MotoGpStatItem> loadMotoGpStats() {
//            String[] years = getResources().getStringArray(R.array.motogp_years);
//            String[] champions = getResources().getStringArray(R.array.motogp_champions);
//            String[] constructors = getResources().getStringArray(R.array.motogp_constructors);
//
//            List<MotoGpStatItem> items = new ArrayList<MotoGpStatItem>();
//
//            final int itemCount = years.length;
//            for (int i = 0; i < itemCount; i++) {
//                MotoGpStatItem item = new MotoGpStatItem();
//                item.year = years[i];
//                item.champion = champions[i];
//                item.constructor = constructors[i];
//                items.add(item);
//            }
//
//            return items;
//        }

//        private static final class MotoGpStatItem {
//            String year;
//            String champion;
//            String constructor;
//        }
//
//        private class MotoGpStatAdapter extends BaseAdapter {
//            private final List<Sale> mItems;
//            private final LayoutInflater mInflater;
//
//            public MotoGpStatAdapter(List<Sale> items, LayoutInflater inflater) {
//                mItems = items;
//                mInflater = inflater;
//            }
//
//            public List<Sale> cloneItems() {
//                return new ArrayList<Sale>(mItems);
//            }
//
//            @Override
//            public int getCount() {
//                return mItems.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return mItems.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    convertView = mInflater.inflate(R.layout.layout_report, parent, false);
//                }
//
//
//
////                MotoGpStatItem item = (MotoGpStatItem) getItem(position);
////
////                TextView yearView = (TextView) convertView.findViewById(R.id.year);
////                yearView.setText(item.year);
////
////                TextView championView = (TextView) convertView.findViewById(R.id.champion);
////                championView.setText(item.champion);
////
////                TextView constructorView = (TextView) convertView.findViewById(R.id.constructor);
////                constructorView.setText(item.constructor);
//
//                return convertView;
//            }
//        }
//    }

package com.refresh.chotusalesv1.ui.sale;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.DateTimeStrategy;
import com.refresh.chotusalesv1.domain.sale.BuyerClass;
import com.refresh.chotusalesv1.domain.sale.Sale;
import com.refresh.chotusalesv1.domain.sale.SaleLedger;
import com.refresh.chotusalesv1.techicalservices.PDFUtil;
import com.refresh.chotusalesv1.ui.component.PrintCustomContent;
import com.refresh.chotusalesv1.ui.component.UpdatableFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.PRINT_SERVICE;
import static com.refresh.chotusalesv1.techicalservices.taxutil.round;

/**
 * UI for showing sale's record.
\developed by Sri Haridev Software Solutions
 *
 */
public class ReportFragment extends UpdatableFragment implements PDFUtil.PDFUtilListener {
	
	private SaleLedger saleLedger;
	List<Map<String, String>> saleList, StableSaleList;
	private ListView saleLedgerListView;
	private TextView totalBox;
	private Spinner spinner;
	private Button previousButton;
	private Button nextButton;
	private TextView currentBox;
	private Calendar currentTime;
	private DatePickerDialog datePicker;
	private Button printReport;
	private View mRootView,Heading,Dateformat;
	private String TaxTotal;

	private String mFilePath;

	private SimpleAdapter sAdap;
	
	public static final int DAILY = 0;
	public static final int WEEKLY = 1;
	public static final int MONTHLY = 2;
	public static final int YEARLY = 3;
    public List<View> AllViewslist;
    private View totallayout;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
//		try {
			saleLedger = new SaleLedger(getActivity().getApplicationContext());
//		} catch (NoDaoSetException e) {
//			e.printStackTrace();
//		}

		View view = inflater.inflate(R.layout.layout_report, container, false);

		mRootView = view.findViewById(R.id.rootView);
		previousButton = (Button) view.findViewById(R.id.previousButton);
		nextButton = (Button) view.findViewById(R.id.nextButton);
		currentBox = (TextView) view.findViewById(R.id.currentBox);
		saleLedgerListView = (ListView) view.findViewById(R.id.saleListView);
		totalBox = (TextView) view.findViewById(R.id.totalBox);
		spinner = (Spinner) view.findViewById(R.id.spinner1);
		printReport = (Button) view.findViewById(R.id.printButton);
        Dateformat = view.findViewById(R.id.layoutDatehor);
        Heading = view.findViewById(R.id.headingofList);
        totallayout = view.findViewById(R.id.totallayout);
		
		initUI();
		return view;
	}

	/**
	 * Initiate this UI.
	 */
	private void initUI() {
		currentTime = Calendar.getInstance();
		datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int y, int m, int d) {
				currentTime.set(Calendar.YEAR, y);
				currentTime.set(Calendar.MONTH, m);
				currentTime.set(Calendar.DAY_OF_MONTH, d);
				update();
			}
		}, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
		        R.array.period, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);
		spinner.setSelection(0);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {	
				update();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
			
		});

		StableSaleList = new ArrayList<Map<String, String>>();
		
		currentBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePicker.show();
			}
		});

		printReport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				StableSaleList = new ArrayList<Map<String, String>>();
                StableSaleList.add(MapitemsLabel(5));
				StableSaleList.add(MapitemsLabel(2));// Date Time
				StableSaleList.add(MapitemsLabel(3));// Seperator
				StableSaleList.add(MapitemsLabel(0));//Items name
				StableSaleList.add(MapitemsLabel(3));// Seperator

				StableSaleList.addAll(saleList);
				StableSaleList.add(MapitemsLabel(3));// Seperator
				StableSaleList.add(MapitemsLabel(4));
                StableSaleList.add(MapitemsLabel(5));
				StableSaleList.add(MapitemsLabel(1));// Total


				PrintManager printManager = (PrintManager) getActivity().getSystemService(PRINT_SERVICE);
        		printManager.print("print_any_view_job_name", new PrintCustomContent(getActivity(),
                mRootView, StableSaleList),null);
			}
		});
		
		
		
		previousButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addDate(-1);
			}
		});
		
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addDate(1);
			}
		});
		
		saleLedgerListView.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
		    	  String id = saleList.get(position).get("id").toString();
		    	  Intent newActivity = new Intent(getActivity().getBaseContext(), SaleDetailActivity.class);
		          newActivity.putExtra("id", id);
		          startActivity(newActivity);  
		      }     
		});


		
	}
	
	/**
	 * Show list.
	 * @param list
	 */
	private void showList(List<Sale> list) {

		saleList = new ArrayList<Map<String, String>>();
		for (Sale sale : list) {
			String buyername = getBuyerName(sale.getBuyerid()).Buyername;
			String buyerPhone = getBuyerName(sale.getBuyerid()).BuyerPhone;
			sale.SetBuyername(buyername);
			sale.setBuyerPhone(buyerPhone);
			saleList.add(sale.toMap());

//			sale.getBuyerid();
		}


		sAdap = new SimpleAdapter(getActivity().getBaseContext() , saleList,
				R.layout.listview_report, new String[] { "id","buyername", "startTime","tax", "total"},
				new int[] { R.id.sid,R.id.bname, R.id.startTime ,R.id.taxR, R.id.total});

		saleLedgerListView.setAdapter(sAdap);

	}



	public Map<String, String> MapitemsLabel(int id)
	{
		Map<String, String> map = new HashMap<String, String>();
		if(id==0) {
			map.put("id", "id");
			map.put("startTime", "starttime");
			map.put("endTime", "endtime");
//            map.put("status", getStatus());
			map.put("total", "Total");
			map.put("tax", "tax");
			map.put("buyername", "buyername");
		}
		else if(id==1)
		{
			map.put("id", "TOTAL ");
			map.put("startTime", " ");
			map.put("endTime", " ");
//            map.put("status", getStatus());
			map.put("total", totalBox.getText().toString());
			map.put("tax", " ");
			map.put("buyername", "  ");

		}
		else if(id==2)
		{
			map.put("id", "DATETIME:");
			map.put("startTime", " ");
			map.put("endTime", " ");
//            map.put("status", getStatus());
			map.put("total", currentBox.getText().toString() );
			map.put("tax", " ");
			map.put("buyername", "  ");
		}
		else if(id==3)
		{
			map.put("id", "----------------");
			map.put("startTime", "-----------------------------");
			map.put("endTime", " ");
//            map.put("status", getStatus());
			map.put("total", "------------------------------" );
			map.put("tax", "---------------------------");
			map.put("buyername", "-------------------------");

		}
		else if(id ==4)
		{
			map.put("id", "TAX TOTAL:");
			map.put("startTime", " ");
			map.put("endTime", " ");
//            map.put("status", getStatus());
			map.put("total", " " );
			map.put("tax", TaxTotal);
			map.put("buyername", "  ");

		}
		else if(id==5)
        {
            map.put("id", " ");
            map.put("startTime", " ");
            map.put("endTime", " ");
//            map.put("status", getStatus());
            map.put("total", " " );
            map.put("tax", " ");
            map.put("buyername", "  ");
        }

		return map;
	}

	private BuyerClass getBuyerName(int buyerid) {
		return saleLedger.getBuyerName(buyerid);
	}

	@Override
	public void update() {
//		StableSaleList = new ArrayList<Map<String, String>>();
		int period = spinner.getSelectedItemPosition();
		List<Sale> list = null;
		Calendar cTime = (Calendar) currentTime.clone();
		Calendar eTime = (Calendar) currentTime.clone();
		
		if(period == DAILY){
			currentBox.setText(" [" + DateTimeStrategy.getSQLDateFormat(currentTime) +  "] ");
			currentBox.setTextSize(16);
		} else if (period == WEEKLY){
			while(cTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				cTime.add(Calendar.DATE, -1);
			}
			
			String toShow = " [" + DateTimeStrategy.getSQLDateFormat(cTime) +  "] ~ [";
			eTime = (Calendar) cTime.clone();
			eTime.add(Calendar.DATE, 7);
			toShow += DateTimeStrategy.getSQLDateFormat(eTime) +  "] ";
			currentBox.setTextSize(16);
			currentBox.setText(toShow);
		} else if (period == MONTHLY){
			cTime.set(Calendar.DATE, 1);
			eTime = (Calendar) cTime.clone();
			eTime.add(Calendar.MONTH, 1);
			eTime.add(Calendar.DATE, -1);
			currentBox.setTextSize(18);
			currentBox.setText(" [" + currentTime.get(Calendar.YEAR) + "-" + (currentTime.get(Calendar.MONTH)+1) + "] ");
		} else if (period == YEARLY){
			cTime.set(Calendar.DATE, 1);
			cTime.set(Calendar.MONTH, 0);
			eTime = (Calendar) cTime.clone();
			eTime.add(Calendar.YEAR, 1);
			eTime.add(Calendar.DATE, -1);
			currentBox.setTextSize(20);
			currentBox.setText(" [" + currentTime.get(Calendar.YEAR) +  "] ");
		}
		currentTime = cTime;
		list = saleLedger.getAllSaleDuring(cTime, eTime);
		double total = 0;
		double taxTotal =0;
		for (Sale sale : list) {
			if(sale.getTranTax()){
				total+= sale.Total- sale.getDiscount();
			}
			else {
				total += sale.getTotal()- sale.getDiscount();
			}
			taxTotal += sale.getTaxTotal();
		}

		TaxTotal= String.valueOf(round(taxTotal,2));
		
		totalBox.setText(round(total,2) + "");
		showList(list);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// update();
		// it shouldn't call update() anymore. Because super.onResume() 
		// already fired the action of spinner.onItemSelected()
	}
	
	/**
	 * Add date.
	 * @param increment
	 */
	private void addDate(int increment) {
		int period = spinner.getSelectedItemPosition();
		if (period == DAILY){
			currentTime.add(Calendar.DATE, 1 * increment);
		} else if (period == WEEKLY){
			currentTime.add(Calendar.DATE, 7 * increment);
		} else if (period == MONTHLY){
			currentTime.add(Calendar.MONTH, 1 * increment);
		} else if (period == YEARLY){
			currentTime.add(Calendar.YEAR, 1 * increment);
		}
		update();
	}

    @Override
    public void pdfGenerationSuccess(File savedPDFFile) {
        Toast.makeText(getActivity(),"Saved PDF file at:"+ savedPDFFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void pdfGenerationFailure(Exception exception) {

        Toast.makeText(getActivity(),"Couldn't save file with error:"+ exception.getMessage(),Toast.LENGTH_SHORT).show();
    }



//	private void writePDFDocument(final PdfDocument pdfDocument) {
//
//		for (int i = 0; i < AllViewslist.size(); i++) {
//
//			//Get Content View.
//			View contentView = AllViewslist.get(i);
//
//			// crate a page description
//			PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.
//					Builder((int) PDF_PAGE_WIDTH, (int) PDF_PAGE_HEIGHT, i + 1).create();
//
//			// start a page
//			PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//			// draw view on the page
//			Canvas pageCanvas = page.getCanvas();
//			pageCanvas.scale(1f, 1f);
//			int pageWidth = pageCanvas.getWidth();
//			int pageHeight = pageCanvas.getHeight();
//			int measureWidth = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
//			int measuredHeight = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
//			contentView.measure(measureWidth, measuredHeight);
//			contentView.layout(0, 0, pageWidth, pageHeight);
//			contentView.draw(pageCanvas);
//
//			// finish the page
//			pdfDocument.finishPage(page);
//
//		}
//	}
//
//
//	private File savePDFDocumentToStorage(final PdfDocument pdfDocument) throws IOException {
//		FileOutputStream fos = null;
//		// Create file.
//		File pdfFile = null;
//		if (mFilePath == null || mFilePath.isEmpty()) {
//			pdfFile = File.createTempFile(Long.toString(new Date().getTime()), "pdf");
//		} else {
//			pdfFile = new File(mFilePath);
//		}
//
//		//Create parent directories
//		File parentFile = pdfFile.getParentFile();
//		if (!parentFile.exists() && !parentFile.mkdirs()) {
//			throw new IllegalStateException("Couldn't create directory: " + parentFile);
//		}
//		boolean fileExists = pdfFile.exists();
//		// If File already Exists. delete it.
//		if (fileExists) {
//			fileExists = !pdfFile.delete();
//		}
//		try {
//			if (!fileExists) {
//				// Create New File.
//				fileExists = pdfFile.createNewFile();
//			}
//
//			if (fileExists) {
//				// Write PDFDocument to the file.
//				fos = new FileOutputStream(pdfFile);
//				pdfDocument.writeTo(fos);
//
//				//Close output stream
//				fos.close();
//
//				// close the document
//				pdfDocument.close();
//			}
//			return pdfFile;
//		} catch (IOException exception) {
//			exception.printStackTrace();
//			if (fos != null) {
//				fos.close();
//			}
//			throw exception;
//		}
//	}
}



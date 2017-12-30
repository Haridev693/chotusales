package com.refresh.chotusalesv1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.refresh.chotusalesv1.domain.DateTimeStrategy;

import org.achartengine.GraphicalView;

import java.util.Calendar;

import butterknife.BindView;

import static com.refresh.chotusalesv1.ui.sale.ReportFragment.DAILY;
import static com.refresh.chotusalesv1.ui.sale.ReportFragment.MONTHLY;
import static com.refresh.chotusalesv1.ui.sale.ReportFragment.WEEKLY;
import static com.refresh.chotusalesv1.ui.sale.ReportFragment.YEARLY;

public class ShowSalesGraph extends AppCompatActivity {


    @BindView(R.id.SalesgraphView)
    GraphicalView salesGraph;

    @BindView(R.id.salesSpinner)
    Spinner saleSpinner;
    private LineGraphSeries<DataPoint> series;
    private Calendar currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sales_graph);
        series = new LineGraphSeries<DataPoint>();
        currentTime = Calendar.getInstance();


//        series.appendData(new DataPoint());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        saleSpinner.setAdapter(adapter);
        saleSpinner.setSelection(0);
        saleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                updategraph(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

    }

    private void updategraph(Integer pos) {

        Calendar cTime = (Calendar) currentTime.clone();
        Calendar eTime = (Calendar) currentTime.clone();

        if(pos == DAILY){
//            currentBox.setText(" [" + DateTimeStrategy.getSQLDateFormat(currentTime) +  "] ");
//            currentBox.setTextSize(16);
        } else if (pos == WEEKLY){
            while(cTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                cTime.add(Calendar.DATE, -1);
            }

            String toShow = " [" + DateTimeStrategy.getSQLDateFormat(cTime) +  "] ~ [";
            eTime = (Calendar) cTime.clone();
            eTime.add(Calendar.DATE, 7);
            toShow += DateTimeStrategy.getSQLDateFormat(eTime) +  "] ";
        } else if (pos == MONTHLY){
            cTime.set(Calendar.DATE, 1);
            eTime = (Calendar) cTime.clone();
            eTime.add(Calendar.MONTH, 1);
            eTime.add(Calendar.DATE, -1);
//            currentBox.setTextSize(18);
//            currentBox.setText(" [" + currentTime.get(Calendar.YEAR) + "-" + (currentTime.get(Calendar.MONTH)+1) + "] ");
        } else if (pos == YEARLY){
            cTime.set(Calendar.DATE, 1);
            cTime.set(Calendar.MONTH, 0);
            eTime = (Calendar) cTime.clone();
            eTime.add(Calendar.YEAR, 1);
            eTime.add(Calendar.DATE, -1);
        }


//        switch(pos)
//        {
//            case 0:{
//
//                break;
//            }
//            case 1:{
//                break;
//            }
//            case 2:{
//                break;
//            }
//            case 3:{
//                break;
//            }
//
//        }

    }
}

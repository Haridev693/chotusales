package com.refresh.chotusalesv1.domain.inventory;

import android.util.Log;

/**
 * Created by Lenovo on 12/13/2017.
 */



public class productdetail{
        public double tax;
        public double taxUnitPrice;

        public productdetail()
        {

        }

    public productdetail calculatetax(double oldPrice, double taxRate, Boolean IsInclusive)
    {
        productdetail p = new productdetail();
        if(IsInclusive)
        {

            try {
            p.taxUnitPrice  = round((oldPrice / ((taxRate /100) + 1 )),2);


//                p.taxUnitPrice= round(((100 * oldPrice) / (100 + taxRate)),2);
            p.tax = round((oldPrice - p.taxUnitPrice),2);//((p.taxUnitPrice * taxRate) / 100),2);

            }catch(Exception e) {

                Log.i("MathError:", e.getMessage());
            }
        }else
        {
            p.taxUnitPrice = oldPrice;
            p.tax= round(((p.taxUnitPrice*taxRate)/100),2);
//            this.ItemTotal = round(Price + tax,2);//Price+tax;// *taxRate)/100;
        }
        return p;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}


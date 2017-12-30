package com.refresh.chotusalesv1.techicalservices;

import android.util.Log;

/**
 * Created by Lenovo on 12/1/2017.
 */

public class taxutil {

    public double Price;
    public double tax;
    public double ItemTotal;
    public Boolean IsInclusive;


    public taxutil()
    {}

    public taxutil(double Price, double tax, Boolean IsInclusive)
    {
        this.Price = Price;
        this.tax = tax;
        this.IsInclusive = IsInclusive;
    }


    public taxutil calculatetax(double oldPrice, double taxRate, Boolean IsInclusive)
    {
        if(IsInclusive)
        {
            try {
//                this.Price = round(((Price * taxRate) / 100),2);
                this.Price = round(((100 * oldPrice) / (100 + taxRate)),2);
                this.tax = round(((Price * taxRate) / 100),2);
//                this.ItemTotal = round(Price + tax,2);
//                round(this.ItemTotal,2);
            }catch(Exception e) {

                Log.i("MathError:", e.getMessage());
            }
            //Price =
        }else
        {
            this.Price = oldPrice;
            this.tax = round(((Price*taxRate)/100),2);
//            this.ItemTotal = round(Price + tax,2);//Price+tax;// *taxRate)/100;
        }
        return this;
    }




    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
//
//    public static Float round(float value, int scale) {
//        int pow = 10;
//        for (int i = 1; i < scale; i++) {
//            pow *= 10;
//        }
//        float tmp = value * pow;
//        float tmpSub = tmp - (int) tmp;
//
//        return ( (float) ( (int) (
//                value >= 0
//                        ? (tmpSub >= 0.5f ? tmp + 1 : tmp)
//                        : (tmpSub >= -0.5f ? tmp : tmp - 1)
//        ) ) ) / pow;
//
//        // Below will only handles +ve values
//        // return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
//    }


}

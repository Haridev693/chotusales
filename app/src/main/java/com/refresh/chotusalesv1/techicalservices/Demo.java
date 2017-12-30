package com.refresh.chotusalesv1.techicalservices;

import android.content.Context;
import android.util.Log;

import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.domain.inventory.Inventory;
import com.refresh.chotusalesv1.domain.inventory.ProductCatalog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Reads a demo products from CSV in res/raw/
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class Demo {

	/**
	 * Adds the demo product to inventory.
	 * @param context The current stage of the application.
	 */
	public static void testProduct(Context context) {
        InputStream instream = context.getResources().openRawResource(R.raw.products);
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		Inventory inv = new Inventory(context);
		String line;
		try {
			ProductCatalog catalog = inv.getProductCatalog();
			while (( line = reader.readLine()) != null ) {
				String[] contents = line.split(",");
				Log.d("Demo", contents[0] + ":" + contents[1] + ": " + contents[2]);
				catalog.addProduct(contents[1], contents[0], Double.parseDouble(contents[2]),Integer.parseInt(contents[4]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

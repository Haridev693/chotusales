package com.refresh.chotusalesv1.ui.component;

import android.support.v4.app.Fragment;

/**
 * Fragment which is able to call update() from other class.
 * This is used by Delegation pattern.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public abstract class UpdatableFragment extends Fragment {

	/**
	 * Update fragment.
	 */
	public abstract void update();

}

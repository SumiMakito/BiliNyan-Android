package moe.feng.bilinyan.ui.fragment.home;

import android.app.Fragment;
import android.os.Bundle;

import moe.feng.bilinyan.R;
import moe.feng.bilinyan.ui.fragment.SectionHomeFragment;
import moe.feng.bilinyan.view.ObservableScrollView;

public class PlaceholderFragment extends BaseHomeFragment {

	private ObservableScrollView mScrollView;

	public static PlaceholderFragment newInstance(int minimumY) {
		PlaceholderFragment fragment = new PlaceholderFragment(minimumY);
		return fragment;
	}

	private PlaceholderFragment(int y) {
		super(y);
	}

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_tab_placeholder;
	}

	@Override
	public void finishCreateView(Bundle state) {
		mScrollView = $(R.id.scrollable);

		mScrollView.addOnScrollChangeListener(new ObservableScrollView.OnScrollChangeListener() {
			@Override
			public void onScrollChanged(ObservableScrollView view, int x, int y, int oldx, int oldy) {
				Fragment fragment = getParentFragment();
				if (fragment != null && fragment instanceof SectionHomeFragment) {
					((SectionHomeFragment) fragment).onScrollChanged(x, y, oldx, oldy);
				}
			}
		});
		mScrollView.scrollTo(mScrollView.getScrollX(), getMinimumScrollY());
	}


	@Override
	public void onMinimumScrollYSet(int y) {
		if (mScrollView.getScrollY() <= y) {
			mScrollView.scrollTo(mScrollView.getScrollX(), y);
		}
	}

}

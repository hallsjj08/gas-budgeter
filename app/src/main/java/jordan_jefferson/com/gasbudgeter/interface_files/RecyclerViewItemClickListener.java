package jordan_jefferson.com.gasbudgeter.interface_files;

import android.view.View;

/*
An interface to provide an onClickItem method for the RecyclerView.
 */

public interface RecyclerViewItemClickListener {

    void recyclerViewItemClicked(View v, int position);

}

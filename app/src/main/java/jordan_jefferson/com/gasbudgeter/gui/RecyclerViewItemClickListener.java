package jordan_jefferson.com.gasbudgeter.gui;

import android.view.View;

/*
An interface to provide an onClickItem method for the RecyclerView.
 */

//TODO: create an interface that allows the user to swipe an item to show a delete button.
public interface RecyclerViewItemClickListener {

    void recyclerViewItemClicked(View v, int position);

}
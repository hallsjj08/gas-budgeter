package jordan_jefferson.com.gasbudgeter.gui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import jordan_jefferson.com.gasbudgeter.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FragmentTransaction fragmentTransaction;
    private Fragment navigatedFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_destination:
                    //mTextMessage.setText(R.string.destination);
                    return true;
                case R.id.navigation_garage:
                    navigatedFragment = GarageFragment.newInstance();
                    replaceFragment(navigatedFragment);
                    return true;
                case R.id.navigation_about:
                    //mTextMessage.setText(R.string.about);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void replaceFragment(Fragment fragment){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}

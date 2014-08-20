package com.DJG.fd;
import android.content.Intent;
import android.widget.EditText;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	public final static String EXTRA_MESSAGE = "com.DJG.fd.MESSAGE";
	
	public void startSurvival(View view) {
		if(GameActivity.gameContext==null) {
			GameActivity.gameContext =  this.getApplicationContext();
		}
		GameActivity.gameContext =  this.getApplicationContext();
		GameActivity.setMode("Survival");
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void startCampaign(View view) {
		if(GameActivity.gameContext==null) {
			GameActivity.gameContext =  this.getApplicationContext();
		}
		Intent intent = new Intent(this, CampaignActivity.class);
		startActivity(intent);
	}
	
	public void openStore(View view) {
		Intent intent = new Intent(this, Store.class);
		startActivity(intent);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}

package com.DJG.fd;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class DisplayMessageActivity extends ActionBarActivity {
	
	// Shared data.
	SharedPreferences prefs;
    
	// The current game thread.
	private Thread gameThread;
	private volatile static boolean gameOver;
	
	// Grabbed units (two, one for each hand);
	private static Unit grabbedUnit = null;
	private static Unit secondGrabbedUnit = null;
	
	// The current view.
	private static View currentView;
	private static int screenHeight;
	private static int screenWidth;
	
	// Text, that's all.
	public static String levelText;
	public static String castleHP;
	public static String highScoreText;
	public static String previousHighScoreText;
	
	// List of all units. This list is constantly redrawn.
	public static ArrayList<Unit> allUnits = new ArrayList<Unit>();
	public final static Object allUnitsLock = new Object(); // A lock so we don't fuck up the allUnits
	public static boolean doOnce = true;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) { 
		
		// Respond to a single touch event
	    int action = MotionEventCompat.getActionMasked(event);
	    if(action == android.view.MotionEvent.ACTION_DOWN) {
	    	grabbedUnit = getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(0))),event.getY(event.findPointerIndex(event.getPointerId(0))));
	    }
	    else if(action == android.view.MotionEvent.ACTION_UP) {
	    	if(grabbedUnit != null && grabbedUnit.getKillable() && grabbedUnit == getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(0))),event.getY(event.findPointerIndex(event.getPointerId(0))))) {
	    		grabbedUnit.die();
	    	}
	    }
	    
	    // Respond to a multitouch event.
	    if(event.getPointerCount() > 1) {
			// Respond to the first touch event.
		    if(action == android.view.MotionEvent.ACTION_POINTER_DOWN) {
		    	grabbedUnit = getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(0))),event.getY(event.findPointerIndex(event.getPointerId(0))));
		    }
		    else if(action == android.view.MotionEvent.ACTION_POINTER_UP) {
		    	if(grabbedUnit != null && grabbedUnit.getKillable() && grabbedUnit == getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(0))),event.getY(event.findPointerIndex(event.getPointerId(0))))) {
		    		grabbedUnit.die();
		    	}
		    }
		    // Respond to the second touch event.
	    	if(action == android.view.MotionEvent.ACTION_POINTER_DOWN) {
	    	 	secondGrabbedUnit = getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(1))),event.getY(event.findPointerIndex(event.getPointerId(1))));
	    	}
	    	else if(action == android.view.MotionEvent.ACTION_POINTER_UP && secondGrabbedUnit == getUnitAt(event.getX(event.findPointerIndex(event.getPointerId(1))),event.getY(event.findPointerIndex(event.getPointerId(1))))) {
	    	 	if(secondGrabbedUnit != null && secondGrabbedUnit.getKillable()) {
	    			secondGrabbedUnit.die();
	    		}
	    	}
	    }
	    return true;
	}
	
	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
		  
	         super.onCreate(savedInstanceState);
	         requestWindowFeature(Window.FEATURE_NO_TITLE);
	         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	         prefs = this.getSharedPreferences("flickOffGame", Context.MODE_PRIVATE);
	         View v = new gameView(this);
	         setContentView(v);
	         currentView = v;

			 if(doOnce) { 
				 gameOver = false;
				 levelText = "Wave " + (Wave.getCurrentWaveNumber() + 1);
				 highScoreText = "";
				 previousHighScoreText = "";
				 castleHP = "Health ";
				 initGame();
				 playGame();
				 doOnce = false;
			 }
			 else {
			 }
	  }
	 
	  private class gameView extends View {
	 
	      public gameView(Context context) {
	          super(context);
	      }
	 
	      @Override
	      protected void onDraw(Canvas canvas) {
	          Paint myPaint = new Paint();
	          canvas.drawColor(Color.BLACK);
	          
	          // Draw our text.
  	        	myPaint.setStyle(Paint.Style.STROKE);
  	        	myPaint.setStrokeWidth(3);
  	        	myPaint.setTextSize(50);
  	        	myPaint.setColor(Color.WHITE);
  	        	canvas.drawText(levelText,50f,50f,myPaint);
  	        	canvas.drawText(highScoreText,50f,100f,myPaint);
  	        	canvas.drawText(previousHighScoreText,50f,150f,myPaint);
  	        	canvas.drawText(castleHP, 50f, (float)(screenHeight-50) ,myPaint );
  	        	
  	          // Draw all of our units (just one for now)
	          synchronized(allUnitsLock) {
	        	  for(Unit currentUnit : allUnits) {
	    	        myPaint.setStyle(Paint.Style.FILL);
	        	  	myPaint.setStrokeWidth(23);
	        	  	if(currentUnit.getName() == "Fortress") {
	        		  	myPaint.setStrokeWidth(1);
	        		  	myPaint.setStyle(Paint.Style.STROKE);
	        	  		myPaint.setColor(currentUnit.color);
        		  		canvas.drawCircle(currentUnit.getX(), currentUnit.getY(), currentUnit.getRadius(), myPaint);
	        	  	}
	        	  	else {
	        	  		// What shape do we draw?
	        	  		myPaint.setColor(currentUnit.color);
	        	  		if(currentUnit.getShape() == "Circle") {
	        	  			canvas.drawCircle(currentUnit.getX(), currentUnit.getY(), currentUnit.getRadius(), myPaint);
	        	  		}
	        	  		if(currentUnit.getShape() == "Square") {
	      	              canvas.drawRect(currentUnit.getX(), currentUnit.getY(), currentUnit.getX() + currentUnit.getRadius(), currentUnit.getY() + currentUnit.getRadius(), myPaint );
	        	  		}
	        	  		if(currentUnit.getShape() == "Plus") {
	    	        	  	myPaint.setStrokeWidth(6);
	        	  			canvas.drawLine(currentUnit.getX() + currentUnit.getRadius(), currentUnit.getY(), currentUnit.getX() + currentUnit.getRadius(), currentUnit.getY() + currentUnit.getRadius()*2, myPaint);
	      	            	canvas.drawLine(currentUnit.getX(), currentUnit.getY() + currentUnit.getRadius(), currentUnit.getX() + currentUnit.getRadius()*2, currentUnit.getY() + currentUnit.getRadius(), myPaint);
	        	  		}
	        	  		if(currentUnit.getShape() == "Triangle") {
	        	  			canvas.drawCircle(currentUnit.getX(), currentUnit.getY(), currentUnit.getRadius(), myPaint);
	        	  		}
	        	  	}
	          	}
	          }
	      }
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
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

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_message,
					container, false);
			return rootView;
		}
	}
	
	// Get the selected unit at the coordinates.
	public Unit getUnitAt(float x, float y) {
		synchronized(allUnitsLock) {
			// Spare the plusses if possible.
			for(Unit u : allUnits) {
				float yDistance = (u.getY() - y);
				float xDistance = (u.getX() - x);
				float distanceXY = (float)Math.sqrt(yDistance*yDistance + xDistance*xDistance);
				
				// If the unit is very small make it easier to press.
				if(distanceXY <= 50 + u.getRadius() && u.getName() != "Fortress" && u.getRadius() <= 50 && u.getShape() != "Plus") {
					return u;
				}
				// If the unit is big, don't make it get in the way of other things with a huge hitbox.
				if(distanceXY <= 10 + u.getRadius() && u.getName() != "Fortress" && u.getRadius() > 50 && u.getShape() != "Plus") {
					return u;
				}
			}
			
			// KILL THE PLUSSES!
			for(Unit u : allUnits) {
				float yDistance = (u.getY() - y);
				float xDistance = (u.getX() - x);
				float distanceXY = (float)Math.sqrt(yDistance*yDistance + xDistance*xDistance);
				
				// If the unit is very small make it easier to press.
				if(distanceXY <= 50 + u.getRadius() && u.getName() != "Fortress" && u.getRadius() <= 50) {
					return u;
				}
				// If the unit is big, don't make it get in the way of other things with a huge hitbox.
				if(distanceXY <= 10 + u.getRadius() && u.getName() != "Fortress" && u.getRadius() > 50) {
					return u;
				}
			}
			return null;
		}
	}
	
	// Get screen height and width.
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	// Add a new unit to the list of all units to be drawn in animation.
	public static void addUnit(Unit newUnit) {
		synchronized(allUnitsLock) {
			allUnits.add(newUnit);
		}
	}
	
	public static Unit getUnit(String nameToSearch) {
		synchronized(allUnitsLock) {
			Unit foundUnit = null;
			for(Unit u : allUnits) {
				if(u.getName() == nameToSearch) {
					foundUnit = u;
					break;
				}
			}
			return foundUnit;
		}
	}
	
	public static int getUnitPos(Unit thisUnit) {
		synchronized(allUnitsLock) {
			int foundUnit = 0;
			for(Unit u : allUnits) {
				if(u == thisUnit) {
					break;
				}
				foundUnit++;
			}
			return foundUnit;
		}
	}
	
	public static void killUnit(Unit u) {
		if(allUnits.size()!=0){
			synchronized(allUnitsLock) {
				int foundUnit = 0;
				for(Unit v : allUnits) {
					if(u == v) {
						break;
					}
					foundUnit++;
				}
				if(foundUnit < allUnits.size()) {
					allUnits.remove(foundUnit);
				}
			}
		}
	}
	
	void initGame() {
		UnitType.initUnitTypes();
		// Put the Castle in the middle.
		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
	    Unit u = new Unit("Fortress","Castle",screenWidth/2,screenHeight/2);
	    Wave.initWaves();
	}
	
	void playGame() {
		gameThread=new Thread(new Runnable() {
			public void run() {
				while(!gameOver){
					updateAllUnits();
					currentView.postInvalidate();
					try {
						Thread.sleep(10);
					}
					catch (Throwable t) {
					}
				}
			}
		});
		gameThread.start();
	}
	
	 void youLose() {
		 gameOver = true;
		 doOnce = true;
		 levelText = "Wave " + (Wave.getCurrentWaveNumber() + 1) + " defeated you.";
		 int currHighScore = prefs.getInt("highScoreSurvival", 0);
		 if((Wave.getCurrentWaveNumber() + 1) > currHighScore)  {
			 Editor editor = prefs.edit();
			 editor.putInt("highScoreSurvival", (Wave.getCurrentWaveNumber() + 1));
			 highScoreText = "New high score!";
			 previousHighScoreText = "Previous: Wave " + currHighScore + ".";
			 editor.commit();
		 }
		 else {
			 highScoreText = "Current high score: Wave " + currHighScore + ".";
			 previousHighScoreText = "";
		 }
		 allUnits.clear(); // Don't request the lock because the caller is already locking it.
		 Wave.destroyWaves();
	 }
	 
	public static int numUnits() {
		return allUnits.size();
	}
	
	void updateAllUnits() {
		// Unleash the waves.
		Wave.sendWaves();	
		synchronized(allUnitsLock) {
			// Where is the castle?
			Unit castle = getUnit("Fortress");
			castleHP = "Health " + castle.getHP();
			for(Unit u : allUnits) {
			    float castleY = 0;
			    float castleX = 0;
			    float castleRadius = 0;
			    if(castle!=null) {
			    	castleY = castle.getY();
			    	castleX = castle.getX();
			    	castleRadius = castle.getRadius();
			    }
				float yDistance = (castleY - u.getY());
				float xDistance = (castleX - u.getX());
				float distanceXY = (float)Math.sqrt(yDistance*yDistance + xDistance*xDistance);
				if(distanceXY <= castleRadius + u.getRadius() && u.getName() != "Fortress") {
					u.attacks(castle);
					u.die();
					castleHP = "Health " + castle.getHP();
					if(castle.isDead()){
						youLose();
						break;
					}
					break;
				}
				if(castle.isDead()){
					youLose();
					break;
				}
				else {
					u.moveUnit();
				}
			}
		}
	}

}

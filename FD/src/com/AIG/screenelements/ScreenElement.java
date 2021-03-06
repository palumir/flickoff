package com.AIG.screenelements;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.AIG.abilities.Ability;
import com.AIG.earthDefense.GameActivity;
import com.AIG.earthDefense.MainActivity;
import com.AIG.earthDefense.R;
import com.AIG.units.UnitType;

public class ScreenElement {
	// Static button stuff
	public static Bitmap pauseBMP = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), R.drawable.pause));
	public static Bitmap healthBMP = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), R.drawable.health));
	
	// Global stuff.
	public static ArrayList<ScreenElement> allScreenElements = new ArrayList<ScreenElement>(256);
	public static Object allScreenElementsLock = new Object();
	
	// ScreenElement General Information:
	private String activity = "Game";
	private String name;
	private String type;     // Chose not to simply store this as a ScreenElementType incase we want to modify individual
						     // fields to be unique values. In other words, we may want to change an Ogre to be
	// Position Information: // really big, but we don't want to have to add a new ScreenElementType every time we do that!
	private boolean onScreen = false;
	protected float x;     
	protected float y;     
	private float xNew;
	private float yNew;
	private int width;
	private int height;
	public float oldX;
	private ScreenElementAnimation s;

	// Combat information:
	private boolean killable = false;
	
	// Drawing information:
	private String shape;
	public int color;
	private Bitmap bmp;
	private int textsize;
	
	// ScreenElement Stats
	private int currentHitPoints;
	private int maxHitPoints;
	private int damage;
	
	// Store stuff
	private Ability attachedAbility = null;
	private UnitType attachedPlanet = null;
	
	public ScreenElement(
			String newType,
			String newText,
			float xSpawn,
			float ySpawn,
			String newActivity
			) {
		// Set it's coordinates.
		setName(newText);
		type = newType;
		x = xSpawn;
		y = ySpawn;
		setWidth(50);
		setHeight(50);
		xNew = xSpawn;
		yNew = ySpawn;
		activity = newActivity;
		color = Color.WHITE;
		textsize = 50;
		
		// Add it to the list of ScreenElements to be drawn.
		synchronized(allScreenElementsLock) {
			addScreenElement(this);
		}
	}
	
	public ScreenElement(
			String newType,
			String newText,
			float xSpawn,
			float ySpawn,
			float myWidth,
			float myHeight,
			String newActivity
			) {
		// Set it's coordinates.
		setName(newText);
		type = newType;
		x = xSpawn;
		y = ySpawn;
		setWidth((int)myWidth);
		setHeight((int)myHeight);
		xNew = xSpawn;
		yNew = ySpawn;
		activity = newActivity;
		color = Color.WHITE;
		textsize = 50;
		
		// Add it to the list of ScreenElements to be drawn.
		synchronized(allScreenElementsLock) {
			addScreenElement(this);
		}
	}

	public ScreenElement(String newName, String newType, float xSpawn, float ySpawn, int newWidth, int newHeight, Bitmap newBMP) {
		
		// Set it's coordinates.
		setName(newName);
		type = newType;
		x = xSpawn;
		y = ySpawn;
		xNew = xSpawn;
		yNew = ySpawn;
		setWidth(newBMP.getScaledWidth(MainActivity.metrics));
		setHeight(newBMP.getScaledHeight(MainActivity.metrics));
		bmp = newBMP;
		if(!newName.contains("AbilityIcon") && !newName.contains("UnitIcon")) bmp =  Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), MainActivity.matrix, true);
		
		// Add it to the list of ScreenElements to be drawn.
		synchronized(allScreenElementsLock) {
			addScreenElement(this);
		}
	}
	
	public ScreenElement(String newName, String newType, float xSpawn, float ySpawn, int newWidth, int newHeight, Bitmap newBMP, String newActivity) {
		
		// Set it's coordinates.
		activity = newActivity;
		setName(newName);
		type = newType;
		x = xSpawn;
		y = ySpawn;
		xNew = xSpawn;
		yNew = ySpawn;
		setWidth(newWidth);
		setHeight(newHeight);
		bmp = newBMP;
		if(!newName.contains("AbilityIcon") && !newName.contains("UnitIcon")) bmp =  Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), MainActivity.matrix, true);
		
		// Add it to the list of ScreenElements to be drawn.
		synchronized(allScreenElementsLock) {
			addScreenElement(this);
		}
	}
	
	public ScreenElement(String newName, String newType, float xSpawn, float ySpawn, int newWidth, int newHeight, Bitmap newBMP, String resize, String newActivity) {
		
		// Set it's coordinates.
		activity = newActivity;
		setName(newName);
		type = newType;
		x = xSpawn;
		y = ySpawn;
		xNew = xSpawn;
		yNew = ySpawn;
		setWidth(newWidth);
		setHeight(newHeight);
		bmp = newBMP;
		if(!newName.contains("AbilityIcon") && !newName.contains("UnitIcon")) bmp =  Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), MainActivity.matrix, true);
		
		// Add it to the list of ScreenElements to be drawn.
		synchronized(allScreenElementsLock) {
			addScreenElement(this);
		}
	}
	
	public void moveInstantly(float xGo, float yGo) {
		x = xGo;
		y = yGo;
		xNew = xGo;
		yNew = yGo;
	}

	
	// Add a new ScreenElement to the list of all ScreenElements to be drawn in animation.
		public static void addScreenElement(ScreenElement newScreenElement) {
			synchronized(allScreenElements) {
				allScreenElements.add(newScreenElement);
			}
		}
		
		public static ScreenElement getScreenElement(String nameToSearch) {
			synchronized(allScreenElementsLock) {
				ScreenElement foundScreenElement = null;
				for(int j = 0; j < allScreenElements.size(); j++) {
					ScreenElement u = allScreenElements.get(j);
					if(u.getName() == nameToSearch) {
						foundScreenElement = u;
						break;
					}
				}
				return foundScreenElement;
			}
		}
		
		
		public static int getScreenElementPos(ScreenElement thisScreenElement) {
			synchronized(allScreenElementsLock) {
				int foundScreenElement = 0;
				for(int j = 0; j < allScreenElements.size(); j++) {
					ScreenElement u = allScreenElements.get(j);
					if(u == thisScreenElement) {
						break;
					}
					foundScreenElement++;
				}
				return foundScreenElement;
			}
		}
		
		public static void killScreenElement(ScreenElement u) {
			if(allScreenElements.size()!=0){
				synchronized(allScreenElementsLock) {
					int foundScreenElement = 0;
					for(int j = 0; j < allScreenElements.size(); j++) {
						ScreenElement v = allScreenElements.get(j);
						if(u == v) {
							break;
						}
						foundScreenElement++;
					}
					if(foundScreenElement < allScreenElements.size()) {
						allScreenElements.remove(foundScreenElement);
					}
				}
			}
		}
		
		
		public void setOldX() {
			oldX = x;
		}
	
	// Get the selected ScreenElement at the coordinates.
	public static ScreenElement getScreenElementAt(float x, float y, String act) {
		synchronized(allScreenElementsLock) {
			
			// Get all the close ScreenElements.
			for(int j = 0; j < allScreenElements.size(); j++) {
				ScreenElement u = allScreenElements.get(j);
				if(u.getType().equals("Drawn myButton") &&
						x >= u.getX() - u.getWidth() - u.getHeight()/3 && 
						x <= u.getX() + u.getWidth() + u.getHeight()/3 &&
						y >= u.getY() - u.getHeight() - u.getHeight()/3 &&
						y <= u.getY() + u.getHeight() + u.getHeight()/3 &&
						u.getActivity().equals(act) && ((myButton)u).isClickable()) {
						return u;
				}
				else if(!u.getType().equals("Drawn myButton") && x > u.getX() - 50 && x < u.getX() + u.getWidth() + 50 && y > u.getY() - 50 && y < u.getY() + u.getHeight() + 50 && u.getActivity().equals(act)) {
					return u;
				}
			}
			return null;
		}
	}
	
	public void despawn() {
		// Kill the old unit.
		killScreenElement(this);
	}
	
	public static int numScreenElements() {
		return allScreenElements.size();
	}
	
	
	public static void destroyAllScreenElements() {
		if(allScreenElements!=null) {
			allScreenElements.clear();
		}
	}
	
	public static void destroyAllScreenElements(String activity) {
			synchronized(allScreenElementsLock) {
				for(int i = 0; i < allScreenElements.size(); i++) {
					ScreenElement s = allScreenElements.get(i);
					if(s.getActivity() != null && s.getActivity().equals(activity)) {
						allScreenElements.remove(i);
						i--;
					}
				}
			}
	}
	
	public void draw(Canvas canvas, Paint myPaint) {
		if(this.type == "Text") {
			myPaint.setStyle(Paint.Style.FILL);
			myPaint.setStrokeWidth(3);
			myPaint.setTextSize(this.textsize);
			myPaint.setColor(this.color);
			canvas.drawText(this.getName(), this.x - myPaint.measureText(this.getName())/2, this.y, myPaint);
		}
		else if(this.type == "Drawn Button") {
			this.draw(canvas,myPaint);
		}
		else if(this.type == "Slot1" || this.type == "Slot2" || this.type == "Slot3") {
			myPaint.setStyle(Paint.Style.FILL);
			myPaint.setStrokeWidth(3);
			myPaint.setTextSize(30);
			myPaint.setColor(this.color);
			canvas.drawText(this.getName(), this.x, this.y-5, myPaint);
			myPaint.setStrokeWidth(1);
			myPaint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(this.x, this.y, this.x+70, this.y+70, myPaint);
			if(this.getName() != null) {
				Ability theAbility = Ability.getAbility(this.getName());
				if(theAbility!=null) {
					canvas.drawBitmap(theAbility.getBMP(), this.getX()+3, this.getY()+3, null);
				}
			}
		}
		else if(this.type == "PlanetSlot") {
			myPaint.setStyle(Paint.Style.FILL);
			myPaint.setStrokeWidth(3);
			myPaint.setTextSize(30);
			myPaint.setColor(this.color);
			canvas.drawText(this.getName(), this.x, this.y-5, myPaint);
			myPaint.setStrokeWidth(1);
			myPaint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(this.x, this.y, this.x+105, this.y+105, myPaint);
			if(this.getName() != null) {
				UnitType thePlanet = UnitType.getUnitType(this.getName());
				if(thePlanet!=null) {
					canvas.drawBitmap(thePlanet.getBMP(), this.getX()+3, this.getY()+3, null);
				}
			}
		}
		else if(this.bmp != null) {
			canvas.drawBitmap(this.getBMP(), this.getX()-this.getWidth(), this.getY() - this.getHeight(), null);
		}
	}
	
	public static void drawScreenElements(Canvas canvas, Paint myPaint, String activity) {
        synchronized(allScreenElementsLock) {
			for(int j = 0; j < allScreenElements.size(); j++) {
				ScreenElement currentScreenElement = allScreenElements.get(j);
      	  		// What shape do we draw?
      	  		myPaint.setColor(currentScreenElement.color);
      	  		if(currentScreenElement.getActivity() == activity) {
      	  			currentScreenElement.draw(canvas, myPaint);
      	  		}
        	}
        }
	}
	
	public void unAnimate() {
		this.s = null;
	}
	
	public static void updateScreenElements() {
		FleetingScreenElement.updateFleetingScreenElements();
		synchronized(ScreenElement.allScreenElements) {
			for(int j = 0; j < allScreenElements.size(); j++) {
				ScreenElement u = allScreenElements.get(j);
				ScreenElementAnimation.animateScreenElement(u);
			}
		}
	}
	
	public void attachAbility(Ability a) {
		setAttachedAbility(a);
	}
	
	public void attachPlanet(UnitType u) {
		setAttachedPlanet(u);
	}
	
	public void respondToTouch() {
		if(this.getName() == "Pause") {
			if(!GameActivity.paused) {
				try {
					GameActivity.paused = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					GameActivity.paused = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void animate(String type, int duration) {
		this.s = new ScreenElementAnimation(this, type, duration);
	}
	
	// Methods to get values
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	// Stats
	public int getHP(){
		return currentHitPoints;
	}
	
	public int getCurrentHitPoitns(){
		return currentHitPoints;
	}
	
	public void setTextSize(int s) {
		textsize = s;
	}
	
	public void setColor(int c) {
		color = c;
	}
	
	public int getMaxHitPoints(){
		return maxHitPoints;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public boolean getKillable() {
		return killable;
	}
	
	public Bitmap getBMP() {
		return bmp;
	}
	
	public String getShape() {
		return shape;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getXNew() {
		return xNew;
	}
	
	public float getYNew() {
		return yNew;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public ScreenElementAnimation getS() {
		return s;
	}

	public void setS(ScreenElementAnimation s) {
		this.s = s;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ability getAttachedAbility() {
		return attachedAbility;
	}

	public void setAttachedAbility(Ability attachedAbility) {
		this.attachedAbility = attachedAbility;
	}

	public int getColor() {
		return color;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public UnitType getAttachedPlanet() {
		return attachedPlanet;
	}

	public void setAttachedPlanet(UnitType attachedPlanet) {
		this.attachedPlanet = attachedPlanet;
	}

}
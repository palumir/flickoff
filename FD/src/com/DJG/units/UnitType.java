package com.DJG.units;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.DJG.abilities.Ability;
import com.DJG.abilities.Coin;
import com.DJG.fd.GameActivity;
import com.DJG.fd.MainActivity;
import com.DJG.fd.R;
import com.DJG.planets.Planet;

public class UnitType {
	
	// Global list of all Unit Types.
	private final static Object allUnitTypesLock= new Object(); // A lock so we don't fuck up the allUnits
	private static ArrayList<UnitType> allUnitTypes;
	
	// Frozen BMP
	private Bitmap frozenBMP;
	
	public static void initUnitTypes() {
		
		allUnitTypes = new ArrayList<UnitType>();
		
		// Basic Units
		getAllUnitTypes().add(new UnitType("Asteroid",1f, true, R.drawable.asteroid, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("Fire Asteroid",1f,true, R.drawable.fire_asteroid, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("Ice Asteroid",1f,true, R.drawable.ice_asteroid, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("Cat",2f, true, R.drawable.satelite, 1, 10, "Unit"));
		getAllUnitTypes().add(new UnitType("Cat Gunner",2f, true, R.drawable.satelite_gunner, 1, 10, "Unit"));
		getAllUnitTypes().add(new UnitType("Splitter Big",1.25f,true,R.drawable.splitter_big, 1, 100,"Unit"));
		getAllUnitTypes().add(new UnitType("Splitter Medium",1f,true,R.drawable.splitter_medium, 1, 25,"Unit"));
		getAllUnitTypes().add(new UnitType("Splitter Small",0.25f,true,R.drawable.splitter_small, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("MultiClicker 1",1f, true, R.drawable.fasteroid, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("MultiClicker 2",1f, true, R.drawable.splitter_medium, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("MultiClicker 3",1f, true, R.drawable.fasteroid, 1, 10,"Unit"));
		getAllUnitTypes().add(new UnitType("MultiClicker 4",1f, true, R.drawable.splitter_medium, 1, 10,"Unit"));
		
		// Projectile
		getAllUnitTypes().add(new UnitType("Bullet",10f, true, R.drawable.bullet, R.drawable.bullet, 1, 0,"Projectile"));
		getAllUnitTypes().add(new UnitType("Bomb Bullet",10f, true, R.drawable.bomb_bullet, R.drawable.bomb_bullet, 1, 0,"Projectile"));
		getAllUnitTypes().add(new UnitType("Gunner Bullet",3f, true, R.drawable.red_orb, 1, 1,"Unit"));
	
		//Planets
		Planet.initPlanetTypes();
		
		//Defensive units, Will add own class later
		getAllUnitTypes().add(new UnitType("Fire Defender Moon", 0f,true, R.drawable.fire_moon, 1, 0,"Moon"));
		getAllUnitTypes().add(new UnitType("Mars Moon", 0f, true, R.drawable.moon, 2, 0,"Moon"));
	}
	
	public static UnitType getUnitType(String searchType) {
		UnitType foundUnitType = null;
		synchronized(allUnitTypesLock) {
  	  		for(int i = 0; i < getAllUnitTypes().size(); i++) {
  	  			UnitType u = getAllUnitTypes().get(i);
				if(u.getType().equals(searchType)) {
					foundUnitType = u;
					break;
				}
			}
			if(foundUnitType==null && !searchType.equals("Asteroid")) {
				return getUnitType("Asteroid");
				
			}
			return foundUnitType;
		}
	}
	
	// UnitType constructor.
	public UnitType(String newType, int newRadius, float newMoveSpeed, boolean isKillable) {
		type = newType;
		radius = newRadius;
		moveSpeed = newMoveSpeed;
		killable = isKillable;
		color = Color.WHITE;
	}
	
	// Store unitType constructor;
	public UnitType(String newType, int newRadius, float newMoveSpeed, boolean isKillable, int newBitMapLink, int newHP, int newDamage, String newMetaType, int newCost) {
		type = newType;
		radius = newRadius;
		moveSpeed = newMoveSpeed;
		metaType = "Unit";
		killable = isKillable;
		bitmap = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newBitMapLink));
		maxHitPoints = newHP;
		damage = newDamage;
		metaType = newMetaType;
		setCost(newCost);
		frozenBMP = bitmap;
	}

	public UnitType(String newType, float newMoveSpeed, boolean isKillable, int newBitMapLink, int newHP, int newDamage, String myMetaType) {
		type = newType;
		moveSpeed = newMoveSpeed;
		metaType = "Unit";
		killable = isKillable;
		metaType = myMetaType;
		bitmap = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newBitMapLink));
		radius = bitmap.getScaledWidth(MainActivity.metrics)/2;
		Log.d(type,""+radius);
		maxHitPoints = newHP;
		damage = newDamage;
		frozenBMP = bitmap;
	}
	
	public UnitType(String newType, float newMoveSpeed, boolean isKillable, int newBitMapLink, int myFrozen, int newHP, int newDamage, String myMetaType) {
		type = newType;
		moveSpeed = newMoveSpeed;
		metaType = "Unit";
		killable = isKillable;
		metaType = myMetaType;
		bitmap = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newBitMapLink));
		radius = bitmap.getScaledWidth(MainActivity.metrics)/2;
		maxHitPoints = newHP;
		damage = newDamage;
		frozenBMP = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), myFrozen));
	}
	
	public UnitType(String newType, int newRadius, float newMoveSpeed, boolean isKillable, int newBitMapLink, int newFrozenBMPLink, int newHP, int newDamage, String newMetaType) {
		type = newType;
		radius = newRadius;
		moveSpeed = newMoveSpeed;
		metaType = newMetaType;
		killable = isKillable;
		bitmap = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newBitMapLink));
		setFrozenBMP(GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newFrozenBMPLink)));
		maxHitPoints = newHP;
		damage = newDamage;
	}
	
	public UnitType(String newType, int newRadius, float newMoveSpeed, boolean isKillable, int newBitMapLink, int newHP, int newDamage, String newMetaType) {
		type = newType;
		radius = newRadius;
		moveSpeed = newMoveSpeed;
		metaType = newMetaType;
		killable = isKillable;
		bitmap = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), newBitMapLink));
		setFrozenBMP(bitmap);
		maxHitPoints = newHP;
		damage = newDamage;
	}
	
	public UnitType(String newType, String newMetaType, int newRadius, float newMoveSpeed, boolean isKillable, Bitmap newBitMap, int newHP, int newDamage) {
		setFrozenBMP(bitmap);
		type = newType;
		radius = newRadius;
		moveSpeed = newMoveSpeed;
		metaType = newMetaType;
		killable = isKillable;
		bitmap = newBitMap;
		maxHitPoints = newHP;
		damage = newDamage;
		frozenBMP = bitmap;
	}
	
	public UnitType(String newType, int newRadius, float newMoveSpeed, boolean isKillable, int newColor, String newShape, int newHP, int newDamage) {
		type = newType;
		radius = newRadius;
		metaType = "Unit";
		moveSpeed = newMoveSpeed;
		shape = newShape;
		killable = isKillable;
		color = newColor;
		maxHitPoints = newHP;
		damage = newDamage;
		frozenBMP = bitmap;
	}

	// Unit Type fields. WIP: Seperate into sections when there's lots of values.
	private String type;
	private int radius;
	private boolean killable;
	
	//UI stuff
	private int color;
	private String metaType;
	private String shape;
	private Bitmap bitmap;
	
	//Unit stats
	private int maxHitPoints;
	private int damage;
	private float moveSpeed;
	
	// Store stuff
	private int cost;
	private String description = "None";
	
	public static void addUnitType(UnitType u) {
		getAllUnitTypes().add(u);
	}
	
	public boolean getKillable() {
		return killable;
	}
	
	public static ArrayList<UnitType> getAllOf(String s) {
		synchronized(allUnitTypesLock) {
			ArrayList a = new ArrayList<UnitType>();
  	  		for(int i = 0; i < getAllUnitTypes().size(); i++) {
  	  			UnitType u = getAllUnitTypes().get(i);
				if(u.getMetaType().equals(s)) {
					a.add(u);
				}
			}
			return a;
		}
	}
	
	public void equip() {
		if(Ability.getPrefs().getInt(getType() + "_purchased", -99) == 1) {
			Ability.getEditor().putString("currentPlanet", getType());
			Ability.getEditor().commit();
		}
		else {
			// You do not have that ability purchased.
		}
	}
	
	public void buy() {
		if(Ability.getPrefs().getInt(getType() + "_purchased", -99) == -99 && Coin.getCoins() >= getCost()) {
			Ability.getEditor().putInt(getType() + "_purchased",1);
			Coin.increaseCoins((-1)*getCost());
			Coin.coinsSave();
			Ability.getEditor().commit();
		}
		else if(!(Ability.getPrefs().getInt(getType() + "_purchased", -99) == -99)) {
			// You already purchased that!
		}
		else if(!(Coin.getCoins() >= getCost())) {
			// You don't have enough coins for that!
		}
	}
	
	public String getType() {
		return type;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getColor(){
		return color;
	}
 
	public int getMaxHitPoints(){
		return maxHitPoints;
	}
	
	public String getMetaType() {
		return metaType;
	}
	
	public Bitmap getBMP() {
		return bitmap;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public String getShape() {
		return shape;
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}

	public static ArrayList<UnitType> getAllUnitTypes() {
		return allUnitTypes;
	}

	public static void setAllUnitTypes(ArrayList<UnitType> allUnitTypes) {
		UnitType.allUnitTypes = allUnitTypes;
	}

	public Bitmap getFrozenBMP() {
		return frozenBMP;
	}

	public void setFrozenBMP(Bitmap frozenBMP) {
		this.frozenBMP = frozenBMP;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
package com.AIG.abilities;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.AIG.earthDefense.GameActivity;
import com.AIG.earthDefense.R;

public class Coin {
	
	private static int coins = -1;
	
	// Static information.
	private static ArrayList<Coin> allCoins = new ArrayList<Coin>();
	public final static Object CoinsLock = new Object(); // A lock so we don't fuck up the Coins
	
	// General ability attributes. Coins are static at the moment.
	private long startTime;
	private int duration = 3000;
	
	// Bitmap
	public static Bitmap CoinBMP = GameActivity.makeTransparent(BitmapFactory.decodeResource(GameActivity.gameContext.getResources(), R.drawable.coin));

	// Well, where is the ability?!
	private float x;
	private float y;
	private int radius;
	private int blastRadius;
	private int stroke;
	private int maxStroke;
	private int color;
	
	public Coin(float newX, float newY, int newBlastRadius, int newDuration) {
		x = newX;
		y = newY;
		color = Color.CYAN;
		maxStroke = 30;
		blastRadius = newBlastRadius;
		duration = newDuration;
		startTime = GameActivity.getGameTime();
		synchronized(allCoins) {
			addCoin(this);
		}
	}
	
	public static int getCoins() {
		int defaultCoins = 400;
		if(coins==-1) {
			 coins = Ability.getPrefs().getInt("flickOff_numCoins", defaultCoins);
		}
		return coins;
	}
	
	public static void increaseCoins() {
		Ability.getEditor().putInt("flickOff_numCoins",getCoins() + 1);
		coins++;
	}
	
	public static void increaseCoins(int n) {
		Ability.getEditor().putInt("flickOff_numCoins",getCoins() + n);
		coins = coins+n;
	}
	
	public static void coinsSave() {
		if(Ability.getEditor()!=null) {
			Ability.getEditor().commit();
		}
	}
	
	public void updateCoin(int CoinPos) {
		synchronized(CoinsLock) {
		if(this != null) {
			long currentTime = GameActivity.getGameTime();
			if((int)(currentTime - startTime) > duration) {
					removeCoin(CoinPos);
			}
		 }
		}
	}
	
	public static void updateCoins() {
		GameActivity.coinsText = Integer.toString(getCoins());
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public static void addCoin(Coin b) {
		synchronized(CoinsLock) {
			allCoins.add(b);
		}
	}
	
	public static void removeCoin(int pos) {
		synchronized(CoinsLock) {
			allCoins.remove(pos);
		}
	}
	
	public static void clearCoins() {
		synchronized(CoinsLock) {
			if(allCoins!=null) {
				allCoins.clear();
			}
		}
	}
	
	public static void drawCoins(Canvas canvas, Paint myPaint) {
        // Draw Coins.
        synchronized(Coin.CoinsLock) {
	        myPaint.setStyle(Paint.Style.STROKE);
			for(int i = 0; i < Coin.getAllCoins().size(); i++) {
				Coin s = Coin.getAllCoins().get(i);
    		myPaint.setColor(s.getColor());
	        	myPaint.setStrokeWidth(s.getStroke());
    		canvas.drawCircle(s.getX(),s.getY(),s.getRadius(), myPaint);
    	  }
    	}
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public float getX() {
		return x;
	}
	
	public int getColor() {
		return color;
	}

	public float getY() {
		return y;
	}
	
	public int getBlastRadius() {
		return blastRadius;
	}
	
	public static void clearCoin() {
		synchronized(CoinsLock) {
			allCoins.clear();
		}
	}
	
	public int getStroke() {
		return stroke;
	}
	
	public static ArrayList<Coin> getAllCoins() {
		return allCoins;
	}
}
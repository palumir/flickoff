package com.AIG.waves;
import java.util.ArrayList;
import java.util.Random;

import android.content.SharedPreferences.Editor;

import com.AIG.earthDefense.GameActivity;
import com.AIG.earthDefense.MainActivity;
import com.AIG.generators.WaveGenerator;
import com.AIG.units.Unit;

class XY {
	public int x;
	public int y;
	
	public XY(int xNew, int yNew) {
		y = yNew;
		x = xNew;
	}
}

class UnitPattern {
	public int numUnits;
	public String pattern;
	public UnitPattern(int num, String unitPattern) {
		numUnits = num;
		pattern = unitPattern;
	}
}

public class Wave extends ArrayList<Unit> {
	public static ArrayList<Integer> usedWaves = new ArrayList<Integer>();
	public static WaveGenerator waveGenerator;
	private static boolean isBossWave = false;
	private static Wave currentWave;
	private static boolean isFirst;
	private static long waveEndedTime;
	public final static Object currentWaveLock = new Object(); // A lock so we don't fuck up the currentWave.
	private static double currentWaveNumber;
	private static boolean waveSent = false;
	private static Random r = new Random();
	private static int waveWaitTime = 0;
	private static double speedFactor = 1;
	
	public static void initWaves() {
		// Obviously we just started the game.
		waveSent = false;
		isFirst = true;
		waveGenerator = new WaveGenerator();
		// Start at what wave?
		currentWaveNumber = GameActivity.levelStart;
		sendWave(currentWaveNumber);
	}
	
	public static double getSpeedFactor() {
		return speedFactor;
	}
	
	public static double getWaveNumber() {
		return currentWaveNumber;
	}
	
	public static void setWave(int n) {
		currentWaveNumber = n;
	}

	public static void setCurrentWave(Wave w) {
		currentWave = w;
	}
	
	public static void setWaitTime(int n) {
		setWaveWaitTime(n);
	}
	
	public static void initWaves(int waveStartNumber){
		// Obviously we just started the game.
		waveSent = false;
		isFirst = true;
		waveGenerator = new WaveGenerator();
		// Start at what wave?
		currentWaveNumber = GameActivity.levelStart;
		sendWave(currentWaveNumber);
	}
	
	// If the int has already used, don't use it again. If every int between
	// 0 and top have been used, reset usedWaves, and use the input as num.
	static Integer getMyRandom(Integer num, Integer top) {
		Integer x = num;
		Integer n = 0;
		if(usedWaves == null) {
			usedWaves.add(num);
			return num;
		}
		while(usedWaves.contains(x)) {
			if(x<0) {
				x = top;
			}
			if(n>top+1) {
				usedWaves.clear();
				usedWaves.add(num);
				return num;
			}
			x--;
			n++;
		}
		usedWaves.add(x);
		return x;
	}
	
	static int cap(int num, int cap) {
		if(num > cap) {
			return cap;
		}
		return num;
	}
	
	static String fireorice() {
		int between = getR().nextInt(20); 
		String whatToSend = "Asteroid";
		if(between == 1) {
			whatToSend = "Fire Asteroid";
		}
		if(between == 2) {
			whatToSend = "Ice Asteroid";
		}
		return whatToSend;
	}
	
	static void sendWave(double waveNumber){
		if ((Wave.getCurrentWaveNumber() + 1) > MainActivity.getHighScore(GameActivity.getMode())) {
			Editor editor = GameActivity.prefs.edit();
			editor.putInt(GameActivity.getMode() + "highScore",
					(int)(Wave.getCurrentWaveNumber() + 1));
			editor.commit();
		} 
			Survival.sendSurvivalWave(waveNumber);
	}
	
	public boolean getWaveSent() {
		return waveSent;
	}
	
	public static void addToCurrentWave(Unit u) {
		synchronized(currentWaveLock) {
			currentWave.add(u);
		}
	}
	
	public static Wave getCurrentWave() {
		return currentWave;
	}
	
	public static void sendWaves() {
		
		synchronized(currentWaveLock) {
			
			// Record when it first becomes empty.
			if(currentWave.isEmpty() && isFirst) {
				waveEndedTime = GameActivity.getGameTime();
				isFirst = false;

			}
			
			// Send the next wave if the current one has gotten "close" to earth.
			if(GameActivity.getGameTime() > WaveGenerator.maxTimeOnWave) {
				// Ramp up quickly
				if(currentWaveNumber < 3) {
					currentWaveNumber++;
				}
				else if(currentWaveNumber == 3) {
					currentWaveNumber = 6;
				}
				else if(currentWaveNumber == 6) {
					currentWaveNumber = 8;
				}
				else {
					currentWaveNumber++;
				}
				speedFactor = (getCurrentWaveNumber()/10 + 1);
				sendWave(currentWaveNumber);
				waveSent = false;
				isFirst = true;
				
			}
			
			if(!isBossWave()) {
				// Tell the wave to attack the castle.
				if(currentWave.getWaveSent() == false) {
					currentWave.attackCastle();
					waveSent = true; // Efficiency.
				}
			}
		
		}
	}

	public static void currentWaveAttackCastle() {
			currentWave.attackCastle();
	}
	
	private void attackCastle() {
		// Get the height, width, and a new random number generator.
		int screenWidth = GameActivity.getScreenWidth();
		int screenHeight = GameActivity.getScreenHeight();
		synchronized(currentWaveLock) {
  	  		for(int i = 0; i < this.size(); i++) {
  	  			Unit u = this.get(i);
				u.moveNormally(screenWidth/2,screenHeight/2);
			}
		}
	}
	
	public static void destroyWaves() {
	currentWaveNumber = 0;
	 synchronized(Wave.currentWaveLock) {
		 if(Wave.getCurrentWave() != null) {
			 Wave.getCurrentWave().clear();
		 }
	 }
	}
	
	public static int getUnitPos(Unit thisUnit) {
		int foundUnit = 0;
		synchronized(currentWaveLock) {
  	  		for(int i = 0; i < currentWave.size(); i++) {
  	  			Unit u = currentWave.get(i);
				if(u == thisUnit) {
					break;
				}
				foundUnit++;
			}
			return foundUnit;
		}
	}
	
	public static double getCurrentWaveNumber() {
		return currentWaveNumber;
	}
	
	public int getWaitTime(){
		return getWaveWaitTime();
	}
	
	public static void killUnit(Unit u) {
			synchronized(currentWaveLock) {
				int foundUnit = 0;
	  	  		for(int i = 0; i < currentWave.size(); i++) {
	  	  			Unit v = currentWave.get(i);
						if(v == u) {
							break;
						}
						foundUnit++;
					}
					if(currentWave.size() != 0 && foundUnit < currentWave.size()) {
						currentWave.remove(foundUnit);
					}
				}
	}

	public static int getWaveWaitTime() {
		return waveWaitTime;
	}

	public static void setWaveWaitTime(int waveWaitTime) {
		Wave.waveWaitTime = waveWaitTime;
	}

	public static boolean isBossWave() {
		return isBossWave;
	}

	public static void setBossWave(boolean isBossWave) {
		Wave.isBossWave = isBossWave;
	}

	public static Random getR() {
		return r;
	}

	public static void setR(Random r) {
		Wave.r = r;
	}
}
package com.DJG.screenelements;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.DJG.fd.GameActivity;

public class Background {
	// Background
	static Bitmap background;
	static Canvas bgCanvas;
	static int backX = 0;
	
	public static void drawBackground(Canvas canvas, Paint myPaint) {
		canvas.drawColor(GameActivity.bgColor);
		if (bgCanvas == null) {
			background = Bitmap.createBitmap(GameActivity.getScreenWidth(),
					GameActivity.getScreenHeight(), Bitmap.Config.ARGB_8888);
			bgCanvas = new Canvas(background);
			myPaint.setStrokeWidth(1);
			myPaint.setColor(Color.WHITE);
			int x = 0;
			while (x < GameActivity.getScreenWidth()) {
				int y = 0;
				int n = 0;
				while (y < GameActivity.getScreenHeight()) {
					if (GameActivity.getR().nextInt(GameActivity.getScreenHeight()) == 0) {
						n++;
						bgCanvas.drawPoint(x, y, myPaint);
					}
					if (n > 10) {
						break;
					}
					y++;
				}
				x++;
			}
		}
		backX++;
		if(backX == GameActivity.getScreenWidth()) {
			backX = 0;
		}
		canvas.drawBitmap(background, backX - GameActivity.getScreenWidth(),0,myPaint);
		canvas.drawBitmap(background, backX, 0, myPaint);
	}
}


package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

import java.util.List;

public class BombUpgrade extends UpgradeObject {

	/**
	 * the Sprite of the Object
	 */
	public static Bitmap sprite;

	public BombUpgrade(){
		super();
	}

	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();
		collisionBoxes = List.of(new Box(0f, 1f, 0f, 1f));

	}
}

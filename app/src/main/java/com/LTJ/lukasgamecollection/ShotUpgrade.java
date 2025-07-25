package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

public class ShotUpgrade extends UpgradeObject{
	/**
	 * the Sprite of the Object
	 */
	public static Bitmap sprite;

	public ShotUpgrade(){
		super();
	}
	
	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

	}
}

package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

public class BossShotObject extends SimpleGameObject {
	
	/**
	 * the Sprite of the Object
	 */
	public static Bitmap sprite;
	
	/**
	 * sets height and width of the object
	 */
	public void setDimensions() {
		height = sprite.getHeight();
		width = sprite.getWidth();

	}

}

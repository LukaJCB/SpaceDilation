package com.LTJ.lukasgamecollection;

import android.graphics.Bitmap;

import java.util.List;

public class LifeUpgrade extends UpgradeObject {
	
	private Bitmap sprite;
	
	public Bitmap getSprite() {
		return sprite;
	}

	public void setSprite(Bitmap sprite) {
		this.sprite = sprite;
	}

	public LifeUpgrade() {
		destroyed = true;
	}
	
	public void setDimensions(){
		height = sprite.getHeight();
		width = sprite.getWidth();
		collisionBoxes = List.of(new Box(0f, 1f, 0f, 1f));
	}

}

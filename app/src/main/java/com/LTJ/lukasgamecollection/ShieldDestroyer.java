package com.LTJ.lukasgamecollection;

import java.util.TimerTask;

public class ShieldDestroyer extends TimerTask {

	@Override
	public void run() {
		com.LTJ.lukasgamecollection.SpaceSurface.shield.systemDestroy();

	}

}

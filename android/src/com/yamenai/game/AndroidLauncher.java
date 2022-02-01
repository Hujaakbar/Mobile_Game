package com.yamenai.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yamenai.game.YameNai;
import com.yamenai.game.cordinator.Cordinator;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();


		//to conserve battery and disable the accelerometer and compass
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new Cordinator(), config);
	}
}

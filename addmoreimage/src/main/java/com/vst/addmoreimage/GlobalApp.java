package com.vst.addmoreimage;

import android.app.Application;

public class GlobalApp extends Application{
	private static GlobalApp instance;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	public static GlobalApp getInstance(){
		return instance;
	}
}

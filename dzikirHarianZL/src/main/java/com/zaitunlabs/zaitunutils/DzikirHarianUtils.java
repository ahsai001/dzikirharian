package com.zaitunlabs.zaitunutils;

public class DzikirHarianUtils {

	private final static int DEBUG = 0;
	private final static int RELEASE = 1;
	private static int liveMode = RELEASE;

	public static void setLiveMode(int liveMode){
		DzikirHarianUtils.liveMode = liveMode;
	}
	public static void setDebugMode(){
		DzikirHarianUtils.liveMode = DEBUG;
	}
	public static void setReleaseMode(){
		DzikirHarianUtils.liveMode = RELEASE;
	}

	
	
}

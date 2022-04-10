package com.zaitunlabs.dzikirharian.model;

public class DzikirModel {
	private String bacaan;
	private String counter;
	private String terjemah;
	private String dalil;
	private String sound;
	private String soundCount;
	private String runningtext;

	public String getBacaan() {
		return bacaan;
	}
	public void setBacaan(String bacaan) {
		this.bacaan = bacaan;
	}
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public String getTerjemah() {
		return terjemah;
	}
	public void setTerjemah(String terjemah) {
		this.terjemah = terjemah;
	}
	public String getDalil() {
		return dalil;
	}
	public void setDalil(String dalil) {
		this.dalil = dalil;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public String getRunningtext() {
		return runningtext;
	}
	public void setRunningtext(String runningtext) {
		this.runningtext = runningtext;
	}

	public String getSoundCount() {
		return soundCount;
	}

	public void setSoundCount(String soundCount) {
		this.soundCount = soundCount;
	}
}
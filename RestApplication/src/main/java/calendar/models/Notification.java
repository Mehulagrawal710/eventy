package calendar.models;

public class Notification {

	private boolean isNotifActive;
	private int timeBeforeToNotify;
	private String notifMessage;

	public Notification(boolean isNotifActive, int timeBeforeToNotify, String notifMessage) {
		this.isNotifActive = isNotifActive;
		this.timeBeforeToNotify = timeBeforeToNotify;
		this.notifMessage = notifMessage;
	}

	public boolean isNotifActive() {
		return isNotifActive;
	}

	public void setNotifActive(boolean isNotifActive) {
		this.isNotifActive = isNotifActive;
	}

	public int getTimeBeforeToNotify() {
		return timeBeforeToNotify;
	}

	public void setTimeBeforeToNotify(int timeBeforeToNotify) {
		this.timeBeforeToNotify = timeBeforeToNotify;
	}

	public String getNotifMessage() {
		return notifMessage;
	}

	public void setNotifMessage(String notifMessage) {
		this.notifMessage = notifMessage;
	}

	@Override
	public String toString() {
		return "Notification [isNotifActive=" + isNotifActive + ", timeBeforeToNotify=" + timeBeforeToNotify
				+ ", notifMessage=" + notifMessage + "]";
	}

}

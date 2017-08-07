package com.sap.codeinsights;

//TODO replace all int status codes with enums
public class Status {
    private int statusCode;
    private String currentStatus;
    private String statusLog;

    public Status(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
        statusLog += currentStatus + "\n";
    }

	public void pushUpdate(Update update) {
		this.setCurrentStatus(update.update);
		this.setStatusCode(update.code);
	}

    public String getStatusLog() {
        return statusLog;
    }

}

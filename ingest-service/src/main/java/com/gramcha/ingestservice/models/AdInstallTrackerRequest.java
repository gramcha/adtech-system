/**
 * @author gramcha
 * 07-Jul-2018 2:43:25 PM
 * 
 */
package com.gramcha.ingestservice.models;

import java.util.Date;

public class AdInstallTrackerRequest {
	private String installId;
	private String clickId;
	private Date time;
	
	public String getInstallId() {
		return installId;
	}
	public void setInstallId(String installId) {
		this.installId = installId;
	}
	public String getClickId() {
		return clickId;
	}
	public void setClickId(String clickId) {
		this.clickId = clickId;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}

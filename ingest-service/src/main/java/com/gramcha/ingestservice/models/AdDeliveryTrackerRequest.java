/**
 * @author gramcha
 * 07-Jul-2018 2:32:35 PM
 * 
 */
package com.gramcha.ingestservice.models;

import java.util.Date;

public class AdDeliveryTrackerRequest {
	private String advertisementId;
	private String deliveryId;
	private Date time;
	private String browser;
	private String os;
	private String site;

	public String getAdvertisementId() {
		return advertisementId;
	}

	public void setAdvertisementId(String advertisementId) {
		this.advertisementId = advertisementId;
	}

	public String getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}

/**
 * @author gramcha
 * 07-Jul-2018 4:41:32 PM
 * 
 */
package com.gramcha.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class ClickTracker {
	@Id
	private String id;
	private long advertisementId;
	private String deliveryId;
	private String browser;
	private String os;
	private String site;
	private String clickId;
	private Date time;
	public ClickTracker() {}
	public ClickTracker(long advertisementId, String deliveryId, String browser, String os, String site, String clickId,
			Date time) {
		super();
		this.advertisementId = advertisementId;
		this.deliveryId = deliveryId;
		this.browser = browser;
		this.os = os;
		this.site = site;
		this.clickId = clickId;
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
	public long getAdvertisementId() {
		return advertisementId;
	}

	public void setAdvertisementId(long advertisementId) {
		this.advertisementId = advertisementId;
	}

	public String getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
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

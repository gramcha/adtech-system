/**
 * @author gramcha
 * 09-Jul-2018 9:21:53 PM
 * 
 */
package com.gramcha.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "delivery")
public class DeliveryTracker {
	@Id
	private String id;
	private long advertisementId;
	private String deliveryId;
	private Date time;
	private String browser;
	private String os;
	private String site;

	public DeliveryTracker() {}
	public DeliveryTracker(long advertisementId, String deliveryId, Date time, String browser, String os, String site) {
		super();
		this.advertisementId = advertisementId;
		this.deliveryId = deliveryId;
		this.time = time;
		this.browser = browser;
		this.os = os;
		this.site = site;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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

/**
 * @author gramcha
 * 07-Jul-2018 2:40:10 PM
 * 
 */
package com.gramcha.ingestservice.models;

import java.util.Date;

public class AdClickTrackerRequest {
	private String deliveryId;
	private String clickId;
	private Date time;
	
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

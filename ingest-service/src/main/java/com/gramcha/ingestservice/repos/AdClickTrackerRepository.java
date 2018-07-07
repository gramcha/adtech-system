/**
 * @author gramcha
 * 07-Jul-2018 4:40:20 PM
 * 
 */
package com.gramcha.ingestservice.repos;

import com.gramcha.ingestservice.entities.ClickTracker;

public interface AdClickTrackerRepository {
	void save(ClickTracker clickTracker);
	ClickTracker findByClickId(String clickId);
}

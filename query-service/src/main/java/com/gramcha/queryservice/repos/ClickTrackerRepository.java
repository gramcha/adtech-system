/**
 * @author gramcha
 * 09-Jul-2018 9:57:57 PM
 * 
 */
package com.gramcha.queryservice.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gramcha.entities.ClickTracker;

@Repository
public interface ClickTrackerRepository extends MongoRepository<ClickTracker, String> {
	List<ClickTracker> findByTimeBetween(Date start, Date end);
}

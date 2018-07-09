/**
 * @author gramcha
 * 09-Jul-2018 9:57:57 PM
 * 
 */
package com.gramcha.storeservice.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gramcha.entities.InstallTracker;

@Repository
public interface InstallTrackerRepository extends MongoRepository<InstallTracker, String> {
	Optional<InstallTracker> findById(String id);
}

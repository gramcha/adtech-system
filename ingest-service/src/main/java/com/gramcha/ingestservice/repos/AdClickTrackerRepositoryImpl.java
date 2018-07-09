/**
 * @author gramcha
 * 07-Jul-2018 5:37:51 PM
 * 
 */
package com.gramcha.ingestservice.repos;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.gramcha.entities.ClickTracker;
@Repository
public class AdClickTrackerRepositoryImpl implements AdClickTrackerRepository {
	private static final String KEY = "Click";

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, ClickTracker> hashOperations;

	@Autowired
	public AdClickTrackerRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(ClickTracker clickTracker) {
		hashOperations.put(KEY, clickTracker.getClickId(), clickTracker);
	}

	@Override
	public ClickTracker findByClickId(String clickId) {
		return hashOperations.get(KEY, clickId);
	}
	
	@Override
	public void delete(final String clickId) {
        hashOperations.delete(KEY, clickId);
    }

}

package com.example.url_shortener.scheduler;

import com.example.url_shortener.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class UrlCleanupTask {

    private final UrlRepository urlRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UrlCleanupTask.class);

    public UrlCleanupTask(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    /**
     * Scheduled deletes expired URLs from the database. Runs every day at midnight.
     * CacheEvict ensures that when a link is deleted or expires, it is also removed from Redis instantly.
     * Cron format: "sec min hour day month weekday"
     */
    @Scheduled(cron = "0 0 0 * * *")
    // @Scheduled(fixedRate = 60000 )
    @CacheEvict(value = "urls", allEntries = true)
    @Transactional
    public void purgeExpiredLinks() {
        int deletedCount = urlRepository.deleteByExpiryDateBefore(LocalDateTime.now());
        LOG.info("Deleted {} expired records and cleared cache.", deletedCount);
    }
}

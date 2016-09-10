package com.nklmish.foodtrucks.scheduler;

import com.nklmish.foodtrucks.integration.FoodTruckFetcher;
import com.nklmish.foodtrucks.repository.FoodTruckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class FoodTruckScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(FoodTruckScheduler.class);
    private FoodTruckFetcher lookupService;
    private FoodTruckRepository repository;
    private boolean isEnabled;

    @Autowired
    public FoodTruckScheduler(FoodTruckFetcher lookupService,
                              FoodTruckRepository repository,
                              @Value("${scheduler.foodtruck.data-downloader.enabled}") boolean isEnabled) {
        this.lookupService = lookupService;
        this.repository = repository;
        this.isEnabled = isEnabled;
    }

    @Scheduled(cron = "${scheduler.foodtruck.data-downloader.cron.expression}")
    public void updateDb() {
        if (isEnabled) {
            LOG.trace("updating db... ");
            CompletableFuture
                    .supplyAsync(() -> lookupService.fetch())
                    .thenAccept(repository::save)
                    .exceptionally(ex -> {
                        LOG.error("error updating database", ex);
                        return null;
                    });
            LOG.trace("db updated");
        }
    }

}

package io.github.leases.internal.batch;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contains general configurations for batch related entities and objects
 */
@Configuration
@EnableBatchProcessing
public class BatchConfigs {

    @Bean("persistenceJobListener")
    @JobScope
    public JobExecutionListener persistenceJobListener(@Value("#{jobParameters['fileId']}") long fileId, @Value("#{jobParameters['startUpTime']}") long startUpTime) {
        return new PersistenceJobListener(fileId, startUpTime);
    }
}

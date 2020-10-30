package io.github.leases.internal.batch.leaseDetails;

import com.google.common.collect.ImmutableList;
import io.github.leases.config.FileUploadsProperties;
import io.github.leases.internal.Mapping;
import io.github.leases.internal.excel.ExcelFileDeserializer;
import io.github.leases.internal.model.LeaseDetailsEVM;
import io.github.leases.internal.service.BatchService;
import io.github.leases.service.LeasesFileUploadService;
import io.github.leases.service.dto.LeaseDetailsDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LeaseDetailsBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<LeaseDetailsEVM> deserializer;

    @Autowired
    private LeasesFileUploadService fileUploadService;

    @Autowired
    private FileUploadsProperties fileUploadsProperties;

    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @Value("#{jobParameters['startUpTime']}")
    private static long startUpTime;

    @Autowired
    private JobExecutionListener persistenceJobListener;

    @Autowired
    private BatchService<LeaseDetailsDTO> LeaseDetailsDTOBatchService;

    @Autowired
    private Mapping<LeaseDetailsEVM, LeaseDetailsDTO> mapping;


    @Bean("leaseDetailsPersistenceJob")
    public Job leaseDetailsPersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("leaseDetailsPersistenceJob")
            .preventRestart()
            .listener(persistenceJobListener)
            .incrementer(new RunIdIncrementer())
            .flow(readLeaseDetailsListFromFile())
            .end()
            .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<LeaseDetailsDTO>> leaseDetailsItemWriter() {

        return items -> items.stream().peek(LeaseDetailsDTOBatchService::save).forEach(LeaseDetailsDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<LeaseDetailsEVM>, List<LeaseDetailsDTO>> leaseDetailsItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean("readLeaseDetailsListFromFile")
    public Step readLeaseDetailsListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readLeaseDetailsListFromFile")
            .<List<LeaseDetailsEVM>, List<LeaseDetailsDTO>>chunk(2)
            .reader(leaseDetailsItemReader(fileId))
            .processor(leaseDetailsItemProcessor())
            .writer(leaseDetailsItemWriter())
            .build();
        // @formatter:off
    }

    @Bean
    @JobScope
    public ItemReader<List<LeaseDetailsEVM>> leaseDetailsItemReader(@Value("#{jobParameters['fileId']}") long fileId) {

        return new LeaseDetailsListItemReader(deserializer, fileUploadService, fileId, fileUploadsProperties);
    }
}

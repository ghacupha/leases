package io.github.leases.internal.batch.contractualLeaseRental;

import com.google.common.collect.ImmutableList;
import io.github.leases.config.FileUploadsProperties;
import io.github.leases.internal.Mapping;
import io.github.leases.internal.excel.ExcelFileDeserializer;
import io.github.leases.internal.model.ContractualLeaseRentalEVM;
import io.github.leases.internal.service.BatchService;
import io.github.leases.service.LeasesFileUploadService;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
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
public class ContractualLeaseRentalBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<ContractualLeaseRentalEVM> deserializer;

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
    private BatchService<ContractualLeaseRentalDTO> ContractualLeaseRentalDTOBatchService;

    @Autowired
    private Mapping<ContractualLeaseRentalEVM, ContractualLeaseRentalDTO> mapping;


    @Bean("contractualLeaseRentalPersistenceJob")
    public Job contractualLeaseRentalPersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("contractualLeaseRentalPersistenceJob")
            .preventRestart()
            .listener(persistenceJobListener)
            .incrementer(new RunIdIncrementer())
            .flow(readContractualLeaseRentalListFromFile())
            .end()
            .build();
        // @formatter:on
    }

    @Bean("readContractualLeaseRentalListFromFile")
    public Step readContractualLeaseRentalListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readContractualLeaseRentalListFromFile")
            .<List<ContractualLeaseRentalEVM>, List<ContractualLeaseRentalDTO>>chunk(2)
            .reader(contractualLeaseRentalItemReader(fileId))
            .processor(contractualLeaseRentalItemProcessor())
            .writer(contractualLeaseRentalItemWriter())
            .build();
        // @formatter:off
    }

    @Bean
    public ItemWriter<List<ContractualLeaseRentalDTO>> contractualLeaseRentalItemWriter() {

        return items -> items.stream().peek(ContractualLeaseRentalDTOBatchService::save).forEach(ContractualLeaseRentalDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<ContractualLeaseRentalEVM>, List<ContractualLeaseRentalDTO>> contractualLeaseRentalItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean
    @JobScope
    public ItemReader<List<ContractualLeaseRentalEVM>> contractualLeaseRentalItemReader(@Value("#{jobParameters['fileId']}") long fileId) {

        return new ContractualLeaseRentalListItemReader(deserializer, fileUploadService, fileId, fileUploadsProperties);
    }
}

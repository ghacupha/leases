package io.github.leases.internal.fileProcessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.leases.domain.enumeration.LeasesFileModelType.CONTRACTUAL_LEASE_RENTAL;
import static io.github.leases.domain.enumeration.LeasesFileModelType.CURRENCY_LIST;
import static io.github.leases.domain.enumeration.LeasesFileModelType.LEASE_DETAILS;

/**
 * This object maintains a list of all existing processors. This is a short in the dark about automatically configuring the chain at start up
 */
@Configuration
public class FileUploadProcessorContainer {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("currencyTablePersistenceJob")
    private Job currencyTablePersistenceJob;

    @Autowired
    @Qualifier("leaseDetailsPersistenceJob")
    private Job leaseDetailsPersistenceJob;

    @Autowired
    @Qualifier("contractualLeaseRentalPersistenceJob")
    private Job contractualLeaseRentalPersistenceJob;

    @Bean("fileUploadProcessorChain")
    public FileUploadProcessorChain fileUploadProcessorChain() {

        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        // Create the chain, each should match against it's specific key of data-model type
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, currencyTablePersistenceJob, CURRENCY_LIST));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, leaseDetailsPersistenceJob, LEASE_DETAILS));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, contractualLeaseRentalPersistenceJob, CONTRACTUAL_LEASE_RENTAL));

        return theChain;
    }

}

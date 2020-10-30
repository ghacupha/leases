package io.github.leases.internal.batch.contractualLeaseRental;

import io.github.leases.config.FileUploadsProperties;
import io.github.leases.internal.batch.ListPartition;
import io.github.leases.internal.excel.ExcelFileDeserializer;
import io.github.leases.internal.model.ContractualLeaseRentalEVM;
import io.github.leases.service.LeasesFileUploadService;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This bean reads data into the batch process from the file containing a list of
 * contractual-lease-rental
 */
@Scope("job")
public class ContractualLeaseRentalListItemReader implements ItemReader<List<ContractualLeaseRentalEVM>> {

    private static final Logger log = getLogger(ContractualLeaseRentalListItemReader.class);
    private final FileUploadsProperties fileUploadsProperties;

    private final ExcelFileDeserializer<ContractualLeaseRentalEVM> deserializer;
    private final LeasesFileUploadService fileUploadService;
    private long fileId;

    private ListPartition<ContractualLeaseRentalEVM> ContractualLeaseRentalEVMPartition;

    ContractualLeaseRentalListItemReader(final ExcelFileDeserializer<ContractualLeaseRentalEVM> deserializer,
                               final LeasesFileUploadService fileUploadService,
                               @Value("#{jobParameters['fileId']}") long fileId,
                               final FileUploadsProperties fileUploadsProperties) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;
        this.fileUploadsProperties = fileUploadsProperties;
    }

    @PostConstruct
    private void resetIndex() {

        final List<ContractualLeaseRentalEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        ContractualLeaseRentalEVMPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

        log.info("Items deserialized : {}", unProcessedItems.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Every time this method is called, it will return a List of unprocessed items the size of which is dictated by the maximumPageSize;
     * <p>
     * Once the list of unprocessed items hits zero, the method call will return null;
     * </p>
     */
    @Override
    public List<ContractualLeaseRentalEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<ContractualLeaseRentalEVM> forProcessing = ContractualLeaseRentalEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}

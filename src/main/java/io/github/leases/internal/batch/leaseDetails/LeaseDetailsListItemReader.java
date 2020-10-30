package io.github.leases.internal.batch.leaseDetails;

import io.github.leases.config.FileUploadsProperties;
import io.github.leases.internal.batch.ListPartition;
import io.github.leases.internal.excel.ExcelFileDeserializer;
import io.github.leases.internal.model.LeaseDetailsEVM;
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
 * This is a reader for excel file containing lease-details objects on row-by-row
 * arrangement.
 * We don create instances of this reader since it must be purpose built for a given
 * job. Everytime a new job starts a reader specific to that job is created. That is
 * the design
 */
@Scope("job")
public class LeaseDetailsListItemReader implements ItemReader<List<LeaseDetailsEVM>> {

    private static final Logger log = getLogger(LeaseDetailsListItemReader.class);

    private final FileUploadsProperties fileUploadsProperties;

    private final ExcelFileDeserializer<LeaseDetailsEVM> deserializer;
    private final LeasesFileUploadService fileUploadService;
    private long fileId;

    private ListPartition<LeaseDetailsEVM> LeaseDetailsEVMPartition;

    LeaseDetailsListItemReader(final ExcelFileDeserializer<LeaseDetailsEVM> deserializer,
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

        final List<LeaseDetailsEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        LeaseDetailsEVMPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

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
    public List<LeaseDetailsEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<LeaseDetailsEVM> forProcessing = LeaseDetailsEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}

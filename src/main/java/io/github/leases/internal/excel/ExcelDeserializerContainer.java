package io.github.leases.internal.excel;

import io.github.leases.internal.excel.deserializer.DefaultExcelFileDeserializer;
import io.github.leases.internal.model.ContractualLeaseRentalEVM;
import io.github.leases.internal.model.LeaseDetailsEVM;
import io.github.leases.internal.model.sampleDataModel.CurrencyTableEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.leases.internal.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

/**
 * This container has configurations for our excel file deserializers and a sample is provided for currency-table
 */
@Configuration
public class ExcelDeserializerContainer {

    @Bean("currencyTableExcelFileDeserializer")
    public ExcelFileDeserializer<CurrencyTableEVM> currencyTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(CurrencyTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("contractualLeaseRentalExcelFileDeserializer")
    public ExcelFileDeserializer<ContractualLeaseRentalEVM> contractualLeaseRentalExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(ContractualLeaseRentalEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("leaseDetailsEVMExcelFileDeserializer")
    public ExcelFileDeserializer<LeaseDetailsEVM> leaseDetailsEVMExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(LeaseDetailsEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }
    // todo create bean for every data model
}

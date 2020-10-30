package io.github.leases.internal.excel;

import io.github.leases.internal.model.ContractualLeaseRentalEVM;
import io.github.leases.internal.model.LeaseDetailsEVM;
import io.github.leases.internal.model.sampleDataModel.CurrencyTableEVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.github.leases.internal.excel.ExcelTestUtil.readFile;
import static io.github.leases.internal.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * This test shows how the deserializer works inside the ItemReader interface.
 */
public class ExcelFileUtilsTest {

    private ExcelDeserializerContainer container;

    @BeforeEach
    void setUp() {
        container = new ExcelDeserializerContainer();
    }

    @Test
    public void deserializeCurrenciesFile() throws Exception {

        ExcelFileDeserializer<CurrencyTableEVM> deserializer = container.currencyTableExcelFileDeserializer();

        List<CurrencyTableEVM> currencies = deserializer.deserialize(toBytes(readFile("currencies.xlsx")));

        assertThat(currencies.size()).isEqualTo(13);
        assertThat(currencies.get(0)).isEqualTo(CurrencyTableEVM.builder().rowIndex(1).country("USA").currencyCode("USD").currencyName("US DOLLAR").locality("FOREIGN").build());
        assertThat(currencies.get(1)).isEqualTo(CurrencyTableEVM.builder().rowIndex(2).country("UNITED KINGDOM").currencyCode("GBP").currencyName("STERLING POUND").locality("FOREIGN").build());
        assertThat(currencies.get(2)).isEqualTo(CurrencyTableEVM.builder().rowIndex(3).country("EURO-ZONE").currencyCode("EUR").currencyName("EURO").locality("FOREIGN").build());
        assertThat(currencies.get(3)).isEqualTo(CurrencyTableEVM.builder().rowIndex(4).country("KENYA").currencyCode("KES").currencyName("KENYA SHILLING").locality("LOCAL").build());
        assertThat(currencies.get(4)).isEqualTo(CurrencyTableEVM.builder().rowIndex(5).country("SWITZERLAND").currencyCode("CHF").currencyName("SWISS FRANC").locality("FOREIGN").build());
        assertThat(currencies.get(5)).isEqualTo(CurrencyTableEVM.builder().rowIndex(6).country("SOUTH AFRICA").currencyCode("ZAR").currencyName("SOUTH AFRICAN RAND").locality("FOREIGN").build());
        assertThat(currencies.get(12)).isEqualTo(CurrencyTableEVM.builder().rowIndex(13).country("CHINA").currencyCode("CNY").currencyName("CHINESE YUAN").locality("FOREIGN").build());
    }

    @Test
    public void leasesDetailsFiles() throws Exception {
        ExcelFileDeserializer<LeaseDetailsEVM> deserializer = container.leaseDetailsEVMExcelFileDeserializer();

        List<LeaseDetailsEVM> evms = deserializer.deserialize(toBytes(readFile("leaseDetails.xlsx")));

        assertThat(evms.size()).isEqualTo(868);

        for (int i = 0; i < 868; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    new LeaseDetailsEVM()
                        .setRowIndex(i + 1)
                        .setLeaseContractNumber(String.valueOf(1005+i))
                        .setIncrementalBorrowingRate(1 + i)
                        .setCommencementDate("2010/11/01")
                        .setLeasePrepayments(100 + i)
                        .setInitialDirectCosts(100 + i)
                        .setDemolitionCosts(100 + i)
                        .setAssetAccountNumber(String.valueOf(1005 + i) + "A")
                        .setLiabilityAccountNumber(String.valueOf(1005 + i) + "L")
                        .setDepreciationAccountNumber(String.valueOf(1005 + i) + "D")
                        .setInterestAccountNumber(String.valueOf(1005 + i) + "I")
                );
        }
    }

    @Test
    public void contractualLeaseFiles() throws Exception {
        ExcelFileDeserializer<ContractualLeaseRentalEVM> deserializer = container.contractualLeaseRentalExcelFileDeserializer();

        List<ContractualLeaseRentalEVM> evms = deserializer.deserialize(toBytes(readFile("contractualLeaseRental.xlsx")));

        assertThat(evms.size()).isEqualTo(40);

        for (int i = 0; i < 40; i++) {
            String index = String.valueOf(i + 1);
            assertThat(evms.get(i))
                .isEqualTo(
                    new ContractualLeaseRentalEVM()
                        .setRowIndex(i + 1)
                        .setLeaseContractNumber(String.valueOf(1005+i))
                        .setRentalSequenceNumber(String.valueOf(i + 1))
                        .setLeaseRentalDate("2010/11/01")
                        .setLeaseRentalAmount(100 + i)
                );
        }
    }

}

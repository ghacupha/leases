package io.github.leases.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ContractualLeaseRentalMapperTest {

    private ContractualLeaseRentalMapper contractualLeaseRentalMapper;

    @BeforeEach
    public void setUp() {
        contractualLeaseRentalMapper = new ContractualLeaseRentalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(contractualLeaseRentalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(contractualLeaseRentalMapper.fromId(null)).isNull();
    }
}

package io.github.leases.internal.fileProcessing;

import io.github.leases.service.LeasesMessageTokenQueryService;
import io.github.leases.service.dto.LeasesMessageTokenCriteria;
import io.github.leases.service.dto.LeasesMessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

/**
 * Implementation of token-search where the token value itself is of the value string
 */
@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final LeasesMessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final LeasesMessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public LeasesMessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        LeasesMessageTokenCriteria tokenValueCriteria = new LeasesMessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}

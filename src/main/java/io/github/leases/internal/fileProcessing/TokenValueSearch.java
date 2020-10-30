package io.github.leases.internal.fileProcessing;

import io.github.leases.service.dto.LeasesMessageTokenDTO;

/**
 * To search for Message-Token entity with a certain message-token-value
 * <p/>
 * Of course the assumption here is there exists a full blown message-token entity
 * <p/>
 * which is used to persist to db tokens as they are generated
 * @param <T>
 */
public interface TokenValueSearch<T> {

    LeasesMessageTokenDTO getMessageToken(final T tokenValue);
}

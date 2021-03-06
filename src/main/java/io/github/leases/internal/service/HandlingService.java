package io.github.leases.internal.service;

/**
 * This service the uploaded file in whatever way that is deemed necessary.
 * This service returns an instance of itself inorder to run withing a chain of file-handlers
 */
public interface HandlingService<H> {

    /**
     * Returns an instance of this after handling the payload issued
     *
     * @param payload The item being handled
     */
    void handle(H payload);
}

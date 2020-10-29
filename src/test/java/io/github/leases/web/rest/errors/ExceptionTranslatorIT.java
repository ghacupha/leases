package io.github.leases.web.rest.errors;

/*-
 * Leases - Leases management application
 * Copyright © 2020 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.leases.LeasesApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests {@link ExceptionTranslator} controller advice.
 */
@WithMockUser
@AutoConfigureWebTestClient
@SpringBootTest(classes = LeasesApp.class)
public class ExceptionTranslatorIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testConcurrencyFailure() {
        webTestClient.get().uri("/api/exception-translator-test/concurrency-failure")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.CONFLICT)
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo(ErrorConstants.ERR_CONCURRENCY_FAILURE);
    }

    @Test
    public void testMethodArgumentNotValid() {
         webTestClient.post().uri("/api/exception-translator-test/method-argument")
             .contentType(MediaType.APPLICATION_JSON)
             .bodyValue("{}")
             .exchange()
             .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
             .expectBody()
             .jsonPath("$.message").isEqualTo(ErrorConstants.ERR_VALIDATION)
             .jsonPath("$.fieldErrors.[0].objectName").isEqualTo("test")
             .jsonPath("$.fieldErrors.[0].field").isEqualTo("test")
             .jsonPath("$.fieldErrors.[0].message").isEqualTo("NotNull");
    }

    @Test
    public void testMissingRequestPart() {
        webTestClient.get().uri("/api/exception-translator-test/missing-servlet-request-part")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400");
    }

    @Test
    public void testMissingRequestParameter() {
        webTestClient.get().uri("/api/exception-translator-test/missing-servlet-request-parameter")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400");
    }

    @Test
    public void testAccessDenied() {
        webTestClient.get().uri("/api/exception-translator-test/access-denied")
            .exchange()
            .expectStatus().isForbidden()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.403")
            .jsonPath("$.detail").isEqualTo("test access denied!");
    }

    @Test
    public void testUnauthorized() {
        webTestClient.get().uri("/api/exception-translator-test/unauthorized")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.401")
            .jsonPath("$.path").isEqualTo("/api/exception-translator-test/unauthorized")
            .jsonPath("$.detail").isEqualTo("test authentication failed!");
    }

    @Test
    public void testMethodNotSupported() {
        webTestClient.post().uri("/api/exception-translator-test/access-denied")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.405")
            .jsonPath("$.detail").isEqualTo("405 METHOD_NOT_ALLOWED \"Request method 'POST' not supported\"");
    }

    @Test
    public void testExceptionWithResponseStatus() {
        webTestClient.get().uri("/api/exception-translator-test/response-status")
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.400")
            .jsonPath("$.title").isEqualTo("test response status");
    }

    @Test
    public void testInternalServerError() {
        webTestClient.get().uri("/api/exception-translator-test/internal-server-error")
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.message").isEqualTo("error.http.500")
            .jsonPath("$.title").isEqualTo("Internal Server Error");
    }

}

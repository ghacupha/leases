package io.github.leases.web.rest;

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
import io.github.leases.config.Constants;
import io.github.leases.domain.User;
import io.github.leases.repository.UserRepository;
import io.github.leases.web.rest.vm.LoginVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UserJWTController} REST controller.
 */
@AutoConfigureWebTestClient
@SpringBootTest(classes = LeasesApp.class)
public class UserJWTControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testAuthorize() throws Exception {
        User user = new User();
        user.setLogin("user-jwt-controller");
        user.setEmail("user-jwt-controller@example.com");
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));
        user.setCreatedBy(Constants.SYSTEM_ACCOUNT);

        userRepository.save(user).block();

        LoginVM login = new LoginVM();
        login.setUsername("user-jwt-controller");
        login.setPassword("test");
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("$.id_token").isNotEmpty();
    }

    @Test
    public void testAuthorizeWithRememberMe() throws Exception {
        User user = new User();
        user.setLogin("user-jwt-controller-remember-me");
        user.setEmail("user-jwt-controller-remember-me@example.com");
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode("test"));
        user.setCreatedBy(Constants.SYSTEM_ACCOUNT);

        userRepository.save(user).block();

        LoginVM login = new LoginVM();
        login.setUsername("user-jwt-controller-remember-me");
        login.setPassword("test");
        login.setRememberMe(true);
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueMatches("Authorization", "Bearer .+")
            .expectBody()
            .jsonPath("$.id_token").isNotEmpty();
    }

    @Test
    public void testAuthorizeFails() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername("wrong-user");
        login.setPassword("wrong password");
        webTestClient.post().uri("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(login))
            .exchange()
            .expectStatus().isUnauthorized()
            .expectHeader().doesNotExist("Authorization")
            .expectBody()
            .jsonPath("$.id_token").doesNotExist();
    }
}

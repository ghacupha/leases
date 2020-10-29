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

import io.github.leases.config.Constants;
import io.github.leases.security.jwt.JWTFilter;
import io.github.leases.security.jwt.TokenProvider;
import io.github.leases.service.AuditEventService;
import io.github.leases.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    private final AuditEventService auditEventService;

    public UserJWTController(TokenProvider tokenProvider, ReactiveAuthenticationManager authenticationManager, AuditEventService auditEventService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.auditEventService = auditEventService;
    }

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<JWTToken>> authorize(@Valid @RequestBody Mono<LoginVM> loginVM) {
        return loginVM
            .flatMap(login -> authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                .onErrorResume(throwable -> onAuthenticationError(login, throwable))
                .flatMap(auth -> onAuthenticationSuccess(login, auth))
                .flatMap(auth -> Mono.fromCallable(() -> tokenProvider.createToken(auth, Boolean.TRUE.equals(login.isRememberMe()))))
            )
            .map(jwt -> {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
            });
    }

    private Mono<? extends Authentication> onAuthenticationSuccess(LoginVM login, Authentication auth) {
        return Mono.just(login)
            .map(LoginVM::getUsername)
            .filter(username -> !Constants.ANONYMOUS_USER.equals(username))
            .flatMap(auditEventService::saveAuthenticationSuccess)
            .thenReturn(auth);
    }

    private Mono<? extends Authentication> onAuthenticationError(LoginVM login, Throwable throwable) {
        return Mono.just(login)
            .map(LoginVM::getUsername)
            .filter(username -> !Constants.ANONYMOUS_USER.equals(username))
            .flatMap(username -> auditEventService.saveAuthenticationError(username, throwable))
            .then(Mono.error(throwable));
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}

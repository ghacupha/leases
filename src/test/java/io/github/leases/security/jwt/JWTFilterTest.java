package io.github.leases.security.jwt;

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

import io.github.leases.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class JWTFilterTest {

    private TokenProvider tokenProvider;

    private JWTFilter jwtFilter;

    @BeforeEach
    public void setup() {
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        tokenProvider = new TokenProvider(jHipsterProperties);
        ReflectionTestUtils.setField(tokenProvider, "key",
            Keys.hmacShaKeyFor(Decoders.BASE64
                .decode("fd54a45s65fds737b9aafcb3412e07ed99b267f33413274720ddbb7f6c5e64e9f14075f2d7ed041592f0b7657baf8")));

        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", 60000);
        jwtFilter = new JWTFilter(tokenProvider);
    }

    @Test
    public void testJWTFilter() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "test-user",
            "test-password",
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        String jwt = tokenProvider.createToken(authentication, false);
        MockServerHttpRequest.BaseBuilder request = MockServerHttpRequest
            .get("/api/test")
            .header(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        jwtFilter.filter(
            exchange,
            it -> Mono.subscriberContext()
                .flatMap(c -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .doOnSuccess(auth -> assertThat(auth.getName()).isEqualTo("test-user"))
                .doOnSuccess(auth -> assertThat(auth.getCredentials().toString()).isEqualTo(jwt))
                .then()
        ).block();
    }

    @Test
    public void testJWTFilterInvalidToken() {
        String jwt = "wrong_jwt";
        MockServerHttpRequest.BaseBuilder request = MockServerHttpRequest
            .get("/api/test")
            .header(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        jwtFilter.filter(
            exchange,
            it -> Mono.subscriberContext()
                .flatMap(c -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .doOnSuccess(auth -> assertThat(auth).isNull())
                .then()
        ).block();
    }

    @Test
    public void testJWTFilterMissingAuthorization() {
        MockServerHttpRequest.BaseBuilder request = MockServerHttpRequest
            .get("/api/test");
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        jwtFilter.filter(
            exchange,
            it -> Mono.subscriberContext()
                .flatMap(c -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .doOnSuccess(auth -> assertThat(auth).isNull())
                .then()
        ).block();
    }

    @Test
    public void testJWTFilterMissingToken() {
        MockServerHttpRequest.BaseBuilder request = MockServerHttpRequest
            .get("/api/test")
            .header(JWTFilter.AUTHORIZATION_HEADER, "Bearer ");
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        jwtFilter.filter(
            exchange,
            it -> Mono.subscriberContext()
                .flatMap(c -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .doOnSuccess(auth -> assertThat(auth).isNull())
                .then()
        ).block();
    }

    @Test
    public void testJWTFilterWrongScheme() {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "test-user",
            "test-password",
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        String jwt = tokenProvider.createToken(authentication, false);
        MockServerHttpRequest.BaseBuilder request = MockServerHttpRequest
            .get("/api/test")
            .header(JWTFilter.AUTHORIZATION_HEADER, "Basic " + jwt);
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        jwtFilter.filter(
            exchange,
            it -> Mono.subscriberContext()
                .flatMap(c -> ReactiveSecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .doOnSuccess(auth -> assertThat(auth).isNull())
                .then()
        ).block();
    }

}

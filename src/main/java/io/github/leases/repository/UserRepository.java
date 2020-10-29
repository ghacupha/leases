package io.github.leases.repository;

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

import io.github.leases.domain.Authority;
import io.github.leases.domain.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, Long>, UserRepositoryInternal {

    @Query("SELECT * FROM jhi_user WHERE activation_key = :activationKey")
    Mono<User> findOneByActivationKey(String activationKey);

    @Query("SELECT * FROM jhi_user WHERE activated = false AND activation_key IS NOT NULL AND created_date < :dateTime")
    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    @Query("SELECT * FROM jhi_user WHERE reset_key = :resetKey")
    Mono<User> findOneByResetKey(String resetKey);

    @Query("SELECT * FROM jhi_user WHERE LOWER(email) = LOWER(:email)")
    Mono<User> findOneByEmailIgnoreCase(String email);

    @Query("SELECT * FROM jhi_user WHERE login = :login")
    Mono<User> findOneByLogin(String login);

    @Query("SELECT COUNT(DISTINCT id) FROM jhi_user WHERE login != :anonymousUser")
    Mono<Long> countAllByLoginNot(String anonymousUser);

    @Query("INSERT INTO jhi_user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(Long userId, String authority);

    @Query("DELETE FROM jhi_user_authority")
    Mono<Void> deleteAllUserAuthorities();
}

interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}

interface UserRepositoryInternal extends DeleteExtended<User> {

    Mono<User> findOneWithAuthoritiesByLogin(String login);

    Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<User> findAllByLoginNot(Pageable pageable, String login);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {
    private final DatabaseClient db;
    private final ReactiveDataAccessStrategy dataAccessStrategy;

    public UserRepositoryInternalImpl(DatabaseClient db, ReactiveDataAccessStrategy dataAccessStrategy) {
        this.db = db;
        this.dataAccessStrategy = dataAccessStrategy;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email.toLowerCase());
    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db.execute("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id WHERE u." + fieldName + " = :" + fieldName)
            .bind(fieldName, fieldValue)
            .map((row, metadata) ->
                Tuples.of(
                    dataAccessStrategy.getRowMapper(User.class).apply(row, metadata),
                    Optional.ofNullable(row.get("authority_name", String.class))
                )
            )
            .all()
            .collectList()
            .filter(l -> !l.isEmpty())
            .map(l -> {
                User user = l.get(0).getT1();
                user.setAuthorities(
                    l.stream()
                        .filter(t -> t.getT2().isPresent())
                        .map(t -> {
                            Authority authority = new Authority();
                            authority.setName(t.getT2().get());
                            return authority;
                        })
                        .collect(Collectors.toSet())
                );
                return user;
            });
    }

    @Override
    public Flux<User> findAllByLoginNot(Pageable pageable, String login) {
        return db.select().from(User.class)
            .matching(Criteria.where("login").not(login))
            .page(pageable)
            .as(User.class)
            .all();
    }

    @Override
    public Mono<Void> delete(User user) {
        return db.execute("DELETE FROM jhi_user_authority WHERE user_id = :userId")
            .bind("userId", user.getId())
            .then()
            .then(db.delete()
                .from(User.class)
                .matching(Criteria.where("id").is(user.getId()))
                .then()
            );
    }

}

package com.error.center.domain.repository;

import com.error.center.domain.enums.Level;
import com.error.center.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {

    Optional<Event> findByLevelAndDescriptionIgnoreCaseAndLogIgnoreCaseAndOriginIgnoreCase(
            @Param("level") Level level,
            @Param("description") String description,
            @Param("log") String log,
            @Param("origin") String origin
    );

    @Query("select e from Event e " +
            "where lower(e.description) like %:search% " +
            "or lower(e.log) like %:search% " +
            "or lower(e.origin) like %:search%")
    Page<Event> search(@Param("search") String search, Pageable pageable);

    @Query("select e from Event e " +
            "where e.level = :level " +
            "and (lower(e.description) like %:search% " +
            "or lower(e.log) like %:search% " +
            "or lower(e.origin) like %:search%)")
    Page<Event> searchByLevel(@Param("search") String search, @Param("level") Level level, Pageable pageable);

    @Query("select e from Event e " +
            "where e.date between :init and :end " +
            "and (lower(e.description) like %:search% " +
            "or lower(e.log) like %:search% " +
            "or lower(e.origin) like %:search%)")
    Page<Event> searchByDate(@Param("search") String search, @Param("init") Date init, @Param("end") Date end, Pageable pageable);
}

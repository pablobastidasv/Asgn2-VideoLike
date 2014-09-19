package org.magnum.mobilecloud.video.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * Created by pbastidas on 9/14/14.
 */

public interface VideoRepo extends CrudRepository<Video, Long> {

    public Collection<Video> findByName(String name);

    @Query("SELECT v FROM Video v WHERE v.duration < :duration")
    public Collection<Video> findByDurationLessThan(@Param("duration") long duration);
}

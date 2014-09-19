package org.magnum.mobilecloud.video.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by pbastidas on 9/14/14.
 */

public interface VideoRepo extends CrudRepository<Video, Long> {

    public Collection<Video> findByName(String name);
}

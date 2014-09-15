package org.magnum.mobilecloud.video;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbastidas on 9/14/14.
 */
@Controller
public class VideoController {

    @Autowired
    private VideoRepo videoRepo;

    @RequestMapping(method = RequestMethod.GET, value = VideoSvcApi.VIDEO_SVC_PATH)
    public @ResponseBody List<Video> getVideos(){
        return Lists.newArrayList(videoRepo.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = VideoSvcApi.VIDEO_SVC_PATH)
    public @ResponseBody Long getVideos(@RequestBody Video video){
        videoRepo.save(video);

        return video.getId();
    }
}

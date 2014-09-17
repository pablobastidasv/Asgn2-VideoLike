package org.magnum.mobilecloud.video;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public @ResponseBody Video getVideos(@RequestBody Video video){
        videoRepo.save(video);

        return video;
    }

    @RequestMapping(method = RequestMethod.GET, value = VideoSvcApi.VIDEO_SVC_PATH + "/{videoId}")
    public @ResponseBody Video getVideo(@PathVariable(value = "videoId") long videoId, HttpServletResponse response){
        Video video = videoRepo.findOne(videoId);

        if(video == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return video;
    }

    @RequestMapping(method = RequestMethod.POST, value = VideoSvcApi.VIDEO_SVC_PATH+ "/{id}/like")
    public @ResponseBody Video likeVideo(@PathVariable(value = "id") long videoId){
        Video video = videoRepo.findOne(videoId);

        video.setLikes(video.getLikes() + 1);

        videoRepo.save(video);

        return video;
    }

}

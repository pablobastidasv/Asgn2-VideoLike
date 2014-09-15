package org.magnum.mobilecloud.video;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pbastidas on 9/14/14.
 */
@Controller
public class VideoController {
    @RequestMapping(method = RequestMethod.GET, value = "/video")
    public void getVideos(){

    }
}

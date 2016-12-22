/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.crangis.leader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@RestController
public class GameController implements LogListener{




    private List<Follower> followers;
    private Log log;
    private Map<String, Score> currentState;


    public GameController(@Value("${followers}") String followerList) {
        this.currentState = new HashMap<String, Score>();
        this.log = new Log();
        this.log.addListener(this);

        String[] followersArray = followerList.split(",");
        followers = new ArrayList<Follower>();
        for (String s : followersArray) {
            String[] address = s.split(":");
            followers.add(new Follower(address[0], Integer.parseInt(address[1])));
        }

        // hard code some followers
        for(Follower f : followers) {
            this.log.addListener(new LogReplicator(f));
        }
    }

    @RequestMapping(value = "/home/score", method = RequestMethod.PUT)
    public void homeScore(@RequestBody String score){
        log.append(new Score("home", Integer.parseInt(score)));
    }

    @RequestMapping(value = "/visitor/score", method = RequestMethod.PUT)
    public void visitorScore(@RequestBody String score){
        log.append(new Score("visitor", Integer.parseInt(score)));
    }

    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public Collection<Score> currentScore(){
        return currentState.values();
    }

    public void newScore(Score score) {
        if (currentState.containsKey(score.getTeam())) {
            currentState.replace(score.getTeam(), score);
        }else {
            currentState.put(score.getTeam(), score);
        }
    }
}

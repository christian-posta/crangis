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
package io.crangis.follower;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@RestController
public class StateMachine implements LogListener{

    private Map<String, Score> currentState;
    private Log log;

    public StateMachine() {
        this.currentState = new HashMap<String, Score>();
        this.log = new Log();
        this.log.addListener(this);
    }

    @RequestMapping(value = "appendScore", method = RequestMethod.POST)
    public AppendResponse appendScoreToLog(@RequestBody Score score) {
        log.append(score);
        return new AppendResponse("OK");
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

// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import java.util.ArrayList;
import java.util.Collection;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendeeCollection = request.getAttendees();
    ArrayList<String> attendees = new ArrayList<>(attendeeCollection);
    long duration = request.getDuration();
    Collection<TimeRange> results= new ArrayList<TimeRange>();
    ArrayList<Event> eventList = new ArrayList<>(events);
    if (duration > TimeRange.WHOLE_DAY.duration()){
        return results;
    }
    if(events.size()==0){
        results.add(TimeRange.fromStartDuration(0, 24*60));
        return results;
    }
    int currentStart = 0;
    int currentEnd = 0;
    for(int i = 0; i < eventList.size(); i++){
        if(i - 1 >= 0 && eventList.get(i-1).getWhen().contains(eventList.get(i).getWhen())){
            continue;
        }
        for (int j = 0; j < attendees.size(); j++ ){
            if (eventList.get(i).getAttendees().contains(attendees.get(j))){
                currentEnd = eventList.get(i).getWhen().start();
                if(currentStart!=currentEnd && currentEnd-currentStart>=duration){
                    TimeRange possibleTime = TimeRange.fromStartEnd(currentStart, currentEnd, false);
                    results.add(possibleTime);
                }
                 currentStart= currentEnd+eventList.get(i).getWhen().duration();
                    currentEnd = currentStart;
            }

        }
    }
    currentEnd=24*60;
    if(currentStart!=currentEnd && currentEnd-currentStart>=duration){
                    TimeRange possibleTime = TimeRange.fromStartEnd(currentStart, currentEnd, false);
                    results.add(possibleTime);
    }
    return results;
  }

}

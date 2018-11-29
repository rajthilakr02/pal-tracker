package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private final HashMap<Long,TimeEntry> timeEntryMap = new HashMap<>();
    private final List<TimeEntry> timeEntryList = new ArrayList<>();
    private long counter = 0l;

    public TimeEntry create(TimeEntry timeEntry){
        counter = counter+1l;
        timeEntry.setId(counter);
        timeEntryMap.put(counter,timeEntry);
        timeEntryList.add(timeEntry);
        return timeEntryMap.get(counter);
    }

    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    public List<TimeEntry> list() {
        return timeEntryList;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
       timeEntry.setId(id);
       timeEntryMap.put(id, timeEntry);
       return timeEntryMap.get(id);
    }

    public void delete(long id) {
        timeEntryList.remove(timeEntryMap.get(id));
        timeEntryMap.remove(id);
    }

}

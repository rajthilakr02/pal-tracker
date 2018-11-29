package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private final TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity(timeEntryRepository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @RequestMapping(path="/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {
        TimeEntry entry = timeEntryRepository.find(timeEntryId);
        if(null != entry && entry.getId()==timeEntryId)
            return ResponseEntity.ok(entry);
        else
            return new ResponseEntity(entry, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable("timeEntryId") long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry entry = timeEntryRepository.update(timeEntryId,expected);
        if(null != entry && entry.getId() == timeEntryId)
            return ResponseEntity.ok(entry);
        else
            return new ResponseEntity(entry, HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("timeEntryId") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

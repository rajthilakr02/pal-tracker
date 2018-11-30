package io.pivotal.pal.tracker;

import javax.sql.DataSource;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours)" +
                    "VALUES(?,?,?,?)",
                    RETURN_GENERATED_KEYS);
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            System.out.println(timeEntry.getDate());
            System.out.println("local date "+Date.valueOf(timeEntry.getDate()));
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            return statement;
        },keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        String sql = "SELECT * from time_entries where id=?";
        try {
            Object object = jdbcTemplate.queryForObject(sql, new Object[]{id}, new TimeEntryMapper());
            return object!=null ? (TimeEntry) object : null;

        } catch(Exception exp){
            //handling specific not found condition
            if(exp.getMessage()!=null && exp.getMessage().equalsIgnoreCase("RowMapper is required")){
                return null;
            }
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * from time_entries";
        return jdbcTemplate.query(sql,new TimeEntryMapper());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }


}

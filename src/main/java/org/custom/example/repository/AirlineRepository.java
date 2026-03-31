package org.custom.example.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.custom.example.entity.Airline;

@Repository
public class AirlineRepository {
    private final JdbcTemplate jdbc;

    public AirlineRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Airline> findAll() {
        return jdbc.query(
            "SELECT id, name FROM airline",
            (rs, rowNum) -> {
                Airline a = new Airline();
                a.setId(rs.getLong("id"));
                a.setName(rs.getString("name"));
                return a;
            }
        );
    }

    public Airline findById(Long id) {
        return jdbc.queryForObject(
            "SELECT id, name FROM airline WHERE id = ?",
            (rs, rowNum) -> {
                Airline a = new Airline();
                a.setId(rs.getLong("id"));
                a.setName(rs.getString("name"));
                return a;
            },
            id
        );
    }

    public int save(Airline airline) {
        return jdbc.update(
            "INSERT INTO airline (name) VALUES (?)",
            airline.getName()
        );
    }

    public int delete(Long id) {
        return jdbc.update(
            "DELETE FROM airline WHERE id = ?",
            id
        );
    }
}

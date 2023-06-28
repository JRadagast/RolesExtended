package com.demo.RolesExtension.repository;

import com.demo.RolesExtension.SpringJdbcConfig;
import com.demo.RolesExtension.beans.Team;
import com.demo.RolesExtension.util.UuidUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;

@Repository
public class TeamRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * SQLs are loaded as static so its not needed to load them again for every instance of teamRepository created.
     */
    private static String QUERY_SAVE = "INSERT INTO team (idteam, name, idteamlead) VALUES " +
            "( :IDTEAM, :NAME, :TEAMLEADID )";
    
    private static String QUERY_UPDATE = "UPDATE team SET " +
            " name = :NAME, idteamlead = :TEAMLEADID where idteam = :IDTEAM ";
    
    private static String QUERY_FIND_BY_ID = "SELECT * FROM team WHERE idteam = :IDTEAM";
    
    
    @PostConstruct
    public void init(){
        setDataSource();
    }
    
    /**
     * Insert team on the database.
     * 
     * @param team
     * @return 
     */
    public Team save(Team team) {       

        MapSqlParameterSource params = getParamsInsert( team );
        System.out.println("TEAM LEAD ID: " + team.getTeamLeadId() );

        this.jdbcTemplate.update(QUERY_SAVE, params );
        Team t = null;

        /* Checks if the team has successfully been added to the database */
        try {
            t = getById( team.getIdteam() );
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return t;
    }
    
    /**
     * Update a team registry
     * @param team
     * @return
     * @throws SQLException 
     */
    public Team update( Team team ) throws SQLException {
        MapSqlParameterSource params = getParamsInsert( team );

        this.jdbcTemplate.update(QUERY_UPDATE, params);

        return team;
    }

    /* Defines the namedParameters used when inserting a Team. */
    public MapSqlParameterSource getParamsInsert( Team team ){
        MapSqlParameterSource params = new MapSqlParameterSource();
        
        params.addValue("IDTEAM", team.getIdteam() );
        params.addValue("NAME", team.getName() );
        params.addValue("TEAMLEADID", team.getTeamLeadId());

        return params;
    }

    /***
     * Get team by id.
     * 
     * @param idTeam
     * @return
     * @throws SQLException 
     */
    public Team getById(String idTeam ) throws SQLException {
        MapSqlParameterSource params = new MapSqlParameterSource();

        List<Team> x = this.jdbcTemplate.query(QUERY_FIND_BY_ID, params, rowMapper() );

        return x != null && x.size() > 0 ? x.get(0) : null;
    }

    /**
     * Map the team from the database to the entity bean.
     * 
     * @return
     * @throws SQLException 
     */
    public RowMapper<Team> rowMapper() throws SQLException {
        return new RowMapper<Team>() {
            @Override
            public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
                Team t = new Team();

                t.setIdteam( rs.getString("IDTEAM") );
                t.setName( rs.getString("NAME") );
                t.setTeamLeadId( rs.getString("IDTEAMLEAD") );

                return t;
            }
        };
    }

    
    /**
     * Initializes the data Source as defined on the default configuration.
     */
    public void setDataSource() {
        SpringJdbcConfig config = new SpringJdbcConfig();
        this.jdbcTemplate = new NamedParameterJdbcTemplate( config.mySqlDataSource());
    }
}

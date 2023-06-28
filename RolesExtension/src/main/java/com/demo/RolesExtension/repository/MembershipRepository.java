package com.demo.RolesExtension.repository;

import com.demo.RolesExtension.SpringJdbcConfig;
import com.demo.RolesExtension.beans.Membership;
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
public class MembershipRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * SQLs are loaded as static so its not needed to load them again for every instance of membershipRepository created.
     */
    private static String QUERY_SAVE = "INSERT INTO membership (idteam, iduser) VALUES " +
            "( :IDTEAM, :IDUSER )";
    
    private static String QUERY_UPDATE = "UPDATE membership SET " +
            " idteam = :IDTEAM, iduser = :IDUSER where idmembership = :IDMEMBERSHIP ";
    
    private static String QUERY_FIND_BY_ID = "SELECT * FROM membership WHERE idmembership = :IDMEMBERSHIP";
    
    private static String QUERY_FIND_BY_ROLE = "SELECT m.*\n" +
                                               "FROM membership m\n" +
                                               "INNER JOIN user u ON m.iduser = u.iduser\n" +
                                               "INNER JOIN role r ON r.idrole = u.idrole\n" +
                                               "WHERE r.idrole = :IDROLE";
    
    @PostConstruct
    public void init(){
        setDataSource();
    }
    
    /**
     * Insert membership on the database.
     * 
     * @param membership
     * @return 
     */
    public Membership save(Membership membership) {        
        /* Get the database instance keyholder, so its possible to retrieve the key after the insert instruction is executed. */
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = getParamsInsert( membership );

        this.jdbcTemplate.update(QUERY_SAVE, params, keyHolder, new String[] { "IDMEMBERSHIP" });
        Membership m = null;

        /* Checks if the membership has successfully been added to the database */
        try {
            m = getById(keyHolder.getKey().longValue());
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return m;
    }
    
    /**
     * Update a membership registry
     * @param membership
     * @return
     * @throws SQLException 
     */
    public Membership update( Membership membership ) throws SQLException {
        MapSqlParameterSource params = getParamsInsert( membership );

        this.jdbcTemplate.update(QUERY_UPDATE, params);

        return membership;
    }

    /* Defines the namedParameters used when inserting a membership. */
    public MapSqlParameterSource getParamsInsert( Membership membership ){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDTEAM", membership.getIdteam());
        params.addValue("IDUSER", membership.getIduser());

        return params;
    }

    /***
     * Get membership by id.
     * 
     * @param idmembership
     * @return
     * @throws SQLException 
     */
    public Membership getById(Long idmembership ) throws SQLException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDMEMBERSHIP", idmembership);

        List<Membership> x = this.jdbcTemplate.query(QUERY_FIND_BY_ID, params, rowMapper() );

        return x != null && x.size() > 0 ? x.get(0) : null;
    }
    
    /***
     * Get memberships by role.
     * 
     * @param idmembership
     * @return
     * @throws SQLException 
     */
    public List<Membership> getMembershipsByRole(Long idRole ) throws SQLException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDROLE", idRole);

        List<Membership> x = this.jdbcTemplate.query(QUERY_FIND_BY_ROLE, params, rowMapper() );

        return x;
    }

    /**
     * Map the membership from the database to the entity bean.
     * 
     * @return
     * @throws SQLException 
     */
    public RowMapper<Membership> rowMapper() throws SQLException {
        return new RowMapper<Membership>() {
            @Override
            public Membership mapRow(ResultSet rs, int rowNum) throws SQLException {
                Membership m = new Membership();

                m.setIdmembership( rs.getLong("IDMEMBERSHIP") );
                m.setIdteam( rs.getString("IDTEAM") );
                m.setIduser( rs.getString("IDUSER") );

                return m;
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

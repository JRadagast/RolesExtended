package com.demo.RolesExtension.repository;

import com.demo.RolesExtension.SpringJdbcConfig;
import com.demo.RolesExtension.beans.Role;
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
public class RoleRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * SQLs are loaded as static so its not needed to load them again for every instance of roleRepository created.
     */
    private static String QUERY_SAVE = "INSERT INTO role (name, isdefault) VALUES " +
            "( :NAME, :ISDEFAULT )";
    
    private static String QUERY_SET_NOT_DEFAULT = "UPDATE role SET " +
            " isdefault = 0 WHERE isdefault = 1";
    
    private static String QUERY_UPDATE = "UPDATE role SET " +
            " name = :NAME where idrole = :IDROLE ";
    
    private static String QUERY_FIND_BY_ID = "SELECT * FROM role WHERE idrole = :IDROLE";
    
    private static String QUERY_FIND_DEFAULT = "SELECT * FROM role WHERE isdefault = :ISDEFAULT";
    
    private static String QUERY_FIND_BY_MEMBERSHIP = "SELECT r.*\n" +
                                                     "FROM role r\n" +
                                                     "INNER JOIN user u ON r.idrole = u.idrole\n" +
                                                     "INNER JOIN membership m ON m.iduser = u.iduser\n" +
                                                     "WHERE m.iduser = :IDUSER\n" +
                                                     "AND m.idteam = :IDTEAM";
    
    private static String QUERY_INSERT_STARTING_VALUES1 = "INSERT IGNORE INTO `role` (`idrole`, `name`, `isdefault`) VALUES (1, 'Developer', 1);";
    private static String QUERY_INSERT_STARTING_VALUES2 = "INSERT IGNORE INTO `role` (`idrole`, `name`, `isdefault`) VALUES (2, 'Product Owner', 0);";
    private static String QUERY_INSERT_STARTING_VALUES3 = "INSERT IGNORE INTO `role` (`idrole`, `name`, `isdefault`) VALUES (3, 'Tester', 0);";
    
    
    @PostConstruct
    public void init(){
        setDataSource();
    }
    
    /**
     * Insert role on the database.
     * 
     * @param role
     * @return 
     */
    public Role save(Role role) {
        /* Get the database instance keyholder, so its possible to retrieve the key after the insert instruction is executed. */
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = getParamsInsert( role );
        
        if ( role.getIsdefault() == true ){
            try {
                this.updateNotDefault();
            } catch ( SQLException ex ){
                ex.printStackTrace();
            }
        }

        this.jdbcTemplate.update(QUERY_SAVE, params, keyHolder, new String[] { "IDROLE" });
        Role r = null;

        /* Checks if the role has successfully been added to the database */
        try {
            r = getById(keyHolder.getKey().longValue());
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return r;
    }
    
    /**
     * Update a role registry
     * @param role
     * @return
     * @throws SQLException 
     */
    public Role update( Role role ) throws SQLException {

        MapSqlParameterSource params = getParamsInsert( role );
        
        if ( role.getIsdefault() == true ){
            try {
                this.updateNotDefault();
            } catch ( SQLException ex ){
                ex.printStackTrace();
            }
        }

        this.jdbcTemplate.update(QUERY_UPDATE, params);

        return role;
    }
    
    /**
     * Set every default role as not default.
     * @param role
     * @return
     * @throws SQLException 
     */
    public void updateNotDefault() throws SQLException {

        MapSqlParameterSource params = new MapSqlParameterSource();

        this.jdbcTemplate.update(QUERY_SET_NOT_DEFAULT, params);
    }

    /* Defines the namedParameters used when inserting a role. */
    public MapSqlParameterSource getParamsInsert( Role role ){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("NAME", role.getName() );
        params.addValue("ISDEFAULT", role.getIsdefault() );

        return params;
    }

    /***
     * Get role by id.
     * 
     * @param idrole
     * @return
     * @throws SQLException 
     */
    public Role getById(Long idrole ) throws SQLException {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDROLE", idrole);

        List<Role> x = this.jdbcTemplate.query(QUERY_FIND_BY_ID, params, rowMapper() );

        return x != null && x.size() > 0 ? x.get(0) : null;
    }
    
    /**
     * Get the ID for the role defined as DEFAULT
     * @return
     * @throws SQLException 
     */
    public Long getDefaultRoleId () throws SQLException {
        
        /**/
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ISDEFAULT", 1);
        
        List<Role> role = this.jdbcTemplate.query(QUERY_FIND_DEFAULT, params, rowMapper() );
        
        return role != null && role.size() > 0 ? role.get(0).getIdrole() : null;
    }
    
    /***
     * Get role from a membership.
     * 
     * @param idrole
     * @return
     * @throws SQLException 
     */
    public Role getRoleByMembership(String idUser, String idTeam ) throws SQLException {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDUSER", idUser);
        params.addValue("IDTEAM", idTeam);

        List<Role> x = this.jdbcTemplate.query(QUERY_FIND_BY_MEMBERSHIP, params, rowMapper() );

        return x != null && x.size() > 0 ? x.get(0) : null;
    }

    /**
     * Map the role from the database to the entity bean.
     * 
     * @return
     * @throws SQLException 
     */
    public RowMapper<Role> rowMapper() throws SQLException {
        return new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                Role r = new Role();

                r.setIdrole(rs.getLong("IDROLE") );
                r.setName(rs.getString("NAME") );
                r.setIsdefault( rs.getBoolean("ISDEFAULT") );

                return r;
            }
        };
    }
    
    /**
     * Insert initial values for Roles.
     */
    public void insertStarterRoles(){
        
        MapSqlParameterSource params = new MapSqlParameterSource();

        this.jdbcTemplate.update(QUERY_INSERT_STARTING_VALUES1, params);
        this.jdbcTemplate.update(QUERY_INSERT_STARTING_VALUES2, params);
        this.jdbcTemplate.update(QUERY_INSERT_STARTING_VALUES3, params);
    }

    
    /**
     * Initializes the data Source as defined on the default configuration.
     */
    public void setDataSource() {
        SpringJdbcConfig config = new SpringJdbcConfig();
        this.jdbcTemplate = new NamedParameterJdbcTemplate( config.mySqlDataSource());
    }
}

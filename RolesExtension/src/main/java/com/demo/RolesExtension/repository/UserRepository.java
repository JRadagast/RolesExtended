package com.demo.RolesExtension.repository;

import com.demo.RolesExtension.SpringJdbcConfig;
import com.demo.RolesExtension.beans.User;
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
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class UserRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @Autowired
    private RoleRepository roleRepository;

    /**
     * SQLs are loaded as static so its not needed to load them again for every instance of UserRepository created.
     */
    private static String QUERY_SAVE = "INSERT INTO user (iduser, firstName, lastName, idrole, displayName, avatarUrl, location) VALUES " +
            "( :IDUSER, :FIRSTNAME, :LASTNAME, :IDROLE, :DISPLAYNAME, :AVATARURL, :LOCATION )";
    
    private static String QUERY_UPDATE = "UPDATE user SET " +
            " firstName = :FIRSTNAME, lastName = :LASTNAME, idrole = :IDROLE, displayName = :DISPLAYNAME, avatarUrl = :AVATARURL, location = :LOCATION where IDUSER = :IDUSER ";
    
    private static String QUERY_FIND_BY_ID = "SELECT * FROM user WHERE iduser = :IDUSER";
    
    @PostConstruct
    public void init(){
        setDataSource();
    }
    
    /**
     * Insert user on the database.
     * 
     * @param user
     * @return 
     */
    public User save(User user) {
        
        /* Check if user has a role, if there isn't, get the default role. */
        if ( user.getIdrole() == null ){
            try {
                user.setIdrole( roleRepository.getDefaultRoleId() );
            } catch ( SQLException ex ){
                ex.printStackTrace();
            }
        }
        
        MapSqlParameterSource params = getParamsInsert( user );

        this.jdbcTemplate.update(QUERY_SAVE, params);
        User u = null;

        /* Checks if the user has successfully been added to the database */
        try {
            u = getById( user.getIduser() );
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return u;
    }
    
    /**
     * Update a user registry
     * @param user
     * @return
     * @throws SQLException 
     */
    public User update( User user ) throws SQLException {
        
        /* Check if user has a role, if there isn't, get the default role. */
        if ( user.getIdrole() == null ){
            try {
                user.setIdrole( roleRepository.getDefaultRoleId() );
            } catch ( SQLException ex ){
                ex.printStackTrace();
            }
        }
        
        MapSqlParameterSource params = getParamsInsert( user );

        this.jdbcTemplate.update(QUERY_UPDATE, params);

        return user;
    }

    /* Defines the namedParameters used when inserting a user. */
    public MapSqlParameterSource getParamsInsert( User user ){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDUSER", user.getIduser() );
        params.addValue("FIRSTNAME", user.getFirstName() );
        params.addValue("LASTNAME", user.getLastName());
        params.addValue("IDROLE", user.getIdrole() );
        params.addValue("DISPLAYNAME", user.getDisplayName() );
        params.addValue("AVATARURL", user.getAvatarUrl() );
        params.addValue("LOCATION", user.getLocation() );

        return params;
    }

    /***
     * Get User by id.
     * 
     * @param idUser
     * @return
     * @throws SQLException 
     */
    public User getById(String idUser ) throws SQLException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("IDUSER", idUser);

        List<User> x = this.jdbcTemplate.query(QUERY_FIND_BY_ID, params, rowMapper() );

        return x != null && x.size() > 0 ? x.get(0) : null;
    }

    /**
     * Map the user from the database to the entity bean.
     * 
     * @return
     * @throws SQLException 
     */
    public RowMapper<User> rowMapper() throws SQLException {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User();

                u.setIduser( rs.getString("IDUSER") );
                u.setFirstName( rs.getString("FIRSTNAME") );
                u.setLastName( rs.getString("LASTNAME") );
                u.setDisplayName( rs.getString("DISPLAYNAME") );
                u.setAvatarUrl( rs.getString("AVATARURL") );
                u.setLocation( rs.getString("LOCATION") );
                u.setIdrole( rs.getLong("IDROLE") );

                return u;
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

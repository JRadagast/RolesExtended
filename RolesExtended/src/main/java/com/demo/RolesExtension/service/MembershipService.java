package com.demo.RolesExtension.service;

import com.demo.RolesExtension.beans.Membership;
import com.demo.RolesExtension.beans.User;
import com.demo.RolesExtension.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository repository;
    
    public ResponseEntity<List<Membership>> getMembershipsByRole(Long idrole){
        try {
            List<Membership> m = repository.getMembershipsByRole(idrole);

            ResponseEntity<List<Membership>> response = new ResponseEntity<List<Membership>>(m, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<List<Membership>> response = new ResponseEntity<List<Membership>>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

    public ResponseEntity<Membership>  getById(Long idMembership){
        try {
            Membership m = repository.getById(idMembership);

            ResponseEntity<Membership> response = new ResponseEntity<Membership>(m, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Membership> response = new ResponseEntity<Membership>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }

    /**
     * Tries to add membership, if membership already exist, update it.
     * @param membership
     * @return 
     */
    public ResponseEntity<Membership> save(Membership membership){
        try {
            Membership m = repository.getById(membership.getIdmembership());
            if (m != null){
                m = repository.update(membership);
            } else {
                m = repository.save(membership);
            }

            ResponseEntity<Membership> response = new ResponseEntity<Membership>(m, HttpStatus.OK);

            return response;
        } catch (SQLException ex){

            ResponseEntity<Membership> response = new ResponseEntity<Membership>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }
    
    /**
     * Insert all memberships for all users belonging to a team.
     * @param users
     * @param teamId
     * @return 
     */
    public ResponseEntity<List<Membership>> saveAll(List<String> users, String teamId){
        List<Membership> addedMembership = new ArrayList<>();
        
        try {
            if (users != null && users.size() > 0){
                Membership m = null;

                for (String userId : users){
                    m = new Membership();
                    
                    m.setIduser( userId );
                    m.setIdteam( teamId );

                    ResponseEntity<Membership> resp = this.save(m);
                    if (resp.getStatusCode() == HttpStatus.OK){
                        addedMembership.add( resp.getBody() ); 
                    } else {
                        throw new SQLException();
                    }
                    
                }

            }

            ResponseEntity<List<Membership>> response = new ResponseEntity<List<Membership>>( addedMembership, HttpStatus.OK );
            return response;
        } catch (SQLException ex){
            
            ResponseEntity<List<Membership>> response = new ResponseEntity<List<Membership>>(HttpStatus.INTERNAL_SERVER_ERROR);

            return response;
        }
    }


}

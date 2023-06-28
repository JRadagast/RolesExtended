package com.demo.RolesExtension.beans;

/**
 *
 * @author JRadagast
 */
//@Entity
public class Membership {
    
    private Long idmembership;
    private String idteam;
    private String iduser;

    public Long getIdmembership() {
        return idmembership;
    }

    public void setIdmembership(Long idmembership) {
        this.idmembership = idmembership;
    }

    public String getIdteam() {
        return idteam;
    }

    public void setIdteam(String idteam) {
        this.idteam = idteam;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

}

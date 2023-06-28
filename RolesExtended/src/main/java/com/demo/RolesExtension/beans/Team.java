package com.demo.RolesExtension.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author JRadagast
 */
//@Entity
public class Team {

    @JsonProperty("id")
    private String idteam;
    
    private String teamLeadId;
    
    private String name;
    
    private List<String> teamMemberIds;

    public String getIdteam() {
        return idteam;
    }

    public void setIdteam(String idteam) {
        this.idteam = idteam;
    }

    public String getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(String teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTeamMemberIds() {
        return teamMemberIds;
    }

    public void setTeamMemberIds(List<String> teamMemberIds) {
        this.teamMemberIds = teamMemberIds;
    }

}

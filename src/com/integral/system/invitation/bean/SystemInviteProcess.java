package com.integral.system.invitation.bean;
// Generated Dec 23, 2011 3:27:11 PM by Hibernate Tools 3.4.0.CR1


import java.util.Date;

/**
 * SystemInviteProcess generated by hbm2java
 */
public class SystemInviteProcess  implements java.io.Serializable {


     private String id;
     private String sponsor;
     private String recipient;
     private Date sponsorTime;
     private Date processTime;
     private String processStatus;
     private String invitationMenu;
     private String invitationMenuName;
     private String processResultCode;
     private String invitationEvent;
     private String invitationReason;
     private String nextaction;
     private String relationData;
     private String relationEntityName;

    public SystemInviteProcess() {
    }

	
    public SystemInviteProcess(String id) {
        this.id = id;
    }
    public SystemInviteProcess(String id, String sponsor, String recipient, Date sponsorTime, Date processTime, String processStatus, String invitationMenu, String processResultCode, String invitationEvent, String invitationReason, String nextaction, String invitationMenuName, String relationData, String relationEntityName) {
       this.id = id;
       this.sponsor = sponsor;
       this.recipient = recipient;
       this.sponsorTime = sponsorTime;
       this.processTime = processTime;
       this.processStatus = processStatus;
       this.invitationMenu = invitationMenu;
       this.invitationMenuName = invitationMenuName;
       this.processResultCode = processResultCode;
       this.invitationEvent = invitationEvent;
       this.invitationReason = invitationReason;
       this.nextaction = nextaction;
       this.relationData = relationData;
       this.relationEntityName = relationEntityName;
    }
   
    public String getRelationEntityName() {
        return relationEntityName;
    }


    public void setRelationEntityName(String relationEntityName) {
        this.relationEntityName = relationEntityName;
    }


    public String getRelationData() {
        return relationData;
    }


    public void setRelationData(String relationData) {
        this.relationData = relationData;
    }


    public String getInvitationMenuName() {
        return invitationMenuName;
    }


    public void setInvitationMenuName(String invitationMenuName) {
        this.invitationMenuName = invitationMenuName;
    }


    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getSponsor() {
        return this.sponsor;
    }
    
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }
    public String getRecipient() {
        return this.recipient;
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public Date getSponsorTime() {
        return this.sponsorTime;
    }
    
    public void setSponsorTime(Date sponsorTime) {
        this.sponsorTime = sponsorTime;
    }
    public Date getProcessTime() {
        return this.processTime;
    }
    
    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }
    public String getProcessStatus() {
        return this.processStatus;
    }
    
    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
    public String getInvitationMenu() {
        return this.invitationMenu;
    }
    
    public void setInvitationMenu(String invitationMenu) {
        this.invitationMenu = invitationMenu;
    }
    public String getProcessResultCode() {
        return this.processResultCode;
    }
    
    public void setProcessResultCode(String processResultCode) {
        this.processResultCode = processResultCode;
    }
    public String getInvitationEvent() {
        return this.invitationEvent;
    }
    
    public void setInvitationEvent(String invitationEvent) {
        this.invitationEvent = invitationEvent;
    }
    public String getInvitationReason() {
        return this.invitationReason;
    }
    
    public void setInvitationReason(String invitationReason) {
        this.invitationReason = invitationReason;
    }
    public String getNextaction() {
        return this.nextaction;
    }
    
    public void setNextaction(String nextaction) {
        this.nextaction = nextaction;
    }




}



package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;


@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
	
	@Autowired
	MeetingService meetingService;
	@Autowired
	ParticipantService  participantService;
//	MeetingParticipantService meetingParticipantService;


	
	//LISTA SPOTKAN
	//curl -H "Content-Type: application/json"  localhost:8080/meetings
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	
	// SPOTKANIE po id
	//curl -H "Content-Type: application/json"  localhost:8080/meetings/3
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
	     Meeting meeting = meetingService.findById(id);
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	
	
	//DODANIE SPOTKANIA
	//curl -H "Content-Type: application/json" -d '{"title":"spotkanienowe", "description": "desc", "date": "Monday"}' localhost:8080/meetings/
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting){
    	Meeting foundMeeting = meetingService.findById(meeting.getId());
    	
    	if (foundMeeting != null) {
    			
        return new ResponseEntity("Unable to create. A meeting with id " + 
        meeting.getId() + " already exist.", HttpStatus.CONFLICT);
    	}
    	
    	meetingService.add(meeting);
    	return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }
    
    
    //USUNIECIE SPOTKANIA
    //curl -X DELETE -H "Content-Type: application/json" localhost:8080/meetings/11/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
	     Meeting meeting = meetingService.findById(id);
	     

    	if (meeting == null) {
    			
        return new ResponseEntity("Unable to delette. A meeting with id " + 
        id + " doesnt exist.", HttpStatus.CONFLICT);
    	}
    	
    	meetingService.delete(meeting);
    	return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }
    

    //DODANIE UCZESTNIKA DO SPOTKANIA
    // curl -H "Content-Type: application/json" -d '{"login":"user5", "password":"passsword"}' localhost:8080/meetings/3/
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addMeetingParticipant(@RequestBody Participant par, @PathVariable("id") long id){
    	Meeting meeting = meetingService.findById(id);
    	
//    	MeetingParticipant meetingPar = new MeetingPar(participant.getLogin(), meeting.getId());

    	if (meeting == null) {
			
    		return new ResponseEntity(HttpStatus.NOT_FOUND);
    	}
    	
    	
    	for (Participant p : meeting.getParticipants()) {
    		
    		if (par.equals(p)) {

    		return new ResponseEntity(HttpStatus.CONFLICT);
    		}
    	}
    	


    	meeting.addParticipant(par);
//        meetingParticipantService.addPar(par, meeting);
    	
    	return new ResponseEntity<Participant>(par, HttpStatus.OK);
    }
    
    
    //MODYFIKACJA SPOTKANIA
    //curl -X PUT -H "Content-Type: application/json" -d '{"title":"zmiana", "description": "desc", "date": "xxMonday"}' localhost:8080/meetings/9/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> changeMeeting(@PathVariable("id") long id,
			@RequestBody Meeting incomingMeeting){
    	
	Meeting meeting = meetingService.findById(id);
	
	if (meeting == null) {
			
    return new ResponseEntity("Unable to change. A meeting with login " + meeting.getId() + " does not exist.", HttpStatus.CONFLICT);
	}
	
	meeting.setDescription(incomingMeeting.getDescription());
	meeting.setTitle(incomingMeeting.getTitle());
	meeting.setDate(incomingMeeting.getDate());
	meetingService.updateMeeting(meeting);
	
	return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }
    
    
    //LISTA UCZESNIKOW
    //curl -H "Content-Type: application/json" localhost:8080/meetings/3/participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
	     Meeting meeting = meetingService.findById(id);
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     Collection<Participant> participants = meeting.getParticipants();
	     return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);

	 }
	
	
	
	//USUNIECIE UCZESTNIKA ZE SPOTKANIA
	//curl -X DELETE -H "Content-Type: application/json" http://localhost:8080/meetings/3/participants/user5
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeetingParticipants(@PathVariable("id") long id, @PathVariable("login") String login) {
		
	     Meeting meeting = meetingService.findById(id);
	     Participant participant = participantService.findByLogin(login);
	     
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     
	    	
	     meeting.removeParticipant(participant);
	     Collection<Participant> participants = meeting.getParticipants();
	     return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);

	 }
	
	
	
}

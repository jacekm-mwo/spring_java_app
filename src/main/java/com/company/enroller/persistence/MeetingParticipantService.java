package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.Participant;

@Component("meetingParticipantService")
public class MeetingParticipantService {

	DatabaseConnector connector;

	public MeetingParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<?> getAll() {
		String hql = "FROM MeetingParticipant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public void add(MeetingParticipant mp) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(mp);
		transaction.commit();
		
	}
	public void addPar(Participant par, Meeting meet) {
		
		String hql = "insert into meeting_participant (meeting_id, participant_login) select :login, :meet"; 
		Query query = connector.getSession().createQuery(hql);
	    query.setParameter("login", par.getLogin());
	    query.setParameter("meet", meet.getId());

	}
	
	
	
}

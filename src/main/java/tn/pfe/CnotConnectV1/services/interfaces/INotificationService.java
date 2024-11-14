package tn.pfe.CnotConnectV1.services.interfaces;

import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.User;

public interface INotificationService {

	void notifyUsers(User recipient, String subject, String message);

	void sendNotification(User user, String message);

}

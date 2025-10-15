package ar.edu.iua.iw3.auth.events;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import ar.edu.iua.iw3.auth.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserEventListener implements ApplicationListener<UserEvent> {

	@Override
	public void onApplicationEvent(UserEvent event) {
		if (event.getTypeEvent().equals(UserEvent.TypeEvent.LOGIN)) {
			handleLogin(event);
		}
		if (event.getTypeEvent().equals(UserEvent.TypeEvent.SEND_EMAIL_WITH_PASSWORD_RESTORED)) {
			handleSendMailWithPasswordRestored(event);
		}
		if (event.getTypeEvent().equals(UserEvent.TypeEvent.SEND_EMAIL_WITH_USERNAME)) {
			handleSendMailWithUsername(event);
		}
	}

	private void handleLogin(UserEvent event) {
		User user = (User) event.getSource();
		HttpServletRequest request=(HttpServletRequest) event.getExtraData();
		log.debug("Evento LOGIN user: '{}', host={}", user.getUsername(), request.getRemoteHost());
	}



	private void handleSendMailWithPasswordRestored(UserEvent event) {
		User user = (User) event.getSource();
		log.debug("Evento SEND_EMAIL_WITH_PASSWORD_RESTORED user: {}", user.getUsername());
	}

	private void handleSendMailWithUsername(UserEvent event) {
		User user = (User) event.getSource();
		log.debug("Evento SEND_EMAIL_WITH_USERNAME user: {}", user.getUsername());
	}
}

package org.corodiak.kcss1devt1auth.service;

public interface EmailService {

	void sendEmailValidation(String name, String receiver, String code);

	boolean verifyEmail(String code);

}

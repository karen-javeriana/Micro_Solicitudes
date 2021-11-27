package com.solicitudes.dto;

public class CorreoGenericoDto {

	private String title;

	private String description;

	private String subject;

	private String template;

	private String email;

	private String link;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public CorreoGenericoDto(String title, String description, String subject, String template, String email,
			String link) {
		super();
		this.title = title;
		this.description = description;
		this.subject = subject;
		this.template = template;
		this.email = email;
		this.link = link;
	}

}

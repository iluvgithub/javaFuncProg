package com.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * https://javatutorial.net/java-servlet-example
 *
 */
public class HelloWorldServlet extends HttpServlet {
	private static final long serialVersionUID = -4751096228274971485L;

    final static Logger logger = LogManager.getLogger(HelloWorldServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("received " + request);
		response.getWriter().println(hello());		
	}

	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
	}

	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}

	private static String hello() {
		return "<!doctype html>" + //
				"<html lang=\"fr\">" + //
				"<head>" + //
				"<meta charset=\"utf-8\">" + //
				"<title>Titre de la page</title>" + //
				"</head>" + //
				"<body>" + //
				"<h1>" + //
				"Hello New World" + //
				"</h1>" + //
				"</body>" + //
				"</html>";
	}
}
package com.artigem.contents.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artigem.item.comparable.AmazonSearchResult;

/**
 * Servlet implementation class ContentsServlet
 */
@WebServlet("/ContentsServlet")
public class ContentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ContentsServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ContentsServlet.doGet()");
		// TODO Auto-generated method stub
		handleRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("ContentsServlet.doPost()");
		// TODO Auto-generated method stub
		handleRequest(request, response);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("ContentsServlet.handleRequest()");
		//response.getWriter().append("Served at: ").append(request.getContextPath());

		PrintWriter out = response.getWriter();

		response.setContentType("text/plain");

		String searchKey = request.getParameter("searchKey");
		System.out.println("searchKey >> " + searchKey);

		
		if(searchKey != null){
		    out.write(new AmazonSearchResult().getResultsFromAmazon(searchKey));
		}
		
		out.close();

	}

}

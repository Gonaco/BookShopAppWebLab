package servlets;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/carro_compra")

    public class Carro extends HttpServlet {

	private List<Book> compras = new ArrayList<Book>();



	// private int id;

	// public String getTitle() {
	//  return title;
	// }

	// public void setTitle(String title) {
	//  this.title = title;
	// }

	// public String getIsbn() {
	//  return isbn;
	// }

	// public void setIsbn(String isbn) {
	//  this.isbn = isbn;
	// }

	// public int getYear() {
	//  return year;
	// }

	// public void setYear(int year) {
	//  this.year = year;
	// }

	// public int getId() {
	//  return id;
	// }

	// public void setId(int id) {
	//  this.id = id;
	// }

	// public String toString() {
	//  return isbn + " " + title + " (" + year + ")";
	// }

        /**
	 * Método del servlet que responde a una petición GET.
	 *
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException
	{
	    // establece ContentType y sistema de codificación de caracteres
	    response.setContentType("text/html; charset=UTF-8");

	    // obtiene un PrintWriter para escribir la respuesta
	    // PrintWriter out = response.getWriter();

	}


    }


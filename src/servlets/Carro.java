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

	private List<Book> compras;
	private HttpSession user_session;
	
	public void createCarro (HttpServletRequest request){
		//Iniciar o comprobar Sesi�n?
		compras = new ArrayList<Book>();
	}

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


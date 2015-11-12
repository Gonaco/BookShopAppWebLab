package servlets;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBManager implements AutoCloseable {

	private Connection connection;

	public DBManager() throws SQLException, NamingException {
		connect();
	}

	private void connect() throws SQLException, NamingException {
		// TODO: program this method

		// String url = "jdbc:mysql://mysql.lab.it.uc3m.es/16_compweb_24b";
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/Libros");
		// connection = DriverManager.getConnection(url,
		// "16_compweb_24","FTgv7f2B");
		connection = ds.getConnection();

	}

	/**
	 * Close the connection to the database if it is still open.
	 *
	 */
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
		connection = null;
	}

	/**
	 * Return the number of units in stock of the given book.
	 *
	 * @param book
	 *            The book object.
	 * @return The number of units in stock, or 0 if the book does not exist in
	 *         the database.
	 * @throws SQLException
	 *             If somthing fails with the DB.
	 */
	public int getStock(Book book) throws SQLException {
		return getStock(book.getId());
	}

	/**
	 * Return the number of units in stock of the given book.
	 *
	 * @param bookId
	 *            The book identifier in the database.
	 * @return The number of units in stock, or 0 if the book does not exist in
	 *         the database.
	 */
	public int getStock(int bookId) throws SQLException {
		// TODO: program this method DONE
		int stock = 0;
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery("SELECT libros_almacenados FROM Stock WHERE id_libro=" + bookId);
			if (rs.next()) {
				stock = rs.getInt("libros_almacenados");
			}

		}
		return stock;
	}

	/**
	 * Search book by ISBN.
	 *
	 * @param isbn
	 *            The ISBN of the book.
	 * @return The Book object, or null if not found.
	 * @throws SQLException
	 *             If somthing fails with the DB.
	 */
	public Book searchBook(String isbn) throws SQLException {
		// TODO: program this method DONE
		Book libro;
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(
					"SELECT BooksDB.title, BooksDB.year, BooksDB.id, Authors.autor FROM BooksDB INNER JOIN Authors INNER JOIN BookAuthor ON BookAuthor.id_autor=Authors.id AND BooksDB.id=BookAuthor.id_libro WHERE BooksDB.ISBN='"
							+ isbn + "'");

			// while (rs.next()){
			// String title = rs.getString("BooksDB.title");
			// String author = rs.getString("Authors.autor");
			// int id = rs.getInt("BooksDB.id");
			// int year = rs.getDate("BooksDB.year");
			// }

			// Se podría considerar mayor complejidad comprobando que rs,
			// efectivamente, no sea una lista si no una
			// única fila

			if (rs.next()) { // Tenemos que recordar que el primer valor inicial
								// que devuelve executeQuery no es válido
				libro = new Book();
				String title = rs.getString("BooksDB.title");
				String author = rs.getString("Authors.autor");
				int id = rs.getInt("BooksDB.id");
				int year = rs.getInt("BooksDB.year");
				libro.setTitle(title);
				libro.setIsbn(isbn);
				libro.setYear(year);
				libro.setId(id);
			} else {
				libro = null;
			}
		}
		return libro;
	}

	/**
	 * Sell a book.
	 *
	 * @param book
	 *            The book.
	 * @param units
	 *            Number of units that are being sold.
	 * @return True if the operation succeeds, or false otherwise (e.g. when the
	 *         stock of the book is not big enough).
	 * @throws SQLException
	 *             If somthing fails with the DB.
	 */
	public boolean sellBook(Book book, int units) throws SQLException {
		return sellBook(book.getId(), units);
	}

	/**
	 * Sell a book.
	 *
	 * @param book
	 *            The book's identifier.
	 * @param units
	 *            Number of units that are being sold.
	 * @return True if the operation succeeds, or false otherwise (e.g. when the
	 *         stock of the book is not big enough).
	 * @throws SQLException
	 *             If something fails with the DB.
	 */
	public boolean sellBook(int book, int units) throws SQLException {
		// TODO: program this method
		boolean success = false;
		int stock = getStock(book);
		if (stock > 0) {

			// Preparamos la conexión de forma adecuada, controlando los
			// commits y haciéndola segura

			connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			connection.setAutoCommit(false);

			// Otro grado de complejidad para seguridad con SQL Injection

			// try(PreparedStatement pstmt = connection.prepareStatement("")){
			// //INSERT venta

			// //pstmt.set

			// }
			// try(PreparedStatement pstmt = connection.prepareStatement("")){
			// //UPDATE stock

			// //pstmt.set

			// }

			try (Statement stmt = connection.createStatement()) {

				int changes = stmt.executeUpdate(
						"UPDATE Stock SET libros_almacenados=libros_almacenados-" + units + " WHERE id_libro=" + book); // UPDATE
																														// stock

				stmt.executeUpdate(
						"INSERT TO Sells (fecha_hora, id_libro, cantidad) VALUES (NOW(), " + book + ", " + units + ")"); // INSERT
																															// venta

				if (changes > 0)
					success = true;

			} finally {

				if (success)
					connection.commit();
				else
					connection.rollback();

			}
			connection.setAutoCommit(true);
		}

		return success;
	}

	/**
	 * Return a list with all the books in the database.
	 *
	 * @return List with all the books.
	 * @throws SQLException
	 *             If something fails with the DB.
	 */
	public List<Book> listBooks() throws SQLException {
		// TODO: program this method DONE
		List<Book> libros = new ArrayList<Book>();
		try (Statement stmt = connection.createStatement()) {
			String query = "SELECT id, title, isbn, year FROM BooksDB";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Book nodo = new Book();
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String isbn = rs.getString("isbn");
				int year = rs.getInt("year");

				nodo.setTitle(title);
				nodo.setIsbn(isbn);
				nodo.setYear(year);
				nodo.setId(id);

				if (!libros.add(nodo)) {
					throw new SQLException();
				}
			}
		}
		return libros;
	}
}

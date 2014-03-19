package exceptions;

import java.sql.SQLException;

public class CategorieException extends SQLException {

	public CategorieException() {
		// TODO Auto-generated constructor stub
	}

	public CategorieException (String m) {
		super(m);
	}
}

package com.packt.cookbook.libraries.common;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Вспомогательный класс для БД
 */
public class DatabaseHelper {

	/**
	 * Создание источника данных (DataSource) по jndi имени
	 *
	 * @param jndiName jndi имя
	 * @return DataSource
	 * @throws NamingException
	 */
	public static DataSource createDataSource(String jndiName) throws NamingException {
		InitialContext initialCtx;
		DataSource dataSource = null;

		if (jndiName != null) {
			initialCtx = new InitialContext();
			try {
				dataSource = (DataSource) initialCtx.lookup(jndiName);
			} catch (NamingException e) {
				dataSource = (DataSource) initialCtx.lookup("java:" + jndiName);
			}
		}
		return dataSource;
	}

}

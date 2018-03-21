/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

/**
 * @author fdelom
 *
 */
public class TestHelper {
	public static void resetAutoIncrementColumns(ApplicationContext applicationContext, String... tableNames)
			throws SQLException {
		DataSource dataSource = applicationContext.getBean(DataSource.class);
		String resetSqlTemplate = "ALTER SEQUENCE %s_id_seq RESTART WITH 1";
		try (Connection dbConnection = dataSource.getConnection()) {
			for (String resetSqlArgument : tableNames) {
				try (Statement statement = dbConnection.createStatement()) {
					String resetSql = String.format(resetSqlTemplate, resetSqlArgument);
					statement.execute(resetSql);
				}
			}
		}
	}
}

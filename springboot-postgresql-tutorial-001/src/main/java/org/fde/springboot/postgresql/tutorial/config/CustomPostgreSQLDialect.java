/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.config;

import java.sql.Types;

import org.fde.springboot.postgresql.tutorial.model.extension.JsonbParameterizedType;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.springframework.context.annotation.Configuration;

/**
 * @author fdelom
 *
 */
@Configuration
public class CustomPostgreSQLDialect extends PostgreSQL94Dialect {

	public CustomPostgreSQLDialect() {
		super();
		registerColumnType(Types.JAVA_OBJECT, JsonbParameterizedType.JSONB);
	}
}

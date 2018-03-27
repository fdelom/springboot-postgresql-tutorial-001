/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.extension;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 * @author fdelom
 *
 */
@Converter(autoApply = true)
@Component
public class LocalDateConverter implements AttributeConverter<LocalDate, Date>, Formatter<LocalDate> {

	@Override
	public Date convertToDatabaseColumn(LocalDate locDate) {
		return (locDate == null ? null : Date.valueOf(locDate));
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return (sqlDate == null ? null : sqlDate.toLocalDate());
	}

	@Override
	public String print(LocalDate object, Locale local) {
		return DateTimeFormatter.ISO_DATE.format(object);
	}

	@Override
	public LocalDate parse(String text, Locale local) throws ParseException {
		return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
	}
}

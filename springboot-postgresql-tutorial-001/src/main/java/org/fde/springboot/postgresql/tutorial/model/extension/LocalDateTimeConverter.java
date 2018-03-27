/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.extension;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
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
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>, Formatter<LocalDateTime> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
		return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
		return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
	}

	@Override
	public String print(LocalDateTime object, Locale local) {
		return DateTimeFormatter.ISO_DATE_TIME.format(object);
	}

	@Override
	public LocalDateTime parse(String text, Locale local) throws ParseException {
		return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
	}
}

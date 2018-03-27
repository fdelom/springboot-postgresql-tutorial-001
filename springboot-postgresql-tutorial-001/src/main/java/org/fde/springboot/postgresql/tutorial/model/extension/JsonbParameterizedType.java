/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.fde.springboot.postgresql.tutorial.exception.ConversionJsonbFailedException;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author fdelom
 *
 */
public class JsonbParameterizedType implements ParameterizedType, UserType {

	private static final ClassLoaderService classLoaderService = new ClassLoaderServiceImpl();

	public static final String JSONB_TYPE = "jsonb";
	public static final String CLASS = "CLASS";

	private Class<?> jsonClassType;

	@Override
	public Class<Object> returnedClass() {
		return Object.class;
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { Types.JAVA_OBJECT };
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		final String cellContent = rs.getString(names[0]);
		if (cellContent == null) {
			return null;
		}
		try {
			Gson gson = new GsonBuilder().create();
			return gson.fromJson(cellContent, jsonClassType);
		} catch (final Exception ex) {
			throw new ConversionJsonbFailedException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.OTHER);
			return;
		}
		try {
			Gson gson = new GsonBuilder().create();
			st.setObject(index, gson.toJson(value), Types.OTHER);
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
		}
	}

	@Override
	public void setParameterValues(Properties parameters) {
		final String clazz = (String) parameters.get(CLASS);
		jsonClassType = classLoaderService.classForName(clazz);
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objOutputStream.writeObject(value);
			objOutputStream.flush();
			objOutputStream.close();
			byteArrayOutputStream.close();

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			return new ObjectInputStream(byteArrayInputStream).readObject();
		} catch (IOException | ClassNotFoundException ex) {
			throw new HibernateException(ex);
		}
	}

	static final class CollectionFactory {
		@SuppressWarnings("unchecked")
		static <E, T extends Collection<E>> T newInstance(Class<T> collectionClass) {
			if (List.class.isAssignableFrom(collectionClass)) {
				return (T) new ArrayList<E>();
			} else if (Set.class.isAssignableFrom(collectionClass)) {
				return (T) new HashSet<E>();
			} else {
				throw new IllegalArgumentException("Unsupported collection type : " + collectionClass);
			}
		}

	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}

		if ((x == null) || (y == null)) {
			return false;
		}

		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		assert(x != null);
		return x.hashCode();
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deepCopy(cached);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		Object deepCopy = deepCopy(value);

		if (!(deepCopy instanceof Serializable)) {
			throw new SerializationException(String.format("%s is not serializable class", value), null);
		}

		return (Serializable) deepCopy;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}
}

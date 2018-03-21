/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author fdelom
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TodoDetails implements Serializable {

	private static final long serialVersionUID = -3293244556415506952L;

	@SerializedName(value = "description")
	private String description;

	@SerializedName(value = "tags")
	private ArrayList<String> tags;
}

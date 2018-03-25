/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.model.audit;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author fdelom
 *
 */
@Component
public class ThreadLocalCurrentAuditor implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		return (String) RequestContextHolder.currentRequestAttributes().getAttribute("Username", SCOPE_REQUEST);
	}
}
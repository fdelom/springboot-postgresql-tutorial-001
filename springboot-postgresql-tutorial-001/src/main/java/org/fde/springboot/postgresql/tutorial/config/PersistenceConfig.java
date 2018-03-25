/**
 * 
 */
package org.fde.springboot.postgresql.tutorial.config;

import org.fde.springboot.postgresql.tutorial.model.audit.ThreadLocalCurrentAuditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author fdelom
 *
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {

	@Bean
	AuditorAware<String> auditorProvider() {
		return new ThreadLocalCurrentAuditor();
	}

}

package com.erretechnology.intranet;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/*
 *  L'applicazione usa diversi thread per le funzioni che richiedono accessi multipli al DB
 *  in modo da velocizzare il caricamento delle pagine. 
 *  Qui è riportata la configurazione per il task
 */



@Configuration
@EnableAsync
public class AsyncConfiguration {
	
	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(100);
		executor.setThreadGroupName("TheGateThread-");
		executor.initialize();
		return executor;
	}
}

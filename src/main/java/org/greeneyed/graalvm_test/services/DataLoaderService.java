package org.greeneyed.graalvm_test.services;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.greeneyed.epl.librarian.services.EplCSVProcessor;
import org.greeneyed.epl.librarian.services.EplCSVProcessor.UpdateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Data
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class DataLoaderService implements ApplicationRunner, EnvironmentAware {
	private Environment environment;

	private final EplCSVProcessor eplCSVProcessor;

	private final BibliotecaService bibliotecaService;

	@Override
	public void run(ApplicationArguments arguments) throws Exception {
		log.info("Reading data...");
		StopWatch timeMeasure = new StopWatch();
		timeMeasure.start("Processing data");
		UpdateSpec updateSpec = eplCSVProcessor.processBackup();
		//
		if (updateSpec.isEmpty()) {
			throw new IOException("Sin datos, para qu\u00e9 arrancar.");
		} else {
			log.info("Preparando {} libros de la descarga con fecha {}", updateSpec.getLibroCSVs().size(),
					DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(updateSpec.getFechaActualizacion()));
			bibliotecaService.update(updateSpec);
			timeMeasure.stop();
			log.info("Test finished in ~{}s", (int)timeMeasure.getTotalTimeSeconds());
		}
	}
}

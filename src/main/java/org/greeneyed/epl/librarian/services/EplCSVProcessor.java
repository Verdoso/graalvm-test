package org.greeneyed.epl.librarian.services;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Service
@Slf4j
public class EplCSVProcessor {

	@Value("${superportable:false}")
	private boolean superPortable;

	private static final String ERROR_DETALLADO = "Error detallado";

	@Data
	public static class LibroCSV implements Serializable {

		private static final long serialVersionUID = 1L;

		private static final Pattern IMGUR_IMAGE_PATTERN = Pattern.compile("https?://(?:i.)?imgur.com/(.+)\\.jpg",
				Pattern.CASE_INSENSITIVE);
		private static final DateTimeFormatter PUBLICADO_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		private int id;

		private String titulo;

		private String autor;

		private String generos;

		private String coleccion;

		public String getColeccion() {
			return StringUtils.isBlank(coleccion) ? "" : coleccion;
		}

		private BigDecimal volumen;

		private int anyoPublicacion;

		private String sinopsis;

		private Integer paginas;

		private BigDecimal revision;

		private String idioma;

		private String publicado;

		private String estado;

		private BigDecimal valoracion;

		private Integer votos;

		private String magnetId;

		private String portadaCompleta;

		public String getPortada() {
			String result = null;
			if (portadaCompleta != null) {
				Matcher matcher = IMGUR_IMAGE_PATTERN.matcher(portadaCompleta);
				if (matcher.find()) {
					result = matcher.group(1);
				} else {
					result = portadaCompleta;
				}
			}
			return result;
		}

		public LocalDate getFechaPublicacion() {
			LocalDate temp = null;
			try {
				if (StringUtils.isNotBlank(publicado)) {
					temp = LocalDate.parse(publicado.substring(publicado.indexOf(":") + 1), PUBLICADO_FORMATTER);
				}
			} catch (Exception e) {
				log.error("Error parseando fecha de publicacion {}", publicado, e.getMessage());
				log.trace(ERROR_DETALLADO, e);
			}
			return temp;
		}
	}

	@Data
	public static class UpdateSpec {
		public static final UpdateSpec NO_SPEC = new UpdateSpec(null, Collections.emptyList());

		private final LocalDateTime fechaActualizacion;
		private final List<LibroCSV> libroCSVs;

		public boolean isEmpty() {
			return libroCSVs == null || libroCSVs.isEmpty();
		}

		public Duration getAntiguedad() {
			return Duration.ofHours(fechaActualizacion == null ? Integer.MAX_VALUE
					: fechaActualizacion.until(LocalDateTime.now(), ChronoUnit.HOURS));
		}
	}

	public UpdateSpec processBackup() {
		File file = new File(".." + File.separator + "test.ser");
		log.warn("Reading file: {}", file.getAbsolutePath());
		LocalDateTime fechaActualizacion = Instant.ofEpochMilli(file.lastModified())
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		List<LibroCSV> librosCSVs = new ArrayList<>();
		try (FileInputStream theFIS = new FileInputStream(file);
				BufferedInputStream theBIS = new BufferedInputStream(theFIS);
				ObjectInputStream theOIS = new ObjectInputStream(theBIS)) {
			LibroCSV libroCSV;
			while ((libroCSV = (LibroCSV) theOIS.readObject()) != null) {
				librosCSVs.add(libroCSV);
			}
		} catch (EOFException e) {
			log.debug("EOF to signal no more, that's how ObjectInputStream works");
		} catch (Exception e) {
			log.error("Error leyendo fichero de backup", e);
			librosCSVs.clear();
		}
		log.warn("Backup Le\u00eddo con {} libros.", librosCSVs.size());
		return new UpdateSpec(fechaActualizacion, librosCSVs);
	}
}

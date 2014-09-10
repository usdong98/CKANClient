package com.sayit.utils.convert.csv;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

public class CSV2JSON extends AbstractCSV {

	public void toJson(List<String[]> lines, OutputStream out) {
		if (lines.size() <= 1) {
			return;
		}
		final String[] header = lines.get(0);

		final Map<String, Object> properties = new HashMap<String, Object>();
		final JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
		final JsonGenerator jg = jgf.createGenerator(out);
		try {
			jg.writeStartObject();

			jg.writeStartArray("headers");
			for (String string : header) {
				jg.write(string);
			}
			jg.writeEnd();

			jg.writeStartArray("rows");
			for (int j = 1; j < lines.size(); j++) {
				final String[] line = lines.get(j);
				jg.writeStartObject();
				for (int i = 0; i < line.length; i++) {
					jg.write(header[i], line[i]);
				}
				jg.writeEnd();

			}
			jg.writeEnd();

			jg.writeEnd();
		} finally {
			jg.close();
		}
	}

	public void transform(InputStreamReader in , OutputStream out) throws IOException {
		toJson(readAll(in), out);
		
	}

	public static void main(String[] args) throws IOException {
		new CSV2JSON().transform(System.in, System.out);
		System.out.println();
	}

}

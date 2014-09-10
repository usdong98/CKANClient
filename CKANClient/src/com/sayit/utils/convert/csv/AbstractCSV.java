package com.sayit.utils.convert.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.text.StrTokenizer;

public abstract class AbstractCSV {

	private final StrTokenizer token = StrTokenizer.getCSVInstance();

	public String[] parseLine(String line) {
		token.reset(line);
		final String[] row = new String[token.size()];
		for (int i = 0; i < row.length; i++) {
			row[i] = (String) token.next();
		}
		return row;
	}

	public List<String[]> readAll(final BufferedReader reader) throws IOException {
		final List<String[]> list = new ArrayList<String[]>(8);
		String line;
		while ((line = reader.readLine()) != null) {
			list.add(parseLine(line));
		}
		return list;
	}

	public List<String[]> readAll(final Reader reader) throws IOException {
		return readAll(new BufferedReader (reader));
	}

	public void transform (InputStream in , OutputStream out) throws IOException {
		transform(new InputStreamReader(in), out);
	}

	abstract void transform(InputStreamReader in, OutputStream out) throws IOException;

}

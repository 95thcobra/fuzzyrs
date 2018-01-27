package com.rs.utils.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WebPage {

    private final URL url;
    private ArrayList<String> lines;

    public WebPage(String url) throws MalformedURLException {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        this.url = new URL(url);
    }

    public void load() throws IOException {
        lines = new ArrayList<String>();
        final URLConnection c = url.openConnection();
        c.setReadTimeout(3000);
        final BufferedReader stream = new BufferedReader(new InputStreamReader(
                c.getInputStream()));
        String line;
        while ((line = stream.readLine()) != null) {
            lines.add(line);
        }
        stream.close();

    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(final ArrayList<String> lines) {
        this.lines = lines;
    }
}

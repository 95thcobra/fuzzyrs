package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

public class RuneWikiExaminesDumper {

    public static void main(final String[] args) throws IOException {
        System.out.println("Starting..");
        Cache.init();
        for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
            if (!ItemDefinitions.getItemDefinitions(itemId).isNoted())
                if (dumpItem(itemId)) {
                    System.out.println("DUMPED ITEM : " + itemId);
                } else {
                    System.out.println("FAILED ITEM: "
                            + itemId
                            + ", "
                            + ItemDefinitions.getItemDefinitions(itemId)
                            .getName());
                }
        }
    }

    public static boolean dumpItem(final int itemId) {
        String pageName = ItemDefinitions.getItemDefinitions(itemId).getName();
        if (pageName == null || pageName.equals("null"))
            return false;
        pageName = pageName.replace("(p)", "");
        pageName = pageName.replace("(p+)", "");
        pageName = pageName.replace("(p++)", "");
        pageName = pageName.replaceAll(" ", "_");
        try {
            final WebPage page = new WebPage("http://runescape.wikia.com/wiki/"
                    + pageName);
            try {
                page.load();
            } catch (final Exception e) {
                System.out.println("Invalid page: " + itemId + ", " + pageName);
                return false;
            }
            boolean isNextLine = false;
            for (final String line : page.getLines()) {
                if (!isNextLine) {
                    if (line.equals("<th nowrap=\"nowrap\"><a href=\"/wiki/Examine\" title=\"Examine\">Examine</a>")) {
                        isNextLine = true;
                    }
                } else {
                    String examine = line.replace("</th><td> ", "");
                    examine = examine.replace("</th><td>", "");
                    examine = examine.replace("<i> ", "");
                    examine = examine.replace("</i> ", "");
                    examine = examine.replace("&lt;colour&gt; ", "");
                    examine = examine.replace("(bright/thick/warm)", "bright");
                    examine = examine.replace("(Temple of Ikov) ", "");
                    examine = examine.replace("(Fight Arena) ", "");
                    try {
                        final BufferedWriter writer = new BufferedWriter(
                                new FileWriter("itemExamines.txt", true));
                        writer.write(itemId + " - " + examine);
                        writer.newLine();
                        writer.flush();
                        writer.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
            return dumpItem(itemId);
        }
        return false;
    }

}

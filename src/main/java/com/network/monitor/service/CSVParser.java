package com.network.monitor.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
public class CSVParser {

    public List<List<Map<String, String>>> parseCSVFile(String path) {
        List<List<Map<String, String>>> parsedFileAsList = new ArrayList();
        List<Map<String, String>> driveInfo = new ArrayList();

        File file = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!"".equals(line)) {
                    Map<String, String> property = null;

                    if (line.split(",").length != 2) {
                        if (line.split(",").length == 4 && line.contains("IPV6")) {
                            property = new HashMap<String, String>();
                            property.put(line.split(",")[0].trim(), line.split(",")[1].trim());
                            driveInfo.add(property);
                            property = new HashMap<String, String>();
                            property.put(line.split(",")[2].trim(), line.split(",")[3].trim());
                            driveInfo.add(property);
                        }
                    } else {
                        property = new HashMap<String, String>();
                        property.put(line.split(",")[0].trim(), line.split(",")[1].trim());
                        driveInfo.add(property);
                    }
                } else {
                    parsedFileAsList.add(driveInfo);
                    driveInfo = new ArrayList<Map<String, String>>();
                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return parsedFileAsList;
    }
}

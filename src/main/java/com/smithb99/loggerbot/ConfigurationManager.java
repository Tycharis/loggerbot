/* LoggerBot: Logs user join and leave events
 * Copyright (C) 2017 Braedon Smith

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.smithb99.loggerbot;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class ConfigurationManager {
    private Map<String, String> results;

    ConfigurationManager() {
        this.results = getPropertiesValues();
    }

    private Map<String, String> getPropertiesValues() {
        Map<String, String> result = new HashMap<>();
        String propertiesFileName = "config.properties";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName)) {
            Properties properties = new Properties();

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException(
                        "property file '" + propertiesFileName
                                + "' not found in the classpath");
            }

            result.put("token", properties.getProperty("token"));
            result.put("version", properties.getProperty("version"));
            result.put("prefix", properties.getProperty("prefix"));
            result.put("activelistfile", properties.getProperty("activelistfile"));
            result.put("channellistfile", properties.getProperty("channellistfile"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        return result;
    }

    Map<String, String> getResults() {
        return results;
    }
}

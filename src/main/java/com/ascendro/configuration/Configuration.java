

package com.ascendro.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Properties;

@Singleton
public class Configuration {
	
    private final Properties properties;

    
    @Inject
    public Configuration(PluginReader pluginReader) {
        properties = pluginReader.getProperties();
    }

    public String getProperty(String name) {
        if (properties == null) {
            return null;
        }

        return properties.getProperty(name);
    }
    
    public String getProperty(String name, String section) { 
        return getProperty(section + "." + name);
    }
    
    public boolean isEnabled(String name) {
        return "1".equals(getProperty(name + ".enabled"));
    }

}

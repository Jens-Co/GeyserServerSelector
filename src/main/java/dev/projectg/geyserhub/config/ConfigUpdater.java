package dev.projectg.geyserhub.config;

import org.bukkit.configuration.ConfigurationSection;

/**
 * All implementing classes must have a zero-argument constructor.
 */
@FunctionalInterface
public interface ConfigUpdater {

    /**
     * Updates a configuration from a version to the next higher version
     * @param config The config to update
     * @return True if it was a success, false if there was a failure
     * @throws IllegalArgumentException If there was an unexpected difference in the given {@link ConfigurationSection}
     */
    boolean update(ConfigurationSection config) throws IllegalArgumentException;
}

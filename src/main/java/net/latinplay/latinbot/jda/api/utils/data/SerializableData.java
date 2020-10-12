package net.latinplay.latinbot.jda.api.utils.data;

/**
 * Allows custom serialization for JSON payloads of an object.
 */
public interface SerializableData
{
    /**
     * Serialized {@link DataObject} for this object.
     *
     * @return {@link DataObject}
     */
    DataObject toData();
}

package fr.gilles.riceattend.services.repositories.room.converters

import androidx.room.TypeConverter
import fr.gilles.riceattend.models.ResourceType

class ResourceTypeConverter {
    @TypeConverter
    fun fromString(value: String?): ResourceType? {
        return value?.let { ResourceType.valueOf(it) }
    }

    @TypeConverter
    fun resourceTypeToString(resourceType: ResourceType?): String? {
        return resourceType?.name
    }
}
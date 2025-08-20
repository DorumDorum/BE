package com.project.dorumdorum.global.converter;

import com.project.dorumdorum.domain.room.domain.entity.Tag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class TagListConverter implements AttributeConverter<List<Tag>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .map(Tag::name)
                .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<Tag> convertToEntityAttribute(String tagString) {
        if (tagString == null || tagString.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(tagString.split(SEPARATOR))
                .map(Tag::valueOf)
                .collect(Collectors.toList());
    }
}

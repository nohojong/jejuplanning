package net.codecraft.jejutrip.common.Util;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime convert(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;  // 비어 있는 문자열은 null 처리
        }

        try {
            return LocalDateTime.parse(value.trim(), FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("날짜 변환 실패: [" + value + "]", e);
        }
    }  
    
    public Object convert2(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return LocalDateTime.parse(value.trim(), FORMATTER);
    }

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return (attribute == null) ? null : attribute.format(FORMATTER);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty()) ? null : LocalDateTime.parse(dbData, FORMATTER);
    }
}
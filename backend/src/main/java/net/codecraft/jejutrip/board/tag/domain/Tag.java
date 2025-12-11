package net.codecraft.jejutrip.board.tag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tagId;

    @Column(nullable = false , length = 15)
    private String tagData;

    public Tag(String tagData) {
        this.tagData = tagData;
    }

    public static Tag createFreeTag(String tag) {
        return new Tag(tag);
    }
}
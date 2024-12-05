package ru.yandex.practicum.catsgram.model;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Post {
    private Long id;
    private long authorId;
    private String description;
    private Instant postDate;
}

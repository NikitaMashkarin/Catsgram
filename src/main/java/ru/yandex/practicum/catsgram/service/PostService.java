package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private final Map<Long, Post> posts = new LinkedHashMap<>();
    private final UserService userService;

    public PostService(UserService userService){
        this.userService = userService;
    }

    public Collection<Post> findAll(int size, String sort, int from){
        SortOrder sortOrder = SortOrder.from(sort);
        List<Post> listPosts = new LinkedList<>();
        long postsSize = posts.size();
        if(sortOrder == SortOrder.ASCENDING) {
            if (from + size > postsSize) {
                for (long i = from + 1; i <= from + size; i++) {
                    listPosts.add(posts.get(i));
                }
            } else {
                for (long i = from + 1; i <= postsSize; i++) {
                    listPosts.add(posts.get(i));
                }
            }
        } else if (sortOrder == SortOrder.DESCENDING) {
            if (from + size > postsSize) {
                for (long i = from + size; i >= from + 1; i--) {
                    listPosts.add(posts.get(i));
                }
            } else {
                for (long i = postsSize; i >= from + 1; i--) {
                    listPosts.add(posts.get(i));
                }
            }
        }
        return listPosts;
    }

    public Collection<Post> findAll() {
        List<Post> listPosts = new LinkedList<>();
        long postsSize = posts.size();
        if(postsSize >= 10) {
            for (long i = postsSize; i <= postsSize - 10; i--){
                listPosts.add(posts.get(i));
            }
        } else {
            listPosts.addAll(posts.values());
        }
        return listPosts;
    }

    public Post create(Post post) {
        if(userService.findUserById(post.getId()).isPresent()) {
            if (post.getDescription() == null || post.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }

            post.setId(getNextId());
            post.setPostDate(Instant.now());
            posts.put(post.getId(), post);
            return post;
        }
        throw new ConditionsNotMetException("Автор с id = " + post.getId() + " не найден");
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Optional<Post> findPostBId(long id){
        if(posts.containsKey(id)) return Optional.of(posts.get(id));
        return Optional.empty();
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
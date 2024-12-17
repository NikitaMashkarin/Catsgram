package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{postId}")
    public Post findById(@PathVariable long postId){
        Optional<Post> post = postService.findById(postId);
        if(post.isPresent()) return post.get();
        throw new ConditionsNotMetException("Пост с id = " + postId + " не найден");
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "null") String asc,
                                    @RequestParam(defaultValue = "null") String from,
                                    @RequestParam(defaultValue = "null") String size) {
        if(asc.equals("null") || from.equals("null") || size.equals("null")) {
            return postService.findAll();
        } else {
            return postService.findAll(Integer.parseInt(size), asc, Integer.parseInt(from));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}
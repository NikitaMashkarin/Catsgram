package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {

        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Email не может быть пустым");
        }

        if (user.getPassword() == null) {
            throw new ConditionsNotMetException("Пароль не может быть пустым");
        }

        if (user.getUsername() == null) {
            throw new ConditionsNotMetException("Имя не может быть пустым");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (user.getEmail() == null) {
                throw new ConditionsNotMetException("Email не может быть пустым");
            }

            for(User user1 : users.values()){
                if(user.getEmail().equals(user1.getEmail())){
                    throw new DuplicatedDataException("Email уже используется");
                }
            }

            for(User user1 : users.values()){
                if(user.getEmail().equals(user1.getEmail())){
                    throw new DuplicatedDataException("Email уже используется");
                }
            }
            oldUser.setEmail(user.getEmail());
            oldUser.setPassword(user.getPassword());
            oldUser.setUsername(user.getUsername());
            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + user.getId() + " не найден");
    }

    public Optional<User> findUserById(long id){
        if(users.containsKey(id)) return Optional.of(users.get(id));
        return Optional.empty();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

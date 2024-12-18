package ru.yandex.practicum.catsgram.exception;

public class ImageFileException extends RuntimeException {
    public ImageFileException(String message, Exception e) {
        super(message);
    }

    public ImageFileException(String message) {
      super(message);
    }
}

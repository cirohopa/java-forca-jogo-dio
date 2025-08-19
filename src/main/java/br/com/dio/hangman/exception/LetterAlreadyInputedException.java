package br.com.dio.hangman.exception;

public class LetterAlreadyInputedException extends RuntimeException {
    public LetterAlreadyInputedException(String message) {
        super(message);
    }
}

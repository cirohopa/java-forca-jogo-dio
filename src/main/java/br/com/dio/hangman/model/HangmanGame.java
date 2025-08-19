package br.com.dio.hangman.model;

import java.util.List;

import static br.com.dio.hangman.model.HangmanGameStatus.PENDING;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGHT = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGHT_WITH_LINE_SEPARATOR = 10;

    private final int lineSize;
    //private final List<HangmanChar> hangmanPaths;
    private final List<HangmanChar> characters;

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {
        var whiteSpaces = " ".repeat(characters.size());
        var characterSpace = "-".repeat(characters.size());
        this.lineSize = HANGMAN_INITIAL_LINE_LENGHT_WITH_LINE_SEPARATOR + whiteSpaces.length();
        this.hangmanGameStatus = PENDING;
        buildHangmanDesign(whiteSpaces, characterSpace);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpaces.length());
    }

    @Override
    public String toString() {
        return this.hangman;
    }

    private List<HangmanChar> setCharacterSpacesPositionInGame(final List<HangmanChar> characters, final int whiteSpacesAmount) {
        final var LINE_LETTER = 6;

        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(this.lineSize);
        }

        return characters;
    }

    private void buildHangmanDesign(final String whiteSpaces, final String characterSpaces){
        this.hangman =
                  "  _____  " + whiteSpaces + System.lineSeparator() +
                  "  |   |  " + whiteSpaces + System.lineSeparator() +
                  "  |   |  " + whiteSpaces + System.lineSeparator() +
                  "  |      " + whiteSpaces + System.lineSeparator() +
                  "  |      " + whiteSpaces + System.lineSeparator() +
                  "  |      " + whiteSpaces + System.lineSeparator() +
                  "  |      " + whiteSpaces + System.lineSeparator() +
                  "=========" + characterSpaces + System.lineSeparator();

        ;
    }
}

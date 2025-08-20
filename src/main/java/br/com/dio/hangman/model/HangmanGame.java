package br.com.dio.hangman.model;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputedException;

import java.util.ArrayList;
import java.util.List;

import static br.com.dio.hangman.model.HangmanGameStatus.*;

public class HangmanGame {

    //Array com as etapas do desenho da forca
    private static final String[] HANGMAN_STAGES = {
            "  _____  \n  |   |  \n  |   |  \n  |      \n  |      \n  |      \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |      \n  |      \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |   |  \n  |      \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|  \n  |      \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |      \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |  /   \n  |      \n=========",
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |  / \\\n  |      \n========="
    };

    private final List<HangmanChar> characters;
    private final List<Character> failAttempts = new ArrayList<>();
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {
        this.characters = characters;
        this.hangmanGameStatus = PENDING;
    }

    public HangmanGameStatus getHangmanGameStatus() {
        return hangmanGameStatus;
    }

    public void inputCharacter(final char character){
        if (this.hangmanGameStatus != PENDING){
            var message = this.hangmanGameStatus == WIN ?
                    "Parabens! Voce ganhou" : "Voce perdeu! Tente de novo";
            throw new GameIsFinishedException(message);
        }

        //Verifica se a letra já foi tentada (seja errada ou certa)
        boolean alreadyTriedAsFail = failAttempts.contains(character);
        boolean alreadyTriedAsSuccess = characters.stream()
                .anyMatch(c -> c.getCharacter() == character && c.isVisible());

        if (alreadyTriedAsFail || alreadyTriedAsSuccess) {
            throw new LetterAlreadyInputedException("A letra '" + character + "' ja foi digitada.");
        }

        var found = this.characters.stream()
                .filter(c -> c.getCharacter() == character)
                .toList();

        // Se não encontrou a letra, é uma tentativa errada
        if (found.isEmpty()) {
            failAttempts.add(character);
            if (failAttempts.size() >= 6) { // Usar 6 em vez de HANGMAN_STAGES.length-1 para evitar quebra se o array mudar
                this.hangmanGameStatus = LOSE;
            }
        } else { // Se encontrou a letra, é uma tentativa correta
            this.characters.forEach(c -> {
                if (c.getCharacter() == character) {
                    c.enableVisibility();
                }
            });

            if (this.characters.stream().noneMatch(HangmanChar::isInvisible)) {
                this.hangmanGameStatus = WIN;
            }
        }
    }

    @Override
    public String toString() {
        //Desenho da forca correspondente ao número de erros
        String hangmanDrawing = HANGMAN_STAGES[failAttempts.size()];

        StringBuilder wordDisplay = new StringBuilder();
        for (HangmanChar c : this.characters) {
            wordDisplay.append(c.isInvisible() ? "_" : c.getCharacter());
            wordDisplay.append(" ");
        }

        String failsDisplay = "Tentativas erradas: " + failAttempts.toString();

        return hangmanDrawing + "\n\n" + wordDisplay + "\n\n" + failsDisplay;
    }

}

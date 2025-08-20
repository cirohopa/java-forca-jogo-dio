package br.com.dio.hangman.model;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.dio.hangman.model.HangmanGameStatus.*;

public class HangmanGame {

    //Array com as etapas do desenho da forca
    private static final String[] HANGMAN_STAGES = {
            "  _____  \n  |   |  \n  |   |  \n  |      \n  |      \n  |      \n  |      \n=========", // 0 erros
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |      \n  |      \n  |      \n=========", // 1 erro
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |   |  \n  |      \n  |      \n=========", // 2 erros
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|  \n  |      \n  |      \n=========", // 3 erros
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |      \n  |      \n=========", // 4 erros
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |  /   \n  |      \n=========", // 5 erros
            "  _____  \n  |   |  \n  |   |  \n  |   O  \n  |  /|\\\n  |  / \\\n  |      \n========="  // 6 erros (derrota)
    };

    private final List<HangmanChar> characters;
    private final List<Character> failAttempts = new ArrayList<>();
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters) {
        this.characters = characters;
        this.hangmanGameStatus = PENDING;
    }

    public void inputCharacter(final char character){
        if (this.hangmanGameStatus != PENDING){
            var message = this.hangmanGameStatus == WIN ?
                    "Parabéns! Você ganhou" : "Você perdeu! Tente de novo";
            throw new GameIsFinishedException(message);
        }

        // 2. Verifica se a letra já foi tentada (seja errada ou certa)
        boolean alreadyTriedAsFail = failAttempts.contains(character);
        boolean alreadyTriedAsSuccess = characters.stream()
                .anyMatch(c -> c.getCharacter() == character && c.isVisible());

        if (alreadyTriedAsFail || alreadyTriedAsSuccess) {
            // Lança a exceção AQUI
            throw new LetterAlreadyInputedException("A letra '" + character + "' ja foi digitada.");
        }

        // 3. Processa a nova letra
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
            // Torna a letra visível
            this.characters.forEach(c -> {
                if (c.getCharacter() == character) {
                    c.enableVisibility();
                }
            });

            // Verifica se o jogador ganhou
            if (this.characters.stream().noneMatch(HangmanChar::isInvisible)) {
                this.hangmanGameStatus = WIN;
            }
        }
    }

    // Substitua o toString antigo por este:
    @Override
    public String toString() {
        // 1. Pega o desenho da forca correspondente ao número de erros
        String hangmanDrawing = HANGMAN_STAGES[failAttempts.size()];

        // 2. Constrói a string da palavra (ex: "t e s t e")
        StringBuilder wordDisplay = new StringBuilder();
        for (HangmanChar c : this.characters) {
            wordDisplay.append(c.isInvisible() ? "_" : c.getCharacter());
            wordDisplay.append(" "); // Adiciona um espaço entre as letras
        }

        // 3. Constrói a string de tentativas erradas
        String failsDisplay = "Tentativas erradas: " + failAttempts.toString();

        // 4. Combina tudo em uma única string de saída
        return hangmanDrawing + "\n\n" + wordDisplay + "\n\n" + failsDisplay;
    }

}

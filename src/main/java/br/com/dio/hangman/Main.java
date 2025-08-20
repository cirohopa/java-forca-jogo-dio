package br.com.dio.hangman;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputedException;
import br.com.dio.hangman.model.HangmanChar;
import br.com.dio.hangman.model.HangmanGame;
import br.com.dio.hangman.model.HangmanGameStatus;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        var characters = Stream.of(args)
                .map(a -> a.toLowerCase().charAt(0))
                .map(HangmanChar::new).toList();

        System.out.println(characters);

        var hangmanGame = new HangmanGame(characters);

        while (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.PENDING){

            System.out.println("Bem vindo ao jogo da forca, adivinhe a palavra");
            System.out.println(hangmanGame);
            System.out.println("Selecione uma opcao");
            System.out.println("1 - informar uma letra");
            System.out.println("2 - verificar status do jogo");
            System.out.println("3 - sair do jogo");
            var opc = sc.nextInt();

            switch (opc){
                case 1 -> inputCharacter(hangmanGame);

                case 2 -> printGameStatus(hangmanGame);

                case 3 -> System.exit(0);

                default -> System.out.println("Opcao invalida");

            }

        }

        System.out.println("\nFIM DE JOGO!");
        System.out.println(hangmanGame);
        if (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.WIN) {
            System.out.println("Parabens! Voce ganhou!");
        } else {
            System.out.println("Voce perdeu!");
        }

    }

    private static void printGameStatus(HangmanGame hangmanGame) {
        System.out.println(hangmanGame.getHangmanGameStatus());
    }

    private static void inputCharacter(HangmanGame hangmanGame) {
        System.out.println("Digite uma letra");
        var character = sc.next().toLowerCase().charAt(0);

        try {
            hangmanGame.inputCharacter(character);

        } catch (LetterAlreadyInputedException e){
            System.out.println(e.getMessage());
        }
    }

}
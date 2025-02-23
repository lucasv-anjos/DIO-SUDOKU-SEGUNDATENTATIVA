package dio.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class main 
{

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int board_limit = 9;



    public static void main(String[] args) {

        final var positions = Stream.of(args).collect(Collectors.toMap(k -> k.split(";")[0], v -> v.split(";")[1]));

        var option = -1;

        while(true){
            System.out.println("1. Iniciar jogo");
            System.out.println("2. Colocar numero");
            System.out.println("3. Remover numero");
            System.out.println("4. mostrar tabuleiro");
            System.out.println("5. verificar jogo");
            System.out.println("6. limpar jogo");
            System.out.println("7. finalizar jogo");
            System.out.println("8. sair");

            option = scanner.nextInt();

            switch(option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> verifyGame();
                case 6 -> clearGame();
                case 7 -> endGame();
                case 8 -> System.exit(0);

                default -> System.out.println("opção invalida");
            }

        }
    }



    private static void startGame(Map <String, String> positions) {
            if(nonNull(board)){
                System.out.println("o jogo ja foi iniciado");
                return;
            }

            List<List<Space>> spaces = new ArrayList<>();
            for(int i = 0 ; i < board_limit ; i++){
                spaces.add(new ArrayList<>());
                for (int x = 0 ; x < board_limit ; x++){
                    var positionConfig = positions.get("%s,%s".formatted(i,x));
                    var expected = Integer.parseInt(positionConfig.split(",")[0]);
                    var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                    var currentSpace = new Space(expected, fixed);
                    spaces.get(i).add(currentSpace);

                }
            }

            board = new Board(spaces);

            System.out.println("o jogo está pronto para começar");

        }

    private static void inputNumber() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }
        System.out.println("coluna:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("linha:");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("\n informe o numero a ser inserido na posição [%s,%s]", row, col);
        var number = runUntilGetValidNumber(1, 9);
        if(!board.changeValue(col, row, col)){
            System.out.println("a posição possui valor fixo");
        }
    }

    private static void removeNumber() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }
        System.out.println("coluna:");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("linha:");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("\n informe o numero a ser inserido na posição [%s,%s]", row, col);
        var number = runUntilGetValidNumber(1, 9);
        if(!board.clearValue(col, row)){
            System.out.println("a posição possui valor fixo");
        }    }

    private static void verifyGame() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }    
    
        if(board.gameIsFinished()){
            System.out.println("Parabens voce completou o jogo");
            showCurrentGame();
            board = null;
        }else if(board.hasError()){
            System.out.println("seu jogo contem erros, verifique o board");
        }else{
            System.out.println("ainda ha espaços vazios");
        }
    }

    private static void clearGame() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }    
    
        System.out.println("deseja realmente apagar o jogo?");
        var confirm = scanner.next();
        while(!confirm.equalsIgnoreCase("sim") || !confirm.equalsIgnoreCase("nao")){
            System.out.println("informe sim ou não");
            confirm = scanner.next();
        }

        if(confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }

    private static void endGame() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }

    }

    private static void showCurrentGame() {
        if(isNull(board)){
            System.out.println("o jogo não foi iniciado");
            return;
        }

        var args = new Object[81];
        var argPos = 0;
        for(int i = 0; i < board_limit ; i++){
            for(var col: board.getSpaces()){
                args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("seu jogo se encontra da seguinte forma:");
        System.out.println("CurrentGame");
        //System.out.printf((BoardTemplate) + "\n", args );
    }

    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
            while(current < min || current > max){
                System.out.printf("numero invalido, insira um numero entre %s e %s", min, max);
                current = scanner.nextInt();
        }
        return current;
    }


}

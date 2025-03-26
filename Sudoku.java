import java.util.Random;
import java.util.Scanner;

public class Sudoku {

    private int[][] board;
    private int size = 9;
    private int emptyCell = 0;

    public Sudoku() {
        board = new int[size][size];
    }

    public void generatePuzzle(int numClues) {
        // 1. Preencher o tabuleiro completamente (solução válida)
        solveSudoku();

        // 2. Remover números aleatoriamente para criar o puzzle
        Random random = new Random();
        int removedCount = 0;
        int totalCells = size * size;

        while (removedCount < (totalCells - numClues)) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (board[row][col] != emptyCell) {
                int temp = board[row][col];
                board[row][col] = emptyCell;

                // Opcional: Verificar se o puzzle ainda tem uma única solução
                // (Implementação mais complexa, omitida para simplicidade)

                removedCount++;
            }
        }
    }

    public void printBoard() {
        System.out.println("  1 2 3 | 4 5 6 | 7 8 9");
        System.out.println("-------------------------");
        for (int i = 0; i < size; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("-------------------------");
            }
            System.out.print((i + 1) + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] == emptyCell ? ". " : board[i][j] + " ");
                if ((j + 1) % 3 == 0 && (j + 1) != size) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isValidMove(int row, int col, int num) {
        // Verificar linha
        for (int c = 0; c < size; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // Verificar coluna
        for (int r = 0; r < size; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // Verificar o bloco 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean solveSudoku() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == emptyCell) {
                    for (int number = 1; number <= size; number++) {
                        if (isValidMove(row, col, number)) {
                            board[row][col] = number;

                            if (solveSudoku()) {
                                return true; // Solução encontrada
                            } else {
                                board[row][col] = emptyCell; // Backtrack
                            }
                        }
                    }
                    return false; // Sem número válido para esta célula
                }
            }
        }
        return true; // Tabuleiro completo
    }

    public boolean isBoardFull() {
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == emptyCell) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Sudoku game = new Sudoku();
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Nível de dificuldade (número de pistas)
        int difficulty = 30 + random.nextInt(20); // Entre 30 e 50 pistas
        game.generatePuzzle(difficulty);

        System.out.println("Bem-vindo ao Sudoku!");
        game.printBoard();

        while (!game.isBoardFull()) {
            System.out.println("Digite a linha (1-9), coluna (1-9) e número (1-9) separados por espaço (ou 'sair'):");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("sair")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length == 3) {
                try {
                    int row = Integer.parseInt(parts[0]) - 1;
                    int col = Integer.parseInt(parts[1]) - 1;
                    int num = Integer.parseInt(parts[2]);

                    if (row >= 0 && row < game.size && col >= 0 && col < game.size && num >= 1 && num <= game.size) {
                        if (game.board[row][col] == game.emptyCell) {
                            if (game.isValidMove(row, col, num)) {
                                game.board[row][col] = num;
                                game.printBoard();
                                if (game.isBoardFull()) {
                                    System.out.println("Parabéns! Você resolveu o Sudoku!");
                                    break;
                                }
                            } else {
                                System.out.println("Movimento inválido: o número já existe na linha, coluna ou bloco.");
                            }
                        } else {
                            System.out.println("Essa célula já está preenchida.");
                        }
                    } else {
                        System.out.println("Entrada inválida. Linha, coluna e número devem estar entre 1 e 9.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Certifique-se de digitar números.");
                }
            } else {
                System.out.println("Entrada inválida. Digite linha, coluna e número separados por espaço.");
            }
        }

        if (!game.isBoardFull()) {
            System.out.println("Jogo encerrado.");
        }
        scanner.close();
    }
}
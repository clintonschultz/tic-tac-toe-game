import java.util.*;
import java.net.*;
import java.io.*;

public class Client {

    private static Socket toServerSocket;
    private static String hostName = "localhost";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static PrintWriter out;
    private static BufferedReader in;
    private static int row, col;
    private static char[][] board;

    public static void main(String[] args) throws IOException {

        toServerSocket = new Socket(hostName, 8787);
        inputStream = new DataInputStream(toServerSocket.getInputStream());
        outputStream = new DataOutputStream(toServerSocket.getOutputStream());
        out = new PrintWriter(outputStream, true);
        in = new BufferedReader(new InputStreamReader(inputStream));


        board = new char[4][4];
        for (int x = 0; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                board[x][y] = ' ';
            }
        }
        row = -1;
        col = -1;

        playgame(in, out);
    }

    public static void playgame(BufferedReader in, PrintWriter out) throws IOException {
        Scanner inp = new Scanner(System.in);
        String response = " ";
        Boolean turn = false;
        Boolean gameover = false;

        while (gameover == false) {
            if (turn) {
                do {
                    System.out.print("\nEnter Row: ");
                    row = inp.nextInt();
                    System.out.print("\nEnter Column: ");
                    col = inp.nextInt();
                    System.out.println("\n");
                } while (row < 0 || row > 3 || col > 3 || col < 0 || board[row][col] != ' ');
                board[row][col] = 'O';
                out.println("MOVE " + row + " " + col);
            } else {
                response = in.readLine();
                if (!response.equals("CLIENT")) {
                    String[] args = response.split("\\s+"); // splits message into tokens
                    if (args.length > 3) {
                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        if (!args[3].equals("WIN") && row != -1)
                            board[row][col] = 'X';
                        switch (args[3]) {
                            case "WIN":
                                System.out.println("\nCongratulations!!! You WON the game!\n");
                                break;
                            case "TIE":
                                System.out.println("\nThe game was a TIE!\n");
                                break;
                            case "LOSS":
                                System.out.println("\nSORRY! You LOST the game!\n");
                                break;
                        } // end switch
                        gameover = true;
                    } //end if length > 3

                    else { // move was not a win, loss, or tie - just regular move
                        // get row from args[1]  YOU LEFT THIS UNDONE! THAT WAS A PROBLEM!

                        row = Integer.parseInt(args[1]);
                        col = Integer.parseInt(args[2]);
                        board[row][col] = 'X';

                        // get row from args[2]
                        //set cell of board matrix to 'X'
                    } // end else
                } // end if
                else {
                    System.out.println("\nYOU MOVE FIRST\n");
                }  // end else
            } // end else
            printboard();
            turn = !turn;


        } // end while
        System.out.println("\n\nHere is the final game board\n");
        printboard();
    } // end playgame method


    public static void printboard() {
        System.out.println(" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " | " + board[0][3] + " ");
        System.out.println("---------------");
        System.out.println(" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " | " + board[1][3] + " ");
        System.out.println("---------------");
        System.out.println(" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " | " + board[2][3] + " ");
        System.out.println("---------------");
        System.out.println(" " + board[3][0] + " | " + board[3][1] + " | " + board[3][2] + " | " + board[3][3] + " ");
        System.out.println("\n");
    } // displays the game board
} // end class Client

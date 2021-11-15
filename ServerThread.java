import java.io.*;
import java.net.*;
import java.util.Random;

public class ServerThread extends Thread {

    private Socket toclientsocket; // the socket used to communicate with client
    private DataInputStream instream; // stores inputstream of the socket
    private DataOutputStream outstream; // stores outputstream of the socket
    private PrintWriter out; // allows us to use print() and println() methods
    private BufferedReader in; // allows us to use readLine() method
    private Random gen; // used to select random moves
    private char[][] board; // the gameboard matrix
    private int row, col; // hold the current row and column

    // constructor for ServerThread class
    ServerThread(Socket s) throws IOException {
        this.toclientsocket = s;
        gen = new Random();
        instream = new DataInputStream(toclientsocket.getInputStream());
        outstream = new DataOutputStream(toclientsocket.getOutputStream());
        out = new PrintWriter(outstream, true);
        in = new BufferedReader(new InputStreamReader(instream));
        // instantiate and initialize 4x4 array for the board
        board = new char[4][4];
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                board[i][j] = ' ';
            }
        }
        row = -1;
        col = -1;
    }

    public void run() {

        int counter = 0;
        String response = "";
        Boolean gameover = false;
        Boolean turn = false;

        int temp = (int) Math.floor(Math.random() * (4) + 1);

        if (temp <= 3) {
            turn = true;
            out.println("CLIENT");
        } else turn = false;

        while (gameover == false) {
        if (turn) {
            try {
                response = in.readLine(); // read player's move with readLine() method
            } catch (IOException e) {
                System.out.println("Some sort of error on socket in server thread");
            }
            // break string into words
            String[] data = response.split("\\s+"); // breaks string at any whitespace
            row = Integer.parseInt(data[1]);
            col = Integer.parseInt(data[2]);

            // put an 'O' into selected cell of matrix
            board[row][col] = 'O';

            counter++;
            printboard(counter); // call printboard() method

            if (checkwin() || counter == 16) {
                gameover = true; // set gameover to true as game is finished
                if (checkwin())
                    out.println("MOVE -1 -1 WIN"); // send message "MOVE -1 -1 WIN" to client via PrintWriter object
                else out.println("MOVE -1 -1 TIE"); // send message "MOVE -1 -1 TIE" to client
            }
        } else { // this is the server's move code
            makemove(); // see below for method description
            counter++; // increment the move counter
            board[row][col] = 'X'; // set the board[row][col] to an 'X'
            printboard(counter); // see below for method's description

            if (checkwin() || counter == 16) { // did computer win or tie?
                gameover = true;
                if (checkwin()) {
                    //send a LOSS message to the client but make sure to send the correct row and col vals
                    out.println("MOVE " + row + " " + col + " LOSS");
                } else {
                    // send a TIE message to client
                    out.println("MOVE " + row + " " + col + " TIE");
                }
            } else { // move does not end the game
                // send client a MOVE message with correct row and col vals
                out.println("MOVE " + row + " " + col);
            }
        } // end server's turn
        turn = !turn;
    } // end the game's while loop

} // end run() method

    void makemove() {
        // generates the random numbers for col and row until we find a blank spot
        row = (int) Math.floor(Math.random() * (3) + 0);
        col = (int) Math.floor(Math.random() * (3) + 0);

        if (board[row][col] != ' ') {
            makemove();
        }
    }

    boolean checkwin() {
        for (int x = 0; x <= 3; x++) { // check for a row win
            if (board[x][0] == board[x][1] && board[x][1] == board[x][2] && board[x][2] == board[x][3] && board[x][0] != ' ') {
                return true;
            }
        }
        for (int x = 0; x <= 3; x++) { // check for a column win
            if (board[0][x] == board[1][x] && board[1][x] == board[2][x] && board[2][x] == board[3][x] && board[0][x] != ' ') {
                return true;
            }
        }
        // check for a diagonal win from top left to bottom right
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == board[3][3] && board[0][0] != ' ') {
            return true;
        }
        // check for a diagonal win from top right to bottom left
        if (board[0][3] == board[1][2] && board[1][2] == board[2][1] && board[2][1] == board[3][0] && board[0][3] != ' ') {
            return true;
        }
        return false;
    } // checks the matrix to see if there is a win

    void printboard(int count) {
        System.out.println("--- Turn " + count + " ---\n");
        System.out.println(" "+board[0][0]+" | "+board[0][1]+" | "+board[0][2]+" | "+board[0][3]+" ");
        System.out.println("---------------");
        System.out.println(" "+board[1][0]+" | "+board[1][1]+" | "+board[1][2]+" | "+board[1][3]+" ");
        System.out.println("---------------");
        System.out.println(" "+board[2][0]+" | "+board[2][1]+" | "+board[2][2]+" | "+board[2][3]+" ");
        System.out.println("---------------");
        System.out.println(" "+board[3][0]+" | "+board[3][1]+" | "+board[3][2]+" | "+board[3][3]+" ");
        System.out.println("\n");
    } // displays the game board
}


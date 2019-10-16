public class Board
{
    private final int BLANK = 0;
    private final int X = 1;
    private final int O = 2;

    private int[][] _board;
    private int[] _moveLocations;
    private int N;

    // True if X's turn, False if O's turn
    private  boolean _xTurn;


    /**
     * Generic Constructor, initializes a board with n by n blank spaces and sets the turn to X.
     * @param n The dimension of the board will be n by n.
     */
    public Board(int n)
    {
        N = n;
        _board = new int[N][N];
        _moveLocations = new int[N];
        _xTurn = true;
    }

    /**
     * Constructor for testing ony, board is passed in.
     * @param board The array of integers representing the board.
     */
    public Board(int[][] board)
    {
        N = board.length;
        _board = board;
        _moveLocations = new int[N];
    }

    /**
     * Constructor for copying, board, moveLocations, and current turn are passed in.
     * @param board The array of integers representing the board.
     */
    public Board(int[][] board, int[] moveLocations, boolean turn)
    {
        N = board.length;
        _board = board;
        _moveLocations = moveLocations;
        _xTurn = turn;
    }

    /**
     * Creates a deep copy of this board instance.
     * @return a new board that exactly copies the current one.
     */
    public Board copy()
    {
        int[][] board = new int[N][N];
        int[] moves = new int[N];

        for (int x = 0; x < N; x++)
        {
            moves[x] = _moveLocations[x];
            for (int y = 0; y < _moveLocations[x]; y++)
            {
                board[x][y] = _board[x][y];
            }
        }
        return new Board(board, moves, _xTurn);
    }

    /**
     * Make a move at specified location.
     *
     * Whether the turn is currently on X or O will be handled, simply provide the location for the move.
     * @param move Integer value corresponding to the column that you will move into.
     */
    public void makeMove(int move)
    {
        if (_xTurn && _moveLocations[move] < N)
        {
            _board[move][_moveLocations[move]] = X;
            _xTurn = !_xTurn;
            _moveLocations[move]++;
        }
        else if (!_xTurn && _moveLocations[move] < N)
        {
            _board[move][_moveLocations[move]] = O;
            _xTurn = !_xTurn;
            _moveLocations[move]++;
        }
    }

    /**
     * Return an integer value representing undecided, X wins, O wins, or catsgame
     * @return 0 = undecided, 1 = X wins 2 = O wins, 3 = Catsgame
     */
    public int getWinner()
    {
        if (nInALineWith4Possible(4 , X) > 0)
        {
            return X;
        }
        if (nInALineWith4Possible(4, O) > 0)
        {
            return O;
        }

        int blanks = 0;
        for (int[] column : _board)
        {
            for (int space : column)
            {
                if (space == BLANK)
                {
                    blanks++;
                }
            }
        }
        if (blanks == 0)
        {
            return 3;
        }
        else
        {
            return 0;
        }

    }

    /**
     * Check if a move is valid.
     * @param move the integer value for the column you are playing in.
     * @return true if and only if there is room left in that column for playing.
     */
    public boolean isValidMove(int move)
    {
        return _moveLocations[move] < N;
    }

    /**
     * Will return the number of times that n things appear in a line of 4 possible
     * @param n Things in a line of 4, should be a value 2 -> 4 inclusive
     * @param value 1 for Xs, 2 for 0s
     * @return an integer representing the number of times that n things in a line of 4 possible occurred.
     */
    public int nInALineWith4Possible(int n, int value)
    {
        int occurances = 0;

        for (int i = 0; i < N; i++)
        {
            // horizontal
            occurances += checkInARow(i,0,0,1,value,n);
            // vertical
            occurances += checkInARow(0,i,1,0,value,n);
            // diagonal: up to right along left
            occurances += checkInARow(0,i,1,1,value,n);
            // diagonal: up to left along right
            occurances += checkInARow(N-1,i,-1,1,value,n);
            if (i != 0)
            {
                // diagonal: up to right along bottom
                occurances += checkInARow(i,0,1,1,value,n);
                // diagonal: up to left along bottom
                occurances += checkInARow(i,0,-1,1,value,n);
            }
        }

        return occurances;
    }

    private int checkInARow( int x, int y, int deltaX, int deltaY, int value, int n)
    {
        int occurances = 0;
        int[] slide = {0,0,0,0};

        while (x >= 0 && x < N && y >= 0 && y < N)
        {
            slide( slide, _board[x][y] );
            occurances += checkSlide(n , value, slide);

            x += deltaX;
            y += deltaY;
        }

        return occurances;
    }

    /**
     * Iterate the slider
     * @param slide pointer to the slider.
     * @param value the new value entering the slider.
     */
    private void slide(int[] slide, int value)
    {
        for (int i = 0; i < slide.length - 1; i++)
        {
            slide[i] = slide[i + 1];
        }
        slide[slide.length - 1] = value;
    }

    /**
     * Check a single instance of a slider for n or more occurrences of value
     * @param n the quota of instances you are trying to hit
     * @param value the value (X or O) that you are checking for
     * @param slide an array of four consecutive values on the board
     * @return 1 if quota is met, 0 if not met or an occurrence of the opposite of value appears.
     */
    private int checkSlide(int n, int value, int[] slide)
    {
        int numValues = 0;
        for (int i = 0; i < slide.length; i++)
        {
            if (slide[i] == value)
            {
                numValues++;
            }
            if (slide[i] == opposite(value))
            {
                return 0;
            }
        }
        if (numValues >= n)
        {
            return 1;
        }
        return 0;
    }

    private int opposite(int value)
    {
        if (value == X)
        {
            return O;
        }
        return X;
    }
    
}

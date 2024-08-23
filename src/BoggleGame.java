package src;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class BoggleGame implements BoggleGameInterface {

    @Override
    public char[][] generateBoggleBoard(int size) {
        if (size <= 0) {
            return null;
        }
        int stringLength = size * size;
        if (stringLength <= 0) {
            return null;
        }
        String s = generateRandomString(stringLength);
        char[][] board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = s.charAt(i * size + j);
            }
        }
        return board;
    }

    @Override
    public int countWords(char[][] boggleBoard, DictInterface dictionary) {
        int maxLen = boggleBoard.length * boggleBoard[0].length; //  max possible wordLen depending size of board
        return wordsInDict(boggleBoard, dictionary, 3, maxLen).size(); // ret count of words within regulation len
    }

    @Override 
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
        return wordsInDict(boggleBoard, dictionary, wordLength, wordLength).size(); // ret count of words of requested len
    }

    @Override 
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
        int status = dictionary.searchPrefix(new StringBuilder(word)); // get status of word in dict
        return (status == 2 || status == 3); // if status is word or is word AND prefix
    }

    @Override 
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
        int rows = boggleBoard.length;
        int cols = boggleBoard[0].length;

        // for every line (row) in board
        for (int row = 0; row < rows; row++) {
            // for each cell (col) in line (row)
            for (int col = 0; col < cols; col++) {
                // bool array to keep track of visited cells
                boolean[][] visited = new boolean[rows][cols];
                if (isWordInBoardDFS(boggleBoard, visited, new StringBuilder(), word, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override 
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
        
        int minLen = 3;
        int maxLen = boggleBoard[0].length*boggleBoard.length;

        TreeSet<String> wordsInDict = wordsInDict(boggleBoard, dictionary, minLen, maxLen); // gather words from dict in the board
        if(wordsInDict.isEmpty()) return null; // if empty, ret null

        ArrayList<String> arr = new ArrayList<>(wordsInDict); // convert to arrayList

        int randNum = new Random().nextInt(arr.size()); // rand index

        return arr.get(randNum); // return selected word 
    }

    @Override 
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
        
        int rows = boggleBoard.length;
        int cols = boggleBoard[0].length;
        
        // for every line (row) in board 
        for (int row = 0; row < boggleBoard.length; row++) {
            // for each cell (col) in line (row)
            for (int col = 0; col < boggleBoard[0].length; col++) {
                // search only if first letter matches
                if (Character.toLowerCase(boggleBoard[row][col]) == word.charAt(0)) { 
                    ArrayList<Tile> path_coords = new ArrayList<>(); // storing word coords
                    boolean[][] visited = new boolean[rows][cols];
                    if (markWordInBoardDFS(boggleBoard, visited, new StringBuilder(), path_coords, word, row, col)) {
                        return path_coords; // ret word coords
                    }
                }
            }
        }
        return new ArrayList<>(); // if word not found, ret empty list 
    }

    @Override 
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
        

        // if no tiles, empty str, or tiles.size doensn't match word len
        if (tiles.isEmpty() || word.isEmpty() || tiles.size() != word.length()) return false;
    
        // for every tile in tiles
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            
            // if tile not valid
            if (!isValid(boggleBoard, tile.row, tile.col)) return false;
    
            // if char in tile != curr letter in word
            if (Character.toLowerCase(boggleBoard[tile.row][tile.col]) != Character.toLowerCase(word.charAt(i))) return false;
    
            // if curr tile is adj to nextTile
            if (i < tiles.size() - 1) {
                Tile nextTile = tiles.get(i + 1);
                int rowDiff = Math.abs(tile.row - nextTile.row);
                int colDiff = Math.abs(tile.col - nextTile.col);
    
                // if adj (both not 0)
                if (rowDiff > 1 || colDiff > 1 || (rowDiff == 0 && colDiff == 0)) return false;
            }
        }
        return true;
    }

    @Override 
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
        

        // get list of words in dict on board of requested len
        TreeSet<String> wordsInDict = wordsInDict(boggleBoard, dictionary, length, length);
        if(wordsInDict.isEmpty()) return null;

        // convert to arrayList
        ArrayList<String> arr = new ArrayList<>(wordsInDict);

        // get random index
        int randNum = new Random().nextInt(arr.size());

        // ret selected word
        return arr.get(randNum);
    }

    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
        return generatedString;
    }


    // ============================================================================ ============================================================================ //


    // H E L P E R   F U N C T I O N S \\

    private boolean isValid(char[][] boggleBoard, int row, int col) {
        int rows = boggleBoard.length;
        int cols = boggleBoard[0].length;
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    
    private TreeSet<String> wordsInDict(char[][] boggleBoard, DictInterface dictionary, int minLen, int maxLen) {
        // initialize set of found words in board
        TreeSet<String> foundWords = new TreeSet<>();
        // for every row in board
        for (int row = 0; row < boggleBoard.length; row++) {
            // for every col in row
            for (int col = 0; col < boggleBoard[0].length; col++) {
                // [][] for keeping track of visited tiles
                boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[0].length];
                // depth first search of possible words
                wordsInDictDFS(boggleBoard, visited, foundWords, new StringBuilder(), dictionary, row, col, minLen, maxLen);
            }
        }
        return foundWords;
    }

    
    private void wordsInDictDFS(char[][] boggleBoard, boolean[][] visited, TreeSet<String> foundWords, StringBuilder path, DictInterface dictionary, int row, int col, int minLen, int maxLen) {

        // if tile is worth visiting
        if (!isValid(boggleBoard, row, col) || visited[row][col] || path.length() > maxLen) return;
    
        visited[row][col] = true; // mark tile visited
        path.append(boggleBoard[row][col]); // tile to path
    
        // status of curr path in dict
        int status = dictionary.searchPrefix(new StringBuilder(path.toString().toLowerCase()));
    
        // if path is word add to foundWords
        if (path.length() >= minLen && path.length() <= maxLen) { // word meet len reqs
            if (status == 2 || status == 3) foundWords.add(path.toString());
        }
    

        // if path is prefix or word, recursive calls for DFS
        if (status == 1 || status == 2 || status == 3) {
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row - 1, col - 1, minLen, maxLen); // top-left
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row - 1, col, minLen, maxLen);     // top
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row - 1, col + 1, minLen, maxLen); // top-right
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row, col - 1, minLen, maxLen);     // left
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row, col + 1, minLen, maxLen);     // right
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row + 1, col - 1, minLen, maxLen); // bottom-left
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row + 1, col, minLen, maxLen);     // bottom
            wordsInDictDFS(boggleBoard, visited, foundWords, path, dictionary, row + 1, col + 1, minLen, maxLen); // bottom-right
        }
    
        // backtracking \\
        visited[row][col] = false; // unmark tile
        path.deleteCharAt(path.length() - 1); // remove tile from path
    }
    
    
    private boolean isWordInBoardDFS(char[][] boggleBoard, boolean[][] visited, StringBuilder path, String word, int row, int col) {

        // if tile worth visiting
        if (!isValid(boggleBoard, row, col) || visited[row][col] || path.length() > word.length()) return false;

        visited[row][col] = true; // mark tile visited
        path.append(boggleBoard[row][col]); // add tile to path
        
        // if path len matches word len
        if(path.length() == word.length()) {
            // path == word
            if(path.toString().equalsIgnoreCase(word)) return true;
        }

        // call DFS, ret if true (found word)
        if(isWordInBoardDFS(boggleBoard, visited, path, word,  row - 1, col) ||
        isWordInBoardDFS(boggleBoard, visited,  path, word, row + 1, col) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row, col - 1) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row, col + 1) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row - 1, col - 1) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row - 1, col + 1) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row + 1, col - 1) ||
        isWordInBoardDFS(boggleBoard, visited, path, word, row + 1, col + 1)) {
            return true;
        }

        // backtracking \\
        path.deleteCharAt(path.length() - 1); // remove tile from path
        visited[row][col] = false; // unmark tile
        return false;
    }

    
    private boolean markWordInBoardDFS(char[][] boggleBoard, boolean[][] visited, StringBuilder path, ArrayList<Tile> path_coords, String word, int row, int col) {

        // if tile worth visiting 
        if (!isValid(boggleBoard, row, col) || visited[row][col] || path.length() >= word.length()) return false;

        visited[row][col] = true; // mark tile visited
        path.append(boggleBoard[row][col]); // add tile to path 
        path_coords.add(new Tile(row, col)); // add tile to coords
        
        // if path and word are the same len and string, ret true
        if (path.length() == word.length() && path.toString().equalsIgnoreCase(word)) return true;

        // DFS calls
        for (int i = 0; i < 8; i++) {
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row - 1, col - 1)) return true; // top-left
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row - 1, col)) return true;     // top
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row - 1, col + 1)) return true; // top-right
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row, col - 1)) return true;     // left
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row, col + 1)) return true;     // right
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row + 1, col - 1)) return true; // bottom-left
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row + 1, col)) return true;     // bottom
            if (markWordInBoardDFS(boggleBoard, visited, path, path_coords, word, row + 1, col + 1)) return true; // bottom-right
        }

        // backtracking \\ 
        path.deleteCharAt(path.length() - 1); // remove tile from path
        path_coords.remove(path_coords.size() - 1); // remove tile from coords
        visited[row][col] = false; // unmark tile
        return false;
    }
    
}







































// Specify Lengths - Find all words
    // public static ArrayList<String> wordsInBoard(char[][] boggleBoard, int minLen, int maxLen) {
    //     ArrayList<String> foundWords = new ArrayList<>();
    //     int rows = boggleBoard.length;
    //     int cols = boggleBoard[0].length;

    //     for (int row = 0; row < rows; row++) {
    //         for (int col = 0; col < cols; col++) {
    //             boolean[][] visited = new boolean[rows][cols];
    //             wordsInBoardDFS(boggleBoard, visited, foundWords, new StringBuilder(), row, col, minLen, maxLen);
    //         }
    //     }

    //     return foundWords;
    // }

    // private static void wordsInBoardDFS(char[][] boggleBoard, boolean[][] visited, ArrayList<String> foundWords, StringBuilder growingWord,
    //         int row, int col, int minLen, int maxLen) {
    //     int rows = boggleBoard.length;
    //     int cols = boggleBoard[0].length;

    //     if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col]) {
    //         return;
    //     }

    //     growingWord.append(boggleBoard[row][col]);

    //     if (growingWord.length() >= minLen && growingWord.length() <= maxLen) {
    //         foundWords.add(growingWord.toString());
    //     }

    //     visited[row][col] = true;

    //     // all adjacent directions
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row - 1, col, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row + 1, col, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row, col - 1, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row, col + 1, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row - 1, col - 1, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row - 1, col + 1, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row + 1, col - 1, minLen, maxLen);
    //     wordsInBoardDFS(boggleBoard, visited, foundWords, growingWord, row + 1, col + 1, minLen, maxLen);

    //     // for backtracking
    //     growingWord.deleteCharAt(growingWord.length() - 1);
    //     visited[row][col] = false;
    // }

    // wordsInDict


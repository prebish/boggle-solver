# Boggle Solver

A simple command-line Java program to solve Boggle puzzles by finding all possible words on the board, leveraging depth-first search and dictionary validation.

### Table of Contents

- [Installation & Setup](#installation--setup)
    1. [Clone the Repository](#1-clone-the-repository)
    2. [Compile the Program](#2-compile-the-program)
- [Usage](#usage)
    - [Menu Options](#menu-options)
- [License](#license)

## Installation & Setup

Ensure that you have the latest version of the Java Development Kit installed.  
You can download it [<u>**here**</u>](https://www.oracle.com/java/technologies/downloads/).

### 1. Clone the Repository
```bash
git clone https://github.com/prebish/boggle-solver.git
```

### 2. Compile the Program
```bash
cd ./boggle-solver
javac -d build ./src/*.java
```

## Usage

Run the program using the following command:

```bash
java -cp build src.Main
```

### Menu Options

Upon running the program, you will be presented with the following menu options:

*********************************
1. **Generate a new Boggle board**: Create a random Boggle board of specified dimensions.
2. **Load a dictionary from a file**: Load a custom dictionary file to be used for word validation.
3. **Display the Boggle board**: Show the current Boggle board in the console.
4. **Get number of possible words**: Find and display the total number of valid words on the board.
5. **Get number of possible words with a certain length**: Find and display the number of words of a specific length on the board.
6. **Check if a word is in the dictionary**: Verify if a particular word exists in the loaded dictionary.
7. **Check if a word is feasible in the board**: Check if a specific word can be formed on the current Boggle board.
8. **Exit**: Exit the program.
*********************************

Simply enter the number corresponding to your desired option and follow the prompts.

## License
This project is licensed under the **MIT License** - see the `LICENSE` file for details.

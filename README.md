
# Boggle Solver

A simple command-line Java program to solve Boggle puzzles by finding all possible words on the board, leveraging depth-first search and dictionary validation.

### Table of Contents

- [Installation & Setup](#installation--setup)
    1. [Clone the Repository](#1-clone-the-repository)
    2. [Compile the Program](#2-compile-the-program)
- [Usage](#usage)
    - [Generating a Boggle Board](#generating-a-boggle-board)
    - [Finding Words](#finding-words)
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

This is the baseline of what you'll use to run the program. Try running it yourself.  
The usage will print out when there are insufficient arguments.

```bash
java -cp build BoggleSolver
```

### Generating a Boggle Board

#### Generate a random Boggle board with a specified size
```bash
java -cp build BoggleSolver -g <size>
```

Example:
```bash
java -cp build BoggleSolver -g 4
```

### Finding Words

#### Find all words in the generated Boggle board
```bash
java -cp build BoggleSolver -f
```

#### Optionally, specify a dictionary file
```bash
java -cp build BoggleSolver -f -d dictionary.txt
```

## License

This project is licensed under the **MIT License** - see the `LICENSE` file for details.

A clean, testable Chess game implemented in Java with a strong focus on Data Structures & Algorithms (DSA). Built to practice design, correctness, and performance while keeping the code beginner-friendly and extensible.

Features

Full chess rules: legal move generation, check/checkmate, stalemate, draw by insufficient material, fifty-move rule, threefold repetition, castling, en passant, promotion.

Undo/Redo using stacks; reproducible move history.

Move notation: Algebraic (SAN) + coordinate (UCI) forms.

Board import/export: FEN support for start positions.

Search-ready core: deterministic engine hooks (no AI by default, pluggable minimax/alpha–beta).

CLI UI (always available) and optional Swing/JavaFX GUI module.

Unit tests with JUnit.

Board Representation: 8×8 grid with bitboards (long) for fast set ops or a 2D array fallback (configurable).

Move Generation: Precomputed attack masks, sliding attacks via directional scans; pseudo-legal → legal filter.

Game State: Struct-like GameState with Zobrist hash for repetition detection (HashSet/Map).

History: Stack<Move> and Stack<GameStateDelta> for O(1) undo/redo.

Queues: Event queue for UI updates (optional module).

Trees (optional AI): Minimax game tree with alpha–beta pruning, iterative deepening.


Prerequisites

Java 17+

One of: Gradle or Maven (or plain javac)

git clone https://github.com/your-username/chess-dsa-java.git
cd chess-dsa-java

# Run CLI
gradlew :cli:run

# Run tests
gradlew test
# Run tests
mvn -q -DskipTests=false test

# Run CLI (example if cli is an app module)
mvn -q -pl cli -am exec:java -Dexec.mainClass=com.chess.cli.CliApp
javac -d out $(find core -name "*.java") $(find cli -name "*.java")
java -cp out com.chess.cli.CliApp
DSA Chess (Java) — type 'help' for commands

> new                # start new game
> fen <string>       # load position from FEN
> print              # show board
> moves              # list legal moves
> play e2e4          # play move in UCI
> undo | redo        # navigate history
> save <file.pgn>    # (optional) export game
> quit
Check/Checkmate/Stalemate: legal move filter ensures king safety.

Castling: requires unmoved rook/king, empty path, and no attacked squares.

En Passant: only immediately after a two-step pawn move.

Promotion: to Q/R/B/N (default Q); supports UCI e7e8q.

Draws: stalemate, 50-move rule, threefold repetition, insufficient material.

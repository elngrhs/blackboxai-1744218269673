
Built by https://www.blackbox.ai

---

```markdown
# Five Card Draw Poker Game

## Project Overview
This project is a simple implementation of the classic Five Card Draw Poker game. Players can join the game, place bets, replace cards, and determine the winner based on hand evaluations. The game is played in rounds, where players can continue until only one remains or they decide to exit.

## Installation
To run the Poker Game, you need to have Java Development Kit (JDK) installed on your machine. Follow these steps to set up your environment:

1. **Clone the repository**:
   ```bash
   git clone <repository_url>
   cd PokerGame
   ```

2. **Compile the Java file**:
   ```bash
   javac PokerGame.java
   ```

3. **Run the game**:
   ```bash
   java PokerGame
   ```

## Usage
1. Start the game by running the `PokerGame` class.
2. You will be prompted to enter the number of players and their names.
3. Players will take turns placing bets, replacing cards, and playing rounds.
4. Follow the on-screen instructions to play the game.

## Features
- Shuffles and deals cards to players.
- Allows players to bet chips, fold, or check.
- Handles card replacement for strategic play.
- Evaluates hands to determine the winner after betting rounds.
- Supports multiple rounds of play until a player decides to exit.

## Dependencies
This project is a standalone Java application and does not require any external libraries or dependencies.

## Project Structure
The project consists of a single Java file which contains all the necessary classes and logic to operate the poker game:
- **PokerGame.java**: Contains the main logic for the game, including classes for `Card`, `Deck`, `Player`, and `HandEvaluator`.
  - **Card**: Represents a playing card with a suit, rank, and value.
  - **Deck**: Manages a deck of cards, including shuffling and drawing cards.
  - **HandEvaluator**: Evaluates the strength of a player's hand.
  - **Player**: Represents a player in the game, managing their hand, chips, and game actions.

### Game Flow
1. Players are initialized and cards are dealt.
2. Two betting rounds occur.
3. Players may replace cards.
4. The winner is determined based on the evaluated hand strength.
5. The game continues until players decide to stop or only one remains.

Enjoy playing Five Card Draw Poker!
```
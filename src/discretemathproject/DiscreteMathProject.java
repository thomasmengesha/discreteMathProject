
package discretemathproject;

//Kenneth Smith - Discrete Math Project - Spring 2010

import java.util.Scanner;
import java.io.*;
import java.util.*;
import static javax.swing.JOptionPane.*;


public class DiscreteMathProject
{
    public static Scanner keyboard = new Scanner(System.in);
    public static void main(String[] args)
    {
		/*Begin Test Program
		String stuff = "";
		Deck game = new Deck();
		game.newGame();
		game.shuffleDeck();
		Human person = new Human(game);
		Computer bot = new Computer(game, person);
		System.out.print("Cards in List: "+ game.CardsLeft());
		int l = 52;
		int j = 1;
		while(l != 0)
		{
			Card card1 = game.DealCard();
			System.out.print("\nCard:\t"+j+"\t"+card1.toString()+ " " + card1.getSymbolString() + " " +"\n" );
			l--;
			j++;
		}
		System.out.print("\n\n\tCards Left in List: " + game.CardsLeft() + "\n");
		person.startGame();
		if(person.playerPoints() > 21)
				stuff = "You lost!";
		else if (person.playerPoints() == 21)
		{
			stuff = "You won!";
			person.winnings(person.playerBetAmount());
		}
		bot.startGame();
		if(bot.playerPoints() <= 21 && bot.playerPoints() >= person.playerPoints())
			stuff = "You lost!";
		else
		{
			stuff = "You won!";
			person.winnings(person.playerBetAmount());
		}
		System.out.print("\n\nYour bankroll:\t" + person.bankRoll());
		End Test Program*/


		int answer;									  //Represents the user's answer to various questions in the game
		Deck game = new Deck();						  //Linked list to be used as game deck
		Human humanoid = new Human(game);			  //Human type object, used to represent the user in the game
		Computer ai = new Computer(game, humanoid);	  //Computer type object, used to represent the AI in the game

		game.newGame();		  //Builds initial game deck
		game.shuffleDeck();	  //Shuffles initial game deck

		while(true)
		{
			String stuff = "";     		//Used for concatenation of dialog boxes
			ai.startGame();		   		//Begins game for computer player
			humanoid.startGame();  		//Begins game for human player
			humanoid.continuePlay(ai);  //continues game play after initial deal
			ai.continuePlay();

			//Sets 'stuff' string based on player's score
			if(humanoid.playerPoints() > 21)
				stuff = "You lost!";
			else if (humanoid.playerPoints() == 21)
			{
				stuff = "You won!";
				humanoid.winnings(humanoid.playerBetAmount());
			}

			//Sets 'stuff' string if player decides to hold at a value < 21
			else
			{
				if(ai.playerPoints() <= 21 && ai.playerPoints() >= humanoid.playerPoints())
					stuff = "You lost!";
				else
				{
					stuff = "You won!";
					humanoid.winnings(humanoid.playerBetAmount());
				}
			}

			//Determines if the player has enough money to continue play, if true, player is then given
			//the option to play another hand.  If false, the player is told he/she is out of money and
			//given the option to start a new game.
			if(humanoid.bankRoll() > 0)
			{
				answer = showConfirmDialog(null, stuff + "\nDeal Again?");
				if(answer != 0 )  //If user selects 'no' or 'cancel', breaks loop
					break;
			}

			else
			{
				showMessageDialog(null, stuff + "\nYou're out of Money!");
				answer = showConfirmDialog(null, stuff + "\nNew game?");
				if (answer !=0)  //If user selects 'no' or 'cancel', breaks loop
				{
					break;
				}
			}
		}
	}
}

enum CardValue  //enum class for the card value constants
{
	TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"),		//Values are set by index (location value, i.e. TWO = 0, THREE = 1, etc...).
	SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"),	//Note that jack - ace are then equal to 9 - 12 respectively.
	QUEEN("Q"), KING("K"), ACE("A");

	public final int val = ordinal() + 2;  //Adjusts value of card equal to index (+2, TWO = 0 + 2, THREE = 1 + 2, etc...)
	public final String symbol;            //Symbolic value of the card's value

	CardValue(String sym) {symbol = sym;}
}

enum CardSuit //enum class for the card suit constants
{
	SPADES('\u2660'), DIAMONDS('\u2666'),	//Stores the unicode value for each suit symbol
	HEARTS('\u2665'), CLUBS('\u2664');

	public final char symbol;  //Stores the symbol for each suit

	CardSuit(char c) {symbol = c;}
}

class Card
{
	private CardValue _facevalue;  //Enum type data member for "value" of card object
	private CardSuit _suit;        //Enum type data member for suit of card object

	public Card(CardSuit suit, CardValue value)  //Card Object Constructor
	{
		_suit = suit;
		_facevalue = value;
	}

	public CardValue value()  //Returns enum of "value" of the card object for which it is called
	{
		return _facevalue;
	}

	public CardSuit suit()  //Returns enum of suit of the card object for which it is called
	{
		return _suit;
	}

	public String toString()  //Returns string containing the "value" and suit of card object
	{
		return _suit.toString() + " " + _facevalue.toString();
	}

	public String getSymbolString()  //Returns string constaining the symbolic representation of each card object
	{
		return _facevalue.symbol + _suit.symbol;
	}
}

class Deck
{
	private List<Card> gameDeck = new LinkedList<Card>();  //Linked List of Card Objects

	public int CardsLeft()  //Returns the amount (int) of cards remaing in the deck (list)
	{
		return gameDeck.size();
	}

	public void foldHand()  //Used to clear the list created to represent the hand of the player and the computer
	{
		gameDeck.clear();
	}

	public void shuffleDeck()  //Puts the elements of the deck (list) in a psuedorandom order
	{
		Collections.shuffle(gameDeck);
	}
	public void newTopCard(Card first)  //Adds a card to the top (index location: 0) of the deck (list)
	{
		gameDeck.add(0, first);
	}

	public void newGame() //Removes all the elements from the previous deck (list), then builds a new deck (list)
	{
		gameDeck.clear();
		for(CardSuit _suit : CardSuit.values())				//Cycles thru each suit, 1 at time until all suits are used
			for(CardValue _facevalue : CardValue.values())	//Cycles thru each value, 1 at time until all value are used
				gameDeck.add(new Card(_suit, _facevalue));	//Assigns each card one value and one suit
	}

	public Card DealCard()  //Returns the card on top of the deck, and then removes the card from the deck (list)
	{
		Card dealtCard = gameDeck.get(0);	//Retrieves the top card from the deck
		gameDeck.remove(0);					//Removes the dealt card from the deck
		return dealtCard;
	}

	public Card removeOffTop() //Removes element of the list at index location 0 (the top card of the deck)
	{
		Card theCard = gameDeck.get(0);	//Retrieves the top card from the deck
		gameDeck.remove(0);				//Removes the top card from the deck
		return theCard;
	}

	public Card searchAt(int n) //Returns Element at given location within deck/hand (list)
	{
			return gameDeck.get(n);
	}

	public int search(CardSuit _suit, CardValue _facevalue)  //Compares a card to element of the list. If found returns 1, if not found returns -1
	{
		int i = 0;

		for(Card theCard : gameDeck)	//Cycles through the deck
			if(theCard.suit() == _suit  && theCard.value()== _facevalue)	//Compares the card to the suit & value of each card in deck
				i = 1;	//found
			else
				i = -1;	//not found

		return i;
	}
}

abstract class Player
{
	protected Deck _gameDeck;          //Linked list of cards that represent the entire deck of cards
	protected Deck _hand = new Deck(); //Linked list of cards that are held in the player's hand
	protected int _points;             //Points assigned to player based on cards (elements) in player's hand (list)
	protected String _cardValue;       //value of card in hand
	protected int _bet;                //player's current bet during the hand
	protected int _cardCountThisHand;  //Hi-Lo count for each hand
	protected int _deckCount;          //Hi-Lo count for the entire deck for the entire game

	public Player(Deck game) 		   //Player Constructor
	{
		_gameDeck = game;
		_cardCountThisHand = 0;
		_deckCount = 0;
	}

	public void newGame()	//Empty constructor for a new game
	{
		_points = 0;
		_cardValue = "";
		_bet = 25;
		_cardCountThisHand = 0;
		_hand.foldHand();
	}

	public int playerPoints()	//Returns the number of points for each "player" object
	{
		return _points;
	}

	public int playerBetAmount()
	{
		return _bet;
	}

	public abstract void startGame();	//Abstract method, internal algorithms defined in Human and Computer classes

	public Card hit()	//Takes the top card from the game deck (list) and returns it to be copied to
	{				    //the 0 index of the hand of the player,

		int numAces = 0;  //Keeps track of number of aces that have been dealt in game
		_points = 0;

		if(_gameDeck.CardsLeft() == 0)  //If deck is empty, forces build/shuffle of new deck and resets the counts
		{
			_gameDeck.newGame();		//Creates new deck
			_gameDeck.shuffleDeck();	//Shuffles new deck
			_deckCount = 0;
			_cardCountThisHand = 0;
		}

		Card next = _gameDeck.DealCard();            //Creates a new card, and assigns it the value of the first card in the list then
		_hand.newTopCard(next);                      //adds the new card to the player's hand.
		_cardValue += next.getSymbolString() + "";	 //Sets string value to card value

		for(int j = 0; j < _hand.CardsLeft(); j++)
		{
			CardValue compare = _hand.searchAt(j).value(); //Compares the value of each card in hand with enum value, then assigns points
			_points += compare.val;	//Sums player's points value

			if(j == 0)	//ensures that cards are only counted once for card count
				_cardCountThisHand = 0;

			if(compare == CardValue.TWO || compare == CardValue.THREE || compare == CardValue.FOUR ||   //Compares card to value in enum class, if true then
			   compare == CardValue.FIVE || compare == CardValue.SIX)									//_cardCountThisHand = _cardCountThisHand + 1
					_cardCountThisHand++;

			if(compare == CardValue.TEN || compare == CardValue.JACK || compare == CardValue.QUEEN ||	//Compares card to value in enum class. if true then
			   compare == CardValue.KING || compare == CardValue.ACE)									//_cardCountThisHand = _cardCountThisHand - 1
		   	{
					_cardCountThisHand--;
					if(compare == CardValue.JACK)		//Adjusts enum value of jack = 11 to jack = 10
						_points = _points - 1;
					if(compare == CardValue.QUEEN)		//Adjusts enum value of queen = 12 to queen = 10
						_points = _points - 2;
					if(compare == CardValue.KING)		//Adjusts enum value of king = 13 to king = 10
						_points = _points - 3;
					if(compare == CardValue.ACE)		//Adjusts enum value of ace = 14 to ace = 11
						_points = _points - 3;
			}

			if(compare == CardValue.ACE)  //If the card drawn is an 'Ace', adds one to aces, used to determine value of aces in hand
				numAces++;
		}
		for(int k = 1; k <= numAces && _points > 21; k++)  //If player's score > 21, sets value of each ace from 11 to 1
			_points -= 10;

		return next;
	}
}

class Human  extends Player
{
	private int _money;  //Human player's bankroll

	public Human(Deck gameDeck)  //Human constructor, sends gameDeck to super class to enable use of super class' methods
	{
		super(gameDeck);
		_money = 500;
	}

	public void winnings(int amount)  //Given the bet amount per hand, and if player wins, then sets _money = to current value + 2 * bet amount
	{
		_money = _money + (amount * 2);
	}

	public int bankRoll()  //Returns the amount of money in the player's bankroll
	{
		return _money;
	}

	public void startGame()  //Definition of human implentation of abstract startGame() method in super class
	{
		newGame();  //newGame() method from Player class

		if(_money <= 0)		//Resets bankroll only if the player starts a new game, not a new hand
			_money = 500;

		for(int k = 0; k != 1; k++)  //Automatically deals the player 2 cards at the beginning of a hand
			hit();
	}

	public void continuePlay(Player comp)  //Continues play for the player after the initial card is dealt
	{
		while(_points < 21)
		{
			hit();
			_deckCount = _deckCount + _cardCountThisHand + comp._deckCount;;  //Deck count is continually updated from each hand
			if(_points < 21)
			{
				//Gives the user information on the current hand, then the option to raise the bet amount based on that information
				int answer1 = showConfirmDialog(null, "You got : " + _cardValue + "\nYou have " + _points + " points."
												+ "\n\nThe Computer is showing: " + comp._cardValue
												+ "\nDo you want to raise your bet?\n\n\nCurrent Bankroll: " + _money
												+ "\nCurrent Bet Amount: " + _bet + "\nCurrent Hand Count: " + _cardCountThisHand
												+ "\nCurrent Deck Count: " + _deckCount);

				if(answer1 == 0)  //If the user decides to raise the bet amount, this ensures that the player has enough money to raise the bet
				{
					if(_money > 25)
					{
						_bet = _bet + 25;
						_money = _money - _bet;
					}
					else
						showMessageDialog(null, "You don't have enough money!\n"); //Displayed only if the player doesn't have the funds to raise the bet
				}

				//Gives the user the option to be dealt another card
				int answer2 = showConfirmDialog(null, "You got : " + _cardValue +"\nand have "
												+ _points + " points.\n\nCurrent Bet: " + _bet + "\n\nDo you want another card?");

				if(answer2 == 1)  //If user selects 'no' to be dealt another card, breaks loop
					break;
				else if(answer2 == 2)  //If user selects 'cancel', exits program
					System.exit(0);
			}
			else
				showMessageDialog(null, "You got : " + _cardValue +      //Displays information on the result of the hand
						          "\nand have " + _points + " points.");
		}
	}
}

class Computer extends Player
{
	private Player human; //Computer object of the player type

	public Computer(Deck gameDeck, Player person)  //Computer player constructor, sends gameDeck to super class to enable
	{												 //use of super class' methods
		super(gameDeck);
		human = person;
	}

	public void startGame()  //Definition of computer implementation of abstract startGame() method in super class
	{
		newGame();  //newGame() method from Player class

		for(int k = 0; k != 1; k++)  //Automatically deals the computer 2 cards at the beginning of a hand
			hit();
			_deckCount = _cardCountThisHand;
	}

	public void continuePlay() //continues play for the player after the initial card is dealt
	{
		//Controls the computer's strategy.  The computer will 'hit' if score is less than human's score, if computer's
		//score is not greater than 17 points, and if the human's score is not greater than 21 points as defined
		//by the "house rules for blackjack"
		while(_points < 21 && _points < human.playerPoints() && _points <= 16)
			hit();
			showMessageDialog(null, "The computer got: " + _cardValue +
				"\nand has " + _points + " points.");
			_deckCount = _deckCount + _cardCountThisHand;
	}
}

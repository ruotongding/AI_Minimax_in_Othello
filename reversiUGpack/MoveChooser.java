import java.util.ArrayList;

public class MoveChooser {
	private static final int INFINITY = 999999;
	private static int staticValue=0;
	static int bestScore;
	public static int bestMove;



    public static Move chooseMove(BoardState boardState){


    	int searchDepth= Othello.searchDepth;

        	ArrayList<Move> moves= boardState.getLegalMoves();
        	if(moves.isEmpty()){
        		return null;
        	}

				
        int moveChoice = getBestMove(boardState,searchDepth,moves);
        return moves.get(moveChoice);
    }

    //static evaluation function
    public static int getStaticSum(BoardState boardState) {
    	staticValue = 0;
    	//create 2 variables to store weights of white and black
    	int whiteTotal=0;
    	int blackTotal=0;
    	//create static static value table
    	int staticVal[][] = {
        		{120,-20,20,5,5,20,-20,120},
        		{-20,-40,-5,-5,-5,-5,-40,-20},
        		{20,-5,15,3,3,15,-5,20},
        		{5,-5,3,3,3,3,-5,5},
        		{5,-5,3,3,3,3,-5,5},
        		{20,-5,15,3,3,15,-5,20},
        		{-20,-40,-5,-5,-5,-5,-40,-20},
        		{120,-20,20,5,5,20,-20,120},
        };
    	for(int i=0; i<8; ++i) {
    		for(int j=0;j<8; j++) {
    			//check position white or black
    			int posState = boardState.getContents(i, j);
    			//position white
    			if(posState==1) {
    				whiteTotal+=staticVal[i][j];
    			}
    			//position black
    			if(posState==-1) {
    				blackTotal+=staticVal[i][j];
    			}
    			//as empty position is value 0 so we can ignore
    		}
    	}
    	//calculate the static value
    	staticValue=whiteTotal-blackTotal;
    	return staticValue;
    }


    //minimax function with alpha and beta pruning
    public static int minimax(BoardState boardState,int searchDepth, ArrayList<Move> moves, Boolean maxPlayer,int alpha, int beta) {
    	//depth 0 situation
    	if(searchDepth==0) {
    		bestScore = getStaticSum(boardState);
    		return bestScore;
    	}

    	//white move, maximising node
    	else if(maxPlayer) {
    		//initialize maxEva
    		int maxEva = -INFINITY;
    		//check each daughter
    		for(int i=0; i<moves.size(); i++) {
    			//create a new board to store new boardstate
    			BoardState boardState1 = boardState.deepCopy();
    			//set new boardstate
    			boardState1.setContents(moves.get(i).x, moves.get(i).y, 1);
    			//get new legal moves
    			ArrayList<Move> moves1= boardState1.getLegalMoves();
    			//call minimax function recursively
    			int eva = minimax(boardState1,searchDepth-1,moves1,false,alpha,beta);
    			//change max evaluation value
    			if(eva>maxEva) {
    				maxEva = eva;
    			}
    			//change the value of alpha
    			if(eva>alpha) {
    				alpha = eva;
    			}
    			//stop processing if beta<=alpha
    			if(beta<=alpha) {
    				break;
    			}
    		}
    		return maxEva;
    	}
    	//black move, minimising node
    	else {
    		//initialize minEva
    		int minEva = INFINITY;
    		//check each daughter
    		for(int i=0; i<moves.size(); i++) {
    			//create a new board to store new boardstate
    			BoardState boardState1 = boardState.deepCopy();
    			//set new boardstate
    			boardState1.setContents(moves.get(i).x, moves.get(i).y, -1);
    			//get new legal moves
    			ArrayList<Move> moves1= boardState1.getLegalMoves();
    			//call minimax function recursively
    			int eva = minimax(boardState1,searchDepth-1,moves1,true,alpha,beta);
    			//change minimum evaluation value
    			if(eva<minEva) {
    				minEva = eva;
    			}
    			//change beta
    			if(eva<beta) {
    				beta=eva;
    			}
    			//stop processing if beta<=alpha
    			if(beta<=alpha) {
    				break;
    			}
    		}
    		return minEva;
    	}

    }



    //function to get best move based on the minimax values;
    public static int getBestMove(BoardState boardState, int searchDepth, ArrayList<Move> moves) {
    	//create score to store each daughters minimax value
    	int score[] = new int[moves.size()];
    	for(int i=0; i<moves.size(); i++) {
    		//copy the boardstate to process
    		BoardState boardState1=boardState.deepCopy();
    		//make the next moves
    		boardState1.setContents(moves.get(i).x, moves.get(i).y, 1);
    		//get the next legal moves
    		ArrayList<Move> moves1= boardState1.getLegalMoves();
    		//use minimax to find the minimax value of next node
    		score[i]=minimax(boardState1, searchDepth-1, moves1, false,-INFINITY,INFINITY);
    	}
    	//initialize best move
    	bestMove=0;
    	//check values of each daughter
    	for(int i=0; i<moves.size();i++) {
    		int tempBest = score[i];
    		//find the max value daughter and make the best move
    		if(i>0&&i<moves.size()&&tempBest>score[i-1]) {
    			bestMove=i;
    		}
    	}

    	return bestMove;
    }

}

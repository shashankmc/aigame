import java.util.concurrent.ThreadLocalRandom;

import game.Game;
import main.collections.FastArrayList;
import other.move.Move;
import other.context.Context;
import main.FileHandling;
import main.collections.FVector;
import other.state.State;
import other.trial.Trial;
import utils.AIUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import other.AI;

//must have functions - selectAction, initAI, closeAI (may not be necessary)

public class ABAI extends AI{
	public interface ContextCopyInterface
	{
		/**
		 * @param context
		 * @return A copy of the given context
		 */
		public Context copy(final Context context);
	}
	protected int player = -1;
	protected volatile boolean wantsInterrupt = false;
	protected FastArrayList<Move> currentRootMoves = null;
	protected FastArrayList<Move> sortedRootMoves = null;
	public static final float alpha_init = -100000.f;
	public static final float beta_init = -alpha_init;
	boolean playerWon = false;
	protected Context lastSearchedRootContext = null;
	Move bestMoveFinal = null;
	
	public void setWantsInterrupt(final boolean val)
	{
		wantsInterrupt = val;
	}
	
	
	public ABAI() {
		this.friendlyName = "Shashank's ABAI";
		
	}
	public Move selectAction(final Game game, final Context context, final double maxSeconds, 
			final int maxIterations, final int maxDepth)
	{
		playerWon = false;
		final int depthLimit = maxDepth > 0 ? maxDepth : Integer.MAX_VALUE;
		lastSearchedRootContext = context;
		float score = alpha_init;
		
		final int initDepth = 2;
		if (maxSeconds > 0)
		{
			final long startTime = System.currentTimeMillis();
			final long stopTime = startTime + (long) (maxSeconds * 1000);
			
			final long currentTime = System.currentTimeMillis();
			
			score = alphaBetaNegamax(context, depthLimit, alpha_init, beta_init, 1, stopTime);
			//return lastMove;
		}
		else {
			// lastMove = alphaBetaSearch()
			score = alphaBetaNegamax(context, depthLimit, alpha_init, beta_init, 1, 1000);
			//return lastMove;
		}
		return bestMoveFinal;
	}
	public float alphaBetaNegamax(final Context context, final int depth, final float inAlpha, final float inBeta,
			final int maximisingPlayer, final long stopTime) {
		final Trial trial = context.trial();
		final State state = context.state();
		final Game game = context.game();
		final float orgAlpha = inAlpha;
		float alpha = inAlpha;
		float beta = inBeta;
		FastArrayList<Move> legalMoves = game.moves(context).moves();
		
		currentRootMoves = new FastArrayList<Move>(game.moves(context).moves());
		Move bestMove = legalMoves.get(0); 
		if (depth == 0) {
			return -1.f; //need to create evaluation function and return that
		}
		float score = alpha_init;
		for (int i = 1; i <= legalMoves.size(); i++) {
			final Context copyContext = copyContext(context);
			final Move m = legalMoves.get(i);
			game.apply(copyContext, m);
			float value = -alphaBetaNegamax(copyContext, depth-1, -inBeta, -inAlpha, maximisingPlayer, stopTime);
			if (System.currentTimeMillis() >= stopTime || wantsInterrupt) {
				bestMoveFinal = bestMove;
				break;
			}
			if (value > score) {
				score = value;
				bestMove = m;
			}
			if (score > alpha) alpha = score;
			if (score >= beta) break;
		}
		return score;
	}
	public float alphaBeta(final Context context, final int depth, 
			final float inAlpha, final float inBeta, final int maximisingPlayer, final long stopTime) {
		final Trial trial = context.trial();
		final State state = context.state();
		
		final float orgAlpha = inAlpha;
		float alpha = inAlpha;
		float beta = inBeta;
		float score = -1.f;
		final Game game = context.game();
		final int mover = state.playerToAgent(state.mover());
		FastArrayList<Move> legalMoves = game.moves(context).moves();
		final int numLegalMoves = legalMoves.size();
		Move bestMove = legalMoves.get(0);
		// depth check?
		if (mover == maximisingPlayer) {
			score = alpha_init;
			for (int i =0; i < numLegalMoves; ++i)
			{
				final Context copyContext = copyContext(context);
				final Move m = legalMoves.get(i);
				game.apply(copyContext, m);
				final float value = alphaBeta(copyContext, depth - 1, alpha, beta, maximisingPlayer, stopTime);
				if (System.currentTimeMillis() >= stopTime || wantsInterrupt) {
					bestMoveFinal = bestMove;
					return 0;
				}
				if (value > score) {
					bestMove = m;
					score = value;
				}
				if (score > alpha) {
					alpha = score;
				}
				if (alpha >= beta) {
					break;
				}
			}
			bestMoveFinal = bestMove;
		}
		else {
			score = beta_init;
			for (int i = 0; i  < numLegalMoves; ++i) {
				final Context copyContext = copyContext(context);
				final Move m = legalMoves.get(i);
				game.apply(copyContext, m);
				final float value = alphaBeta(copyContext, depth - 1, alpha, beta, maximisingPlayer, stopTime);
				if (System.currentTimeMillis() >= stopTime || wantsInterrupt) {
					return 0;
				}
				if (value < score) {
					bestMove = m;
					score = value;
				}
				if (score < beta) {
					beta = score;
				}
				if (alpha >= beta) {
					break;
				}
			}
			bestMoveFinal = bestMove;
		}
		return score;
	}
	@Override
	public void initAI(final Game game, final int playerID) {
		this.player = playerID;
		
	}
}


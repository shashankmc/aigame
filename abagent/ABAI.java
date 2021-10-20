package shashankAI;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import game.Game;
import game.functions.ints.count.component.CountPieces;
import game.types.board.SiteType;
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
import other.concept.Concept;
import game.util.math.Count;
import other.state.owned.Owned;
import other.location.Location;
import main.collections.FVector;
import game.equipment.component.Component;


public class ABAI extends AI{
	boolean playerWon = false;
	protected Context lastSearchedRootContext = null;
	public static final float alpha_init = -10000000.f;
	public static final float beta_init = -alpha_init;
	Move bestMoveFinal = null;
	@Override
	public Move selectAction(Game game, Context context, double maxSeconds, int maxIterations, int maxDepth) {
		// TODO Auto-generated method stub
		playerWon = false;
		final int depthLimit = maxDepth > 0 ? maxDepth : Integer.MAX_VALUE;
		lastSearchedRootContext = context;
		float score = alpha_init;
		
		final int initDepth = 0;
		if(maxSeconds > 0) {
			final long startTime = System.currentTimeMillis();
			final long stopTime = startTime + (long) (maxSeconds * 1000);
			final long currentTime = System.currentTimeMillis();
			score = alphaBeta(context, depthLimit, alpha_init, beta_init, 1, stopTime);
		}
		else {
			score = alphaBeta(context, depthLimit, alpha_init, beta_init, 1, 1000);
		}
		//global variable updated in alphabeta func.
		return bestMoveFinal;
	}
	public float alphaBeta(final Context context, final int depth, final float inAlpha, final float inBeta, final int maxPlayer, final long stopTime) {
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
		//AM I GOING TO CHECK THE DEPTH!!
		if (mover == maxPlayer) {
			score = alpha_init;
			for (int i =0; i < numLegalMoves; ++i) {
				final Context dupContext = copyContext(context);
				final Move m = legalMoves.get(i);
				game.apply(dupContext, m);
				final float value = alphaBeta(dupContext, depth-1, alpha, beta, maxPlayer, stopTime);
				if (System.currentTimeMillis() >= stopTime) {
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
			for(int i = 0; i < numLegalMoves; ++i) {
				final Context dupContext = copyContext(context);
				final Move m = legalMoves.get(i);
				game.apply(dupContext, m);
				final float value = alphaBeta(dupContext, depth-1, alpha, beta, maxPlayer, stopTime);
				if (System.currentTimeMillis() >= stopTime) {
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
	public float letsEvaluate(final Context context, final Game game, final int maxPlayer) {
		//will give values to the pawns
		//question is how do I check what type of pawn it is.
		final Owned owned = context.state().owned();
		final List<? extends Location>[] pieces = owned.positions(maxPlayer);
	
		return 0.1f;
	}
}
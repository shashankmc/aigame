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


public class ABAI{
	protected int player = -1;
	
	public static final float alpha_init = -100000.f;
	public static final float beta_init = -alpha_init;
	boolean playerWon = false;
	protected Context lastSearchedRootContext = null;
	public ABAI() {
		friendlyName = "Shashank's ABAI";
		
	}
	public Move selectAction(final Game game, final Context context, final double maxSeconds, final int maxIterations, final int maxDepth)
	{
		playerWon = false;
		final int depthLimit = maxDepth > 0 ? maxDepth : Integer.MAX_VALUE;
		lastSearchedRootContext = context;
		
		final int initDepth = 2;
		if (maxSeconds > 0)
		{
			final long startTime = System.currentTimeMillis();
			final long stopTime = startTime + (long) (maxSeconds * 1000);
			
			final long currentTime = System.currentTimeMillis();
			
			//lastMove = alphaBetaSearch()
			//return lastMove;
		}
		else {
			// lastMove = alphaBetaSearch()
			//return lastMove;
		}
		
	}
	public float alphaBeta(final Context context, final int depth, 
			final float inAlpha, final float inBeta, final int maximisingPLayer, final long stopTime) {
		final Trial trial = context.trial();
		final State state = context.state();
		
		final float orgAlpha = inAlpha;
		float alpha = inAlpha;
		float beta = inBeta;
		
		fi
	}		
	
}


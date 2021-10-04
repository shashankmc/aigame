import java.util.concurrent.ThreadLocalRandom;

import game.Game;
import main.collections.FastArrayList;
import other.AI;
import other.context.Context;
import other.move.Move;
import utils.AIUtils;


public class ABAI extends AI{
	protected int player = -1;
	public static final float alpha_init = -100000.f;
	public static final float beta_init = 100000.f;
	
	public ABAI() 
	{
		this.friendlyName = "Alpha beta trial";
	}
	
	@Override
	public Move selectAction
	(
			final Game game,
			final Context context,
			final double maxSeconds,
			final int maxIterations,
			final int maxDepth
	)
	{
		FastArrayList<Move> legalMoves = game.moves(context).moves();
		
		if(!game.isAlternatingMoveGame())
			legalMoves = AIUtils.extractMovesForMover(legalMoves, player);
		
		final int r = ThreadLocalRandom.current().nextInt(legalMoves.size());
		return legalMoves.get(r);
	}
	
	@Override
	public void initAI(final Game game, final int playerID) 
	{
		this.player = playerID;
	}
	
			
	
}


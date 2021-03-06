import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.LinkedList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Engine{

	static class Move{
		int type = 0;
		Car first;
		Car last;
		int index;
	}

	private Map selectedMap;
	private GameTimer timer;
	private GameSolver hint;
	private StarManager stars;
	private GameManager gameManager;
	private LinkedList<Move> moves;
	private int volume;

	public Engine(GameManager manager) {
		gameManager = manager;
		stars = new StarManager();
		moves = new LinkedList<Move>();
		volume = 50;
		selectedMap = null;
	}


	/**
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	// public boolean moveCar(int x1, int y1, int x2, int y2) {
	// 	// TODO - implement Engine.moveCar
	// 	// throw new UnsupportedOperationException();
	// 	Node newNode = new Node();
	// 	newNode.x1Move = x1;
	// 	newNode.y1Move = y1;
	// 	newNode.x2Move = x2;
	// 	newNode.y2Move = y2;
	// 	if(!(moves.getLast().equals(newNode))) {
	// 		moves.add(newNode);
	// 		return true;
	// 	}
	// 	return false;
	// }

	/**
	 *
	 * @param clock
	 */
	public boolean gameLost(GameTimer clock) {
		// TODO - implement Engine.gameLost
//		throw new UnsupportedOperationException();
		/*if(clock.getOutOfTime())
		{
			return true;
		}*/
		return false;
	}

	public boolean gameWon() {
		// TODO - implement Engine.gameWon
		// throw new UnsupportedOperationException();
		Car player = selectedMap.getPlayer();
		if(player.getY() == selectedMap.getDimension()-2)
		{
			gameManager.getDataStorage().updateOpenMaps(selectedMap.getDimension(),gameManager.getOpenMapsArray());
			return true;
		}
		return false;
	}


	public void updateStars() {
		// TODO - implement Engine.updateStars
		int starsGained = calculateStars();
		stars.increaseStars(starsGained);
		//throw new UnsupportedOperationException();
	}

	public int calculateStars() {
		int starsReturned = 0;
		// TODO - implement Engine.calculateStars
		//throw new UnsupportedOperationException();
		if(timer.getTimerOn() == false)
		{
			if(moves.size() - selectedMap.getMinMoveCount() <= 4)
			{
				starsReturned = stars.getThreeStars();
				//stars.increaseStars(stars.getThreeStars());

			}
			if(moves.size() - selectedMap.getMinMoveCount() > 4 && moves.size() - selectedMap.getMinMoveCount() <= 7)
			{
				starsReturned = stars.getTwoStars();
				//stars.increaseStars(stars.getTwoStars());
			}
			if(moves.size() - selectedMap.getMinMoveCount() > 7)
			{
				starsReturned = stars.getOneStar();
				//stars.increaseStars(stars.getOneStar());
			}
		}
		else if(timer.getTimerOn() == true)
		{
			int difficulty = selectedMap.getDifficulty();
			/*if(timer.getOutOfTime())
			{
				starsReturned =  0;
			}*/
			//if(!timer.getOutOfTime()) {
				if(timer.getTimeSpent() <= difficulty * 120000/4)
				{
					starsReturned = stars.getThreeStars();
				}
				else if(timer.getTimeSpent() > difficulty * 120000/ 4 && timer.getTimeSpent() <= difficulty * 120000 /3) {
					starsReturned = stars.getTwoStars();
				}
				else if(timer.getTimeSpent() > difficulty * 120000/ 3 && timer.getTimeSpent() <= difficulty * 120000) {
					starsReturned = stars.getOneStar();
				}
			//}
		}
		return starsReturned;
	}

	public void playSound() {
		// TODO - implement Engine.playSound
//		throw new UnsupportedOperationException();

		if(gameWon()) {
//			Media gameWonSound = new Media(new File("gameWonSound.mp3").toURI());
//			MediaPlayer mediaPlayer = new MediaPlayer(gameWonSound);                                  IN THE FILE MANAGER
			gameManager.getDataStorage().getSoundEffects()[0].play();
		}
		else if(gameLost(timer)) {
//			Media gameLostSound = new Media(new File("gameLostSound.mp3").toURI());
//			MediaPlayer mediaPlayer = new MediaPlayer(gameLostSound);					    		  IN THE FILE MANAGER
			gameManager.getDataStorage().getSoundEffects()[1].play();
		}
	}

//	public int getHints() {
//		// TODO - implement Engine.getHints
//		throw new UnsupportedOperationException();
//	}

	public void undo(Car first, int index, int type) {
		if ( type == 0 || type == 2) {
			selectedMap.getCars()[index] = first;
		} else if ( type == 1 ) {
			Car[] cars = selectedMap.getCars();
			Car[] newCars = new Car[cars.length + 1];
			for ( int i = 0; i < index; i++ )
				newCars[i] = cars[i];
			newCars[index] = first;
			for ( int i = index+1; i < newCars.length; i++)
				newCars[i] = cars[i-1];
			updateBlockinfo();
			selectedMap.setCars(newCars);
		} else if( type == 3) {
			selectedMap.getCars()[index] = first.clone();
		}
		updateBlockinfo();
	}

	public Car[] getLastMove() {
		if ( moves.size() > 0 ) {
			Move temp = moves.pollFirst();
			Car[] move = new Car[2];
			move[0] = temp.first;
			move[1] = temp.last;
			return move;
		}
		return null;
	}

	public int[] getLastMoveData() {
		if ( moves.size() > 0 ) {
			int[] res = new int[2];
			res[0] = moves.peek().type;
			res[1] = moves.peek().index;
			return res;
		}
		return null;
	}

	public Map getSelectedMap() {
		return this.selectedMap;
	}

	/**
	 *
	 * @param selectedMap
	 */
	public void setSelectedMap(Map selectedMap) {
		this.selectedMap = selectedMap;
		timer = new GameTimer(selectedMap.getTime());
	}

	public GameTimer getTimer() {
		return this.timer;
	}

	/**
	 *
	 * @param timer
	 */
	public void setTimer(GameTimer timer) {
		this.timer = timer;
	}

	public GameSolver getHint() {
		return this.hint;
	}

	/**
	 *
	 * @param hint
	 */
	public void setHint(GameSolver hint) {
		this.hint = hint;
	}

	public void createHint() {
		if ( selectedMap != null )
			hint = new GameSolver(selectedMap);
	}

	public LinkedList<int[]> getHints() {
		return hint.generateHint(true);
	}

	public int[] getStars() {
		return this.stars.getStars();
	}

	/**
	 *
	 * @param stars
	 */
	public void setStars(StarManager stars) {
		this.stars = stars;
	}

	public int getExitY(){
		Block[][] arr = selectedMap.getBlocks();
		for(int i = 0; i < arr.length; i++){
			for (int j = 0; j<arr.length; j++){
				if (arr[i][j].isFinishBlock()){
					return j;
				}
			}
		}
		return -1;
	}

	public int getExitX(){
		Block[][] arr = selectedMap.getBlocks();
		for(int i = 0; i < arr.length; i++){
			for (int j = 0; j<arr.length; j++){
				if (arr[i][j].isFinishBlock()){
					return i;
				}
			}
		}
		return -1;
	}
	public void reset() {
		moves = new LinkedList();
		timer.cancelTimer();
		timer = new GameTimer(selectedMap.getTime());
	}

	public LinkedList getMoves() {
		return this.moves;
	}

	/**
	 *
	 * @param moves
	 */
	public void setMoves(LinkedList moves) {
		this.moves = moves;
	}

	public int getVolume() {
		return this.volume;
	}

	/**
	 *
	 * @param volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}


    public boolean updateCarX(Car car, int x) {
		if ( car.getX() != x ) {
			Move temp = new Move();
			temp.first = car.clone();
			temp.type = 0;
	        car.setHorizontalX( x, x + car.getLength() - 1);
			temp.last = car.clone();
			moves.addFirst(temp);
		}
        return true;
    }

    public boolean updateCarY(Car car, int y) {
		if (car.getY() != y ) {
			Move temp = new Move();
			temp.first = car.clone();
			temp.type = 0;
	        car.setVerticalY( y, y + car.getLength() - 1);
			temp.last = car.clone();
			moves.addFirst(temp);
		}
        return true;
    }

    public void updateBlockinfo(){
        Block[][] arr = selectedMap.getBlocks();
		Car[] cars = selectedMap.getCars();
        for ( int i = 0; i < selectedMap.getDimension(); i++) {
            for ( int j = 0; j < selectedMap.getDimension(); j++) {
                arr[i][j].setOccupied(false);
            }
        }
        for ( int i = 0; i < cars.length; i++) {
            if ( cars[i].getCarDirection() == 0 ) {
                for ( int j = 0; j < cars[i].getLength(); j++) {
                    arr[cars[i].getX()][cars[i].getY()+j].setOccupied(true);
                }
            } else if ( cars[i].getCarDirection() == 1 ) {
                for ( int j = 0; j < cars[i].getLength(); j++) {
                    arr[cars[i].getX()+j][cars[i].getY()].setOccupied(true);
                }
            }else if ( cars[i].getCarDirection() == 2 ) {
                for ( int j = 0; j < cars[i].getLength(); j++) {
                    arr[cars[i].getX()][cars[i].getY()+j].setOccupied(true);
                }
            }else {
                for ( int j = 0; j < cars[i].getLength(); j++) {
                    arr[cars[i].getX()+j][cars[i].getY()].setOccupied(true);
                }
            }
        }
		// for ( int i = 0; i < arr.length; i++ ) {
		// 	for ( int j = 0; j < arr.length; j++) {
		// 		int k = arr[i][j].isOccupied() ? 1 : 0;
		// 		System.out.print(k + " ");
		// 	}
		// 	System.out.println();
		// }
		// System.out.println();
    }

	public void blowUpCar( int toGo ) {
		Car[] cars = selectedMap.getCars();
		Car[] newCars = new Car[cars.length - 1];
		Car temp = cars[toGo];
		Move move = new Move();
		move.index = toGo;
		move.type = 1;
		move.first = temp.clone();
		move.last = null;
		moves.addFirst(move);
		for ( int i = 0; i < toGo; i++ )
			newCars[i] = cars[i];
		for ( int i = toGo+1; i < cars.length; i++)
			newCars[i-1] = cars[i];
		selectedMap.setCars(newCars);
		System.out.println("size: " + moves.size());
		//cars = selectedMap.getCars();
	}

	public int getCarNo() {
		return selectedMap.getCars().length;
	}

    public Car findCar(int x, int y) {
		Car[] cars = selectedMap.getCars();
        for ( int i = 0; i < cars.length; i++ ) {
            if ( x >= cars[i].getX() && x <= cars[i].getHorizontalX() ) {
                if ( y >= cars[i].getY() && y <= cars[i].getVerticalY() )
                return cars[i];
            }
        }
        return null;
    }

    public int findCarIndex(int x, int y) {
		Car[] cars = selectedMap.getCars();
        for ( int i = 0; i < cars.length; i++ ) {
            if ( x >= cars[i].getX() && x <= cars[i].getHorizontalX() ) {
                if ( y >= cars[i].getY() && y <= cars[i].getVerticalY() )
                return i;
            }
        }
        return -1;
    }

	public Car getCar(int index) {
		return selectedMap.getCars()[index];
	}

	public Car[] getCars() {
		return selectedMap.getCars();
	}

	public void shrinkCar( int index) {
		Car[] cars = selectedMap.getCars();
		Move move = new Move();
		move.first = cars[index].clone();
		move.type = 2;
		move.index = index;
		if ( cars[index].getCarDirection() == 1 || cars[index].getCarDirection() == 3)
			cars[index].setHorizontalX(cars[index].getX(), cars[index].getHorizontalX() - 1);
		else
			cars[index].setVerticalY(cars[index].getY(), cars[index].getVerticalY() - 1);
		cars[index].setLength( cars[index].getLength() - 1);
		move.last = cars[index].clone();
		moves.addFirst(move);
	}

	public int[] findMax( Car curr ) {
		Block[][] blocks = selectedMap.getBlocks();
		int moveForward = 0;
		int moveBackward = 0;
		if ( curr != null ) {
			if ( curr.getCarDirection() == 1 || curr.getCarDirection() == 3 ) {
				int i = curr.getX() - 1;
				while ( i > -1 && !blocks[i][curr.getY()].isOccupied() ) {
					moveForward++;
					i--;
				}
				int end = curr.getHorizontalX() + 1;
				while ( end < selectedMap.getDimension() && !blocks[end][curr.getY()].isOccupied()) {
					moveBackward++;
					end++;
				}
			} else {
				int i = curr.getY() - 1;
				while ( i > -1 && !blocks[curr.getX()][i].isOccupied()) {
					moveForward++;
					i--;
				}
				int end = curr.getVerticalY() + 1;
				while ( end < selectedMap.getDimension() && !blocks[curr.getX()][end].isOccupied()) {
					moveBackward++;
					end++;
				}
			}
		}
		int[] res = new int[2];
		res[0] = moveForward;
		res[1] = moveBackward;
		return res;
	}

	public Car rotateCar(int index){
		System.out.println(selectedMap);
		Car[] theMapCars = getCars();
		Car toChange = theMapCars[index];
		Car tmp; 
		int change, head, end,headY,endY;
		Move move = new Move();
		move.index = index;
		move.type = 3;
		move.first = theMapCars[index].clone();
		switch (toChange.getCarDirection()){
			case 0:
				head = toChange.getY();
				end = toChange.getVerticalY();
				change = theMapCars[index].getX()+toChange.getLength()-1;
		
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(3);
				tmp.setHorizontalX(theMapCars[index].getX() ,change);	
				tmp.setVerticalY(toChange.getY(),toChange.getY());
				
				//System.out.println("Previous values for the car " + theMapCars[index].getY() + " " + theMapCars[index].getVerticalY() + " "+ theMapCars[index].getX() + " " + theMapCars[index].getHorizontalX() );
				//System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());
				//System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());
				
				if (change < selectedMap.getDimension() && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(3);
					theMapCars[index].setHorizontalX(theMapCars[index].getX() ,change);	
					theMapCars[index].setVerticalY(toChange.getVerticalY(),toChange.getVerticalY());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}	
				break;
			case 1: 
				change = theMapCars[index].getY()-toChange.getLength()+1;
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(0);
				tmp.setHorizontalX(theMapCars[index].getHorizontalX() ,theMapCars[index].getHorizontalX());	
				tmp.setVerticalY(change,theMapCars[index].getY());

				if (change >= 0 && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(0);
					Car car = theMapCars[index].clone();
					//System.out.println(car.getX() + " " +  car.getHorizontalX() + " " + car.getY() + " " +car.getVerticalY());
					theMapCars[index].setHorizontalX(theMapCars[index].getHorizontalX() ,theMapCars[index].getHorizontalX());	
					theMapCars[index].setVerticalY(change,theMapCars[index].getY());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}
				break;
			case 2:
				change = theMapCars[index].getX() - toChange.getLength() + 1;
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(1);
				tmp.setHorizontalX(change,theMapCars[index].getX());	
				tmp.setVerticalY(theMapCars[index].getY(),theMapCars[index].getY());
				System.out.println("Previous values for the car " + theMapCars[index].getY() + " " + theMapCars[index].getVerticalY() + " "+ theMapCars[index].getX() + " " + theMapCars[index].getHorizontalX() );
				System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());
				
				if (change>= 0 && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(1);
					theMapCars[index].setHorizontalX(change ,theMapCars[index].getX());	
					theMapCars[index].setVerticalY(theMapCars[index].getY(),theMapCars[index].getY());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}	
				break;
			case 3:
		
				change = theMapCars[index].getY()+toChange.getLength()-1;

				tmp = theMapCars[index].clone();
				tmp.setCarDirection(2);
				tmp.setHorizontalX(theMapCars[index].getX() ,theMapCars[index].getX());	
				tmp.setVerticalY(theMapCars[index].getY(),change);
				System.out.println("Previous values for the car " + theMapCars[index].getY() + " " + theMapCars[index].getVerticalY() + " "+ theMapCars[index].getX() + " " + theMapCars[index].getHorizontalX() );
				System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());
				System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());

				if (change < selectedMap.getDimension() && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(2);
					theMapCars[index].setHorizontalX(theMapCars[index].getX() ,theMapCars[index].getX());	
					theMapCars[index].setVerticalY(theMapCars[index].getY(),change);
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}
				break;
			default:
				return null;

		}
		switch (toChange.getCarDirection()){
			case 0:
				change = theMapCars[index].getX()-toChange.getLength()+1;
				//Car tmp = theMapCars[index].clone();
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(1);
				tmp.setHorizontalX(change,theMapCars[index].getX());	
				tmp.setVerticalY(toChange.getY(),toChange.getY());
				System.out.println("My x coor: " + change + " " + tmp.getHorizontalX());
				if (change >=0 && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(1);
					theMapCars[index].setHorizontalX(tmp.getX(),tmp.getHorizontalX());	
					theMapCars[index].setVerticalY(toChange.getY(),toChange.getY());
					System.out.println("My x coor: " + theMapCars[index].getX() + " " + theMapCars[index].getHorizontalX());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}	
				break;
			case 1: 
				change = theMapCars[index].getVerticalY()+toChange.getLength()-1;

				tmp = theMapCars[index].clone();
				tmp.setCarDirection(2);
				tmp.setHorizontalX(theMapCars[index].getHorizontalX() ,theMapCars[index].getHorizontalX());	
				tmp.setVerticalY(theMapCars[index].getVerticalY(),change);
				System.out.println("Previous values for the car " + theMapCars[index].getY() + " " + theMapCars[index].getVerticalY() + " "+ theMapCars[index].getX() + " " + theMapCars[index].getHorizontalX() );
				System.out.println("My case 3 says : "+ change+ " "+ theMapCars[index].getHorizontalX() + " " +theMapCars[index].getX());
				if (change < selectedMap.getDimension() && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(2);
					theMapCars[index].setHorizontalX(theMapCars[index].getHorizontalX() ,theMapCars[index].getHorizontalX());	
					theMapCars[index].setVerticalY(theMapCars[index].getVerticalY(),change);
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}
				break;
			case 2:
				change = theMapCars[index].getHorizontalX()+toChange.getLength()-1;
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(3);
				tmp.setHorizontalX(theMapCars[index].getHorizontalX() ,change);	
				tmp.setVerticalY(toChange.getY(),toChange.getY());
				if (change <selectedMap.getDimension() && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(3);
					theMapCars[index].setHorizontalX(theMapCars[index].getHorizontalX() ,change);	
					theMapCars[index].setVerticalY(toChange.getY(),toChange.getY());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}	
				break;
			case 3:
				change = theMapCars[index].getY()-toChange.getLength()+1;
				tmp = theMapCars[index].clone();
				tmp.setCarDirection(0);
				tmp.setHorizontalX(theMapCars[index].getX() ,theMapCars[index].getX());	
				tmp.setVerticalY(change,theMapCars[index].getY());
				
				if (change >= 0 && canRotationBeMade(tmp,nullifyCar(theMapCars[index]))){
					theMapCars[index].setCarDirection(0);
					Car car = theMapCars[index].clone();
					//System.out.println(car.getX() + " " +  car.getHorizontalX() + " " + car.getY() + " " +car.getVerticalY());
					theMapCars[index].setHorizontalX(theMapCars[index].getX() ,theMapCars[index].getX());	
					theMapCars[index].setVerticalY(change,theMapCars[index].getY());
					move.last = theMapCars[index].clone();
					moves.addFirst(move);
					return theMapCars[index];
				}
				break;
			default:
				return null;

		}
		return null;
	}
	private boolean canRotationBeMade(Car car,Block[][] blocks){
		System.out.println(selectedMap);
		String tmp = "";
		for (int i = 0; i < blocks.length; i++){
			for (int j = 0; j < blocks.length; j++){
				tmp = tmp + blocks[i][j].isOccupied() + " ";
			}
			tmp += "\n";
		}
		System.out.println(tmp);
		//System.out.println("Car Direction " + car.getCarDirection());
		if (car.getCarDirection() == 1 || car.getCarDirection() == 3){
			//System.out.println("Car Direction " + car.getCarDirection() + " "+  car.getX() +  " " + car.getHorizontalX() );
			for (int i = car.getX(); i<= car.getHorizontalX(); i++){
				System.out.println(i + " " + car.getY() + " " + blocks[i][car.getY()].isOccupied());
				if (blocks[i][car.getY()].isOccupied()){
					return false;
				}
			}
		}else {

			for (int i = car.getY(); i<= car.getVerticalY(); i++){
				if (blocks[car.getX()][i].isOccupied()){
					return false;
				}
			}
		}

		return true;
	}

	private Block[][] nullifyCar(Car car){
		Map tmp = selectedMap.clone();
		Block[][] blocks = tmp.getBlocks();
		if (car.getCarDirection() == 1 || car.getCarDirection() == 3){
			for (int i = car.getX(); i<= car.getHorizontalX(); i++){
				blocks[i][car.getY()].setOccupied(false);	
			}
		}else {
			for (int i = car.getY(); i<= car.getVerticalY(); i++){
				blocks[car.getX()][i].setOccupied(false);
			}
		}
		return blocks;
	}


}

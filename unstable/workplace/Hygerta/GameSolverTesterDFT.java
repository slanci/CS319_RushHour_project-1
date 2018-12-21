import java.util.*;

class GameSolverTesterDFT{
    TreeNode initialConfig;
    Set<String> hashTable;
    private boolean gameFinished;

    public GameSolverTesterDFT(Map toSolve){
        initialConfig = new TreeNode(toSolve, null);
        hashTable = new HashSet<String>();
        gameFinished = false;
    }
    public void getHintMovesList(){
      dft(initialConfig);
    }
    public void dft(TreeNode current){
        if (gameFinished){
            //System.out.println(current.parent == null);

            return;
        }
        if (isSeenBefore(current)){
            return;
        }
        hashTable.add(hashMap(current.getMap()));
        if (gameFinished(current)){
            gameFinished =true;
            printSolutionPath(current);
            return;
        }
        Car[] cars = current.getMap().getCars();
        for (int i = 0; i<cars.length; i++){
            Map newConfig = canMove(current.getMap(),i,1);
            if (newConfig!=null){
                TreeNode tmp = new TreeNode(newConfig,current);
                dft(tmp);
                if (gameFinished){
                    //printSolutionPath(current);
                    return;
                }

            }else {
            }
            newConfig = canMove(current.getMap(),i,-1);
            if (newConfig!=null){
                TreeNode tmp = new TreeNode(newConfig,current);
                dft(tmp);
                if (gameFinished){
                    //printSolutionPath(current);
                    return;
                }

            }else {
                //System.out.println("Negative Movement Is null here \n"  +i);
            }
        }
    }

    private String hashMap(Map n){
        String tmp = "";
        for (int i = 0; i < n.getCars().length; i++){
            tmp = tmp + i +","+ n.getCars()[i].getX() +","+ n.getCars()[i].getHorizontalX() +","+ n.getCars()[i].getY() +","+ n.getCars()[i].getVerticalY()+",";
        }
        //System.out.println(tmp);
        return tmp;
    }

    private boolean isSeenBefore(TreeNode test){
        String tmp = hashMap(test.getMap());
        return hashTable.contains(tmp);
    }
    private boolean gameFinished(TreeNode possibleSolution){
        return possibleSolution.getMap().getPlayer().getY() == 4;
    }

    private Map canMove(Map config,int index,int unit){
        Map cloned = config.clone();
        Block[][] blocks = cloned.getBlocks();
        Car movable = cloned.getCars()[index];

        if (movable.getCarDirection() == 0 || movable.getCarDirection() == 2){
            int y = movable.getY() + unit;
            int endy = movable.getVerticalY() + unit;
            if (unit == 1){
                if (endy > 5){
                    return null;
                } else {
                    if (blocks[movable.getX()][endy].isOccupied()){
                        return null;
                    } else {
                        blocks[movable.getX()][endy].setOccupied(true);
                        blocks[movable.getX()][y-unit].setOccupied(false);
                        movable.setVerticalY(y,endy);
                        return cloned;
                    }
                }
            } else {
                if (y < 0){
                    return null;
                } else {
                    if (blocks[movable.getX()][y].isOccupied()){
                       // System.out.println("Block " +movable.getX() + " " + y);
                        return null;
                    } else {
                        blocks[movable.getX()][y].setOccupied(true);
                        blocks[movable.getX()][endy-unit].setOccupied(false);
                        movable.setVerticalY(y,endy);
                        return cloned;
                    }
                }
            }
        } else {
            int x = movable.getX() + unit;
            int endx = movable.getHorizontalX() + unit;
            if (unit == 1){
                if (endx > 5){
                    return null;
                } else {
                    if (blocks[endx][movable.getY()].isOccupied()){
                        return null;
                    } else {
                        blocks[endx][movable.getY()].setOccupied(true);
                        blocks[x-unit][movable.getY()].setOccupied(false);
                        movable.setHorizontalX(x,endx);
                        return cloned;
                    }
                }
            } else {
                if (x < 0){
                    return null;
                } else {
                    if (blocks[x][movable.getY()].isOccupied()){
                        return null;
                    } else {
                        blocks[x][movable.getY()].setOccupied(true);
                        blocks[endx-unit][movable.getY()].setOccupied(false);
                        movable.setHorizontalX(x,endx);
                        return cloned;
                    }
                }
            }

        }
    }

    private void printSolutionPath(TreeNode last){
        TreeNode cur = last;
        TreeNode parent = cur.parent;
        String tmp = "";
        while(parent!= null){
            tmp = findMove(cur) + tmp;
            cur = parent;
            parent = parent.parent;
        }
         System.out.println(tmp);
    }
    private String findMove(TreeNode current){
        TreeNode parent = current.parent;
        if (parent!=null){
            Car test, test2;
            for (int i = 0; i < current.getMap().getCars().length; i++){
                test = current.getMap().getCars()[i];
                test2 = parent.getMap().getCars()[i];
                if (test.getX() != test2.getX() || test.getHorizontalX() !=  test2.getHorizontalX()
                    || test.getY() != test2.getY() || test.getVerticalY() != test2.getVerticalY()){
                    return "Move " + i + " from location: " + test2.getX() + ", " + test2.getY() + " to: "+ test.getX() + ", " + test.getY() +"\n";
                }
            }
        }
        return "";
    }

    public static void main(String[] args){
        Map testMap = new Map(6);
        GameSolverTester testSolver = new GameSolverTester(testMap);
        testSolver.getHintMovesList();
    }
}
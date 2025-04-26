package mazenv;

import mazenv.MazeEnv.Action;
public class CheckEnv {
    public static void main(String[] args) {
        MazeEnv mazeEnv = new MazeEnv(20, 100, 50, 50);
        mazeEnv.reset();
        int[][] maze = mazeEnv.getMaze();
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("========================");
        mazeEnv.step(Action.RIGHT);
        maze = mazeEnv.getMaze();
        mazeEnv.step(Action.DOWN);
        int[][] maze1 = mazeEnv.getMaze();
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                System.out.print(maze1[i][j] + " ");
            }
            System.out.println();
        }
    }
}

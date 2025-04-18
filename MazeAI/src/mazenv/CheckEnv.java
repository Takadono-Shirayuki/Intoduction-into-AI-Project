package mazenv;

import mazenv.MazeEnv.Action;
import mazenv.MazeEnv.Buff;
import mazenv.MazeEnv.Debuff;
public class CheckEnv {
    public static void main(String[] args) {
        MazeEnv mazeEnv = new MazeEnv(20, 100, 50, 50);
        mazeEnv.reset();
        mazeEnv.render();
        System.out.println(mazeEnv.getTauCoefficient(2));
    }
}

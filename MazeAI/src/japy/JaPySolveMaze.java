package japy;

import mazeinterface.MazePanel;
public class JaPySolveMaze {
    private JaPy jaPy;
    private MazePanel mazePanel;
    

    public JaPySolveMaze(String pythonScriptFilePath, MazePanel mazePanel) {
        this.mazePanel = mazePanel;
        this.jaPy = new JaPy(pythonScriptFilePath, true);
    }

    public void start()
    {

    }
}

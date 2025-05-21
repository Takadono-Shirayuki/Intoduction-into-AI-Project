package japy;

public class JaPySolveMaze {
    private static JaPy jaPy;

    public static void Initialize(String pythonScriptFilePath) {
        jaPy = new JaPy(pythonScriptFilePath, true);
    }

    public void start()
    {

    }
}

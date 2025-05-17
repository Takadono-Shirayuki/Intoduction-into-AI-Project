package game;

import java.awt.Dimension;

import mazeinterface.GameForm;
import mazeinterface.MainForm;
import mazeinterface.mazecontrol.InfoPanel;
import mazeinterface.mazecontrol.MazePanel;
import mazeinterface.mazedialog.ShadowOverlay;
import mazenv.MazeState;
import mazenv.Pair;

public class Main {
    public static void main(String[] args) {
        GameVariable.loadGameVariable();
        new ShadowOverlay(new MainForm(), 1000, 1000, ShadowOverlay.MIST_RISE);
    }

    // Các biến trạng trái của trò chơi
    public static class GameStateVariable {
        public static final int FLOOR = 0;
        public static final int NUMS_STEP_UNTIL_REGENERATE = 1;
        public static final int STEPS_REMAINING = 2;
        public static final int RECEIVE_BUFF_PROBABILITY = 3;
    }
    private static GameForm gameForm;
    private static InfoPanel infoPanel;
    private static MazePanel mazePanel;

    public static void setGameForm(GameForm form) {
        gameForm = form;
    }

    public static void setInfoPanel(InfoPanel panel) {
        infoPanel = panel;
    }

    public static void setMazePanel(MazePanel panel) {
        mazePanel = panel;
    }

    public static void setVariableValue(int variableName, int value) {
        if (infoPanel == null) {
            System.out.println("InfoPanel is not initialized.");
            return;
        }
        switch (variableName) {
            case GameStateVariable.FLOOR:
                infoPanel.setFloor(value);
                break;
            case GameStateVariable.NUMS_STEP_UNTIL_REGENERATE:
                infoPanel.setNumsStepUntilRegenerate(value);
                break;
            case GameStateVariable.STEPS_REMAINING:
                infoPanel.setStepsRemaining(value);
                break;
            case GameStateVariable.RECEIVE_BUFF_PROBABILITY:
                infoPanel.setReceiveBuffProbability(value);
                break;
            default:
                break;
        }
    }

    public static int getVariableValue(int variableName) {
        if (infoPanel == null) {
            System.out.println("InfoPanel is not initialized.");
            return 0;
        }
        switch (variableName) {
            case GameStateVariable.FLOOR:
                return infoPanel.getFloor();
            case GameStateVariable.NUMS_STEP_UNTIL_REGENERATE:
                return infoPanel.getNumsStepUntilRegenerate();
            case GameStateVariable.STEPS_REMAINING:
                return infoPanel.getStepsRemaining();
            case GameStateVariable.RECEIVE_BUFF_PROBABILITY:
                return infoPanel.getReceiveBuffProbability();
            default:
                return 0;
        }
    }

    public static Pair<MazeState, Boolean> step(int action) {
        if (mazePanel == null) {
            System.out.println("MazePanel is not initialized.");
            return null;
        }
        return mazePanel.movePlayer(action);
    }

    public static void showMessage(String message, Dimension size) {
        if (gameForm != null) {
            gameForm.showMessage(message, size);
        } else {
            System.out.println("GameForm is not initialized.");
        }
    }

    public static void useSkill(int skill) {
        if (mazePanel != null) {
            mazePanel.useSkill(skill);
        } else {
            System.out.println("MazePanel is not initialized.");
        }
    }
}

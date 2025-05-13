package mazeinterface.mazedialog;

import javax.swing.JDialog;

public class ConversationSelectDialog extends JDialog{
    public int returnValue = -1;
    public ConversationSelectDialog(ConversationDialog parent, String displayText, String[] options) {
        super(parent, true);
    }
}

package mazeinterface.mazedialog;

import javax.swing.JDialog;

public class ConversationInputDialog extends JDialog {
    public String returnValue = null;
    
    public ConversationInputDialog(ConversationDialog parent, String displayText) {
        super(parent, true);
    }
    
}

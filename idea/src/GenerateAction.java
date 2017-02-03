import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class GenerateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        System.out.println(event);
    }
}

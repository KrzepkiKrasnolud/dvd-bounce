package pl.smyczek.dvdbounce;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

public class ToggleDvdBounceAction extends AnAction {

    private static final Map<Project, DvdPanel> activePanels = new HashMap<>();
    private static final Map<Project, ComponentAdapter> resizeListeners = new HashMap<>();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        if (activePanels.containsKey(project)) {
            DvdPanel panel = activePanels.remove(project);
            panel.stop();

            JRootPane rootPane = getRootPane(project);
            if (rootPane != null) {
                JLayeredPane layered = rootPane.getLayeredPane();
                layered.remove(panel);

                ComponentAdapter listener = resizeListeners.remove(project);
                if (listener != null) {
                    layered.removeComponentListener(listener);
                }

                layered.revalidate();
                layered.repaint();
            }
        } else {
            JRootPane rootPane = getRootPane(project);
            if (rootPane == null) return;

            JLayeredPane layered = rootPane.getLayeredPane();
            DvdPanel panel = new DvdPanel();

            panel.setBounds(0, 0, layered.getWidth(), layered.getHeight());

            ComponentAdapter resizeListener = new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent evt) {
                    panel.setBounds(0, 0, layered.getWidth(), layered.getHeight());
                }
            };
            layered.addComponentListener(resizeListener);
            resizeListeners.put(project, resizeListener);

            layered.add(panel, JLayeredPane.POPUP_LAYER);
            panel.start();
            activePanels.put(project, panel);
        }
    }

    private JRootPane getRootPane(Project project) {
        IdeFrame frame = WindowManager.getInstance().getIdeFrame(project);
        if (frame != null) {
            JComponent comp = frame.getComponent();
            if (comp != null) {
                return comp.getRootPane();
            }
        }
        return null;
    }
}

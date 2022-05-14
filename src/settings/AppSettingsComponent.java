package settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel mainPanel;
    private final JBRadioButton sonicRadio = new JBRadioButton("Sonic");
    private final JBRadioButton tailsRadio = new JBRadioButton("Tails");
    private final JBRadioButton knuxRadio = new JBRadioButton("Knuckles");

    private final JBCheckBox unlikeSonicIDontChuckle = new JBCheckBox("& Knuckles");

    public AppSettingsComponent() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(sonicRadio);
        buttonGroup.add(tailsRadio);
        buttonGroup.add(knuxRadio);

        this.mainPanel = FormBuilder.createFormBuilder()
                .addComponent(sonicRadio, 1)
                .addComponent(tailsRadio, 1)
                .addComponent(knuxRadio, 1)
                .addComponent(unlikeSonicIDontChuckle, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return sonicRadio;
    }

    public void setSonicRadio(boolean selected) {
        sonicRadio.setSelected(selected);
    }

    public boolean getSonicRadio() {
        return sonicRadio.isSelected();
    }

    public void setTailsRadio(boolean selected) {
        tailsRadio.setSelected(selected);
    }

    public boolean getTailsRadio() {
        return tailsRadio.isSelected();
    }

    public void setKnuxRadio(boolean selected) {
        knuxRadio.setSelected(selected);
    }

    public boolean getKnuxRadio() {
        return knuxRadio.isSelected();
    }

    public void setUnlikeSonicIDontChuckle(boolean selected) {
        unlikeSonicIDontChuckle.setSelected(selected);
    }

    public boolean getUnlikeSonicIDontChuckle() {
        return unlikeSonicIDontChuckle.isSelected();
    }
}

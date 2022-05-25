package settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import data.BarCharacter;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent appSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Sonic Mania Progress Bar";
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return appSettingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        appSettingsComponent = new AppSettingsComponent();
        return appSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        BarCharacter currentCharacter = settings.character;

        return !doRadioButtonsAndSettingsMatch(currentCharacter)
                || !isAndKnucklesStateEqualInSettingsAndUi(settings.andKnuckles);
    }

    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();

        appSettingsComponent.getCurrentSelection()
                .ifPresent(radioButton ->
                        settings.character = BarCharacter.valueOf(radioButton.getText().toUpperCase()));

        settings.andKnuckles = appSettingsComponent.getUnlikeSonicIDontChuckle();
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();

        switch (settings.character) {
            case SONIC:
                appSettingsComponent.setSonicRadio(true);
                appSettingsComponent.setTailsRadio(false);
                appSettingsComponent.setKnuxRadio(false);
                break;
            case TAILS:
                appSettingsComponent.setSonicRadio(false);
                appSettingsComponent.setTailsRadio(true);
                appSettingsComponent.setKnuxRadio(false);
                break;
            case KNUX:
                appSettingsComponent.setSonicRadio(false);
                appSettingsComponent.setTailsRadio(false);
                appSettingsComponent.setKnuxRadio(true);
                break;
        }

        appSettingsComponent.setUnlikeSonicIDontChuckle(settings.andKnuckles);
    }

    @Override
    public void disposeUIResources() {
        appSettingsComponent = null;
    }

    private boolean doRadioButtonsAndSettingsMatch(BarCharacter currentCharacter) {
        return appSettingsComponent.getCurrentSelection()
                .map(radioButton -> radioButton.getText().equalsIgnoreCase(currentCharacter.name()))
                .orElse(false);
    }

    private boolean isAndKnucklesStateEqualInSettingsAndUi(boolean andKnucklesSetting) {
        return andKnucklesSetting == appSettingsComponent.getUnlikeSonicIDontChuckle();
    }
}

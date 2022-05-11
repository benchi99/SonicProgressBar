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
        boolean sonicEnabled = appSettingsComponent.getSonicRadio();
        boolean tailsEnabled = appSettingsComponent.getTailsRadio();

        if (sonicEnabled && BarCharacter.SONIC.equals(currentCharacter)) {
            return false;
        }

        return !tailsEnabled || !BarCharacter.TAILS.equals(currentCharacter);
    }

    @Override
    public void apply() throws ConfigurationException {
        AppSettingsState settings = AppSettingsState.getInstance();

        if (appSettingsComponent.getSonicRadio()) {
            settings.character = BarCharacter.SONIC;
        } else if (appSettingsComponent.getTailsRadio()) {
            settings.character = BarCharacter.TAILS;
        }
    }

    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();

        if (BarCharacter.SONIC.equals(settings.character)) {
            appSettingsComponent.setSonicRadio(true);
            appSettingsComponent.setTailsRadio(false);
        } else if (BarCharacter.TAILS.equals(settings.character)) {
            appSettingsComponent.setSonicRadio(false);
            appSettingsComponent.setTailsRadio(true);
        }
    }

    @Override
    public void disposeUIResources() {
        appSettingsComponent = null;
    }
}

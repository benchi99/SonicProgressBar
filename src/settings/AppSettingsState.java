package settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import data.BarCharacter;

@State(
        name = "settings.AppSettingsState",
        storages = @Storage("sonic-bar.xml")
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    public BarCharacter character = BarCharacter.SONIC;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Override
    public @Nullable AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState appSettingsState) {
        XmlSerializerUtil.copyBean(appSettingsState, this);
    }
}

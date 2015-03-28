/*    This file is part of Arkhados.

 Arkhados is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Arkhados is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Arkhados.  If not, see <http://www.gnu.org/licenses/>. */
package arkhados.ui.hud;

import arkhados.PlayerData;
import arkhados.controls.CActionQueue;
import arkhados.controls.CCharacterHud;
import arkhados.util.PlayerDataStrings;
import arkhados.util.PlayerRoundStats;
import arkhados.util.UserDataStrings;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author william
 */
public class ClientHudManager extends AbstractAppState
        implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private Camera cam;
    private Node guiNode;
    private BitmapFont guiFont;
    private List<Node> characters = new ArrayList<>();
    private List<BitmapText> hpBars = new ArrayList<>();
    private List<BitmapText> playerNames = new ArrayList<>();
    private Spatial playerCharacter = null;
    private GameMessageHandler messageHandler = new GameMessageHandler();
    private SpellBar spellBar = new SpellBar();
    private VisualStatistics statistics = new VisualStatistics();

    public ClientHudManager(Camera cam, Node guiNode, BitmapFont guiFont) {
        this.cam = cam;
        this.guiNode = guiNode;
        this.guiFont = guiFont;
        guiNode.addControl(new CActionQueue());
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
        screen = nifty.getScreen("default_hud");
        spellBar.setNifty(nifty);
        spellBar.setScreen(screen);
        statistics.setNifty(nifty);
        statistics.setScreen(screen);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        cam = app.getCamera();
        messageHandler.initialize(nifty);
        messageHandler.createRows(10);
        statistics.initialize(app);
    }

    public void newOwnCharacter(Spatial newCharacter, boolean differentEntity) {
        playerCharacter = newCharacter;

        spellBar.setPlayerCharacter(newCharacter);
        if (differentEntity) {
            spellBar.clean();
            spellBar.setPlayerCharacter(newCharacter);
            spellBar.loadIcons();
        } else {
            spellBar.setPlayerCharacter(newCharacter);
        }
    }

    @Override
    public void update(float tpf) {
        if (playerCharacter != null) {
            spellBar.updateIcons();
        }

        for (int i = 0; i < characters.size(); ++i) {
            updateHpBar(i);
            updateText(i);
        }
    }

    public void addCharacter(Spatial character) {
        characters.add((Node) character);
        createHpBar();

        int playerId = character.getUserData(UserDataStrings.PLAYER_ID);
        String name =
                PlayerData.getStringData(playerId, PlayerDataStrings.NAME);

        createPlayerName(name);
    }

    public void clear() {
        characters.clear();
        for (BitmapText hpBar : hpBars) {
            hpBar.removeFromParent();
        }

        hpBars.clear();

        for (BitmapText playerName : playerNames) {
            playerName.removeFromParent();
        }

        playerNames.clear();

        clearAllButHpBars();
    }

    public void clearAllButHpBars() {
        playerCharacter = null;

        removeChildren("panel_buffs");
        removeChildren("panel_spells");

        hideStatistics();

        spellBar.clean();
    }

    private void createHpBar() {
        BitmapText hpBar = new BitmapText(guiFont);

        hpBar.setSize(guiFont.getCharSet().getRenderedSize());
        hpBar.setBox(new Rectangle(0, 0, 40, 10));
        hpBar.setColor(ColorRGBA.Red);
        hpBar.setAlignment(BitmapFont.Align.Center);
        hpBar.center();
        guiNode.attachChild(hpBar);
        hpBar.setQueueBucket(RenderQueue.Bucket.Gui);
        hpBars.add(hpBar);
    }

    private void createPlayerName(String name) {
        BitmapText text = new BitmapText(guiFont);

        text.setSize(guiFont.getCharSet().getRenderedSize() * 0.8f);
        text.setBox(new Rectangle(0, 0, 80, 10));
        text.setText(name);
        text.setColor(ColorRGBA.Cyan);
        text.setAlignment(BitmapFont.Align.Center);
        text.center();
        guiNode.attachChild(text);
        text.setQueueBucket(RenderQueue.Bucket.Gui);
        playerNames.add(text);
    }

    private void updateHpBar(int index) {
        Node character = characters.get(index);
        BitmapText hpBar = hpBars.get(index);
        float health = character.getUserData(UserDataStrings.HEALTH_CURRENT);
        if (health == 0) {
            hpBar.setText("");
            return;
        }
        // TODO: Implement better method to get character's head's location
        Vector3f hpBarLocation = cam.getScreenCoordinates(
                character.getLocalTranslation().add(0, 20, 0)).add(-15, 40, 0);
        hpBar.setLocalTranslation(hpBarLocation);
        hpBar.setText(String.format("%.0f", health));
    }

    private void updateText(int index) {
        Node character = characters.get(index);
        BitmapText name = playerNames.get(index);
        float height = name.getHeight();
        Vector3f textLocation = cam.getScreenCoordinates(
                character.getLocalTranslation().add(0, 20, 0))
                .add(-40, 40 + height, 0);
        name.setLocalTranslation(textLocation);
    }

    public void entityDisappeared(Spatial spatial) {
        if (!(spatial instanceof Node)) {
            return;
        }

        Node node = (Node) spatial;

        int index = characters.indexOf(node);

        if (index != -1) {
            BitmapText hpBar = hpBars.get(index);
            hpBar.removeFromParent();
            hpBars.remove(index);

            BitmapText playerName = playerNames.get(index);
            playerName.removeFromParent();
            playerNames.remove(index);

            characters.remove(index);
        }

        if (spatial == playerCharacter) {
            playerCharacter = null;
            spellBar.setPlayerCharacter(null);
        }
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void showStatistics() {
        statistics.show();
    }

    public void hideStatistics() {
        statistics.hide();
    }

    public void endGame() {
        clear();

        nifty.gotoScreen("main_menu");
    }

    public void clearBuffIcons() {
        Element buffIcons = screen.findElementByName("panel_buffs");
        Iterator<Element> it = buffIcons.getElements().iterator();
        for (; it.hasNext();) {
            Element element = it.next();
            element.markForRemoval();
        }
    }

    public Nifty getNifty() {
        return nifty;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setLatestStatsList(List<PlayerRoundStats> latestStatsList) {
        statistics.setLatestStatsList(latestStatsList);
    }

    public void disableCharacterHudControl() {
        if (playerCharacter == null) {
            return;
        }

        playerCharacter.getControl(CCharacterHud.class).setEnabled(false);
    }

    public void addMessage(String message) {
        messageHandler.addMessage(message, Color.WHITE);
    }

    private void removeChildren(Element element) {
        for (Iterator<Element> it = element
                .getElements().iterator(); it.hasNext();) {

            Element child = it.next();
            child.markForRemoval();
        }
    }

    private void removeChildren(String elementName) {
        removeChildren(screen.findElementByName(elementName));
    }

    /**
     * TODO: This is dirty temporary solution to clear messages.
     */
    public void clearMessages() {
        messageHandler.cleanup();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        clear();
    }
}
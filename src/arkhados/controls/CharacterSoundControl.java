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
package arkhados.controls;

import arkhados.Globals;
import com.jme3.audio.AudioNode;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Teemu
 */
public class CharacterSoundControl extends AbstractControl {

    private float suffering = 0;
    private String sufferPath;
    private String deathPath;

    @Override
    protected void controlUpdate(float tpf) {
        suffering += tpf;
    }

    public void suffer(float damage) {
        if (suffering <= 2 || sufferPath == null) {
            return;
        }

        AudioNode sound = new AudioNode(Globals.assetManager, sufferPath);
        sound.setPositional(true);
        sound.setReverbEnabled(false);
        sound.setVolume(1f);
        Node playerNode = (Node) getSpatial();
        playerNode.attachChild(sound);
        sound.play();
        suffering = 0;
    }

    public void death() {
        if (deathPath == null) {
            return;
        }

        AudioNode sound = new AudioNode(Globals.assetManager, deathPath);
        sound.setPositional(true);
        sound.setReverbEnabled(false);
        sound.setVolume(1f);
        Node playerNode = (Node) getSpatial();
        playerNode.attachChild(sound);
        sound.play();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void setSufferSound(String path) {
        this.sufferPath = path;
    }

    public void setDeathSound(String path) {
        this.deathPath = path;
    }
}
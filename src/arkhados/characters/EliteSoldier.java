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
package arkhados.characters;

import arkhados.controls.ActionQueueControl;
import arkhados.controls.CharacterAnimationControl;
import arkhados.controls.CharacterBuffControl;
import arkhados.controls.CharacterPhysicsControl;
import arkhados.controls.CharacterSyncControl;
import arkhados.controls.EliteSoldierAmmunitionControl;
import arkhados.controls.InfluenceInterfaceControl;
import arkhados.controls.SpellCastControl;
import arkhados.spell.Spell;
import arkhados.util.AnimationData;
import arkhados.util.InputMappingStrings;
import arkhados.util.NodeBuilder;
import arkhados.util.UserDataStrings;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Creates entity with EliteSoldiers's features.
 *
 * @author william
 */
public class EliteSoldier extends NodeBuilder {

    @Override
    public Node build() {
        Node entity = (Node) NodeBuilder.assetManager.loadModel("Models/Archer.j3o");
        final float movementSpeed = 36f;
        entity.setUserData(UserDataStrings.SPEED_MOVEMENT, movementSpeed);
        entity.setUserData(UserDataStrings.SPEED_MOVEMENT_BASE, movementSpeed);
        entity.setUserData(UserDataStrings.SPEED_ROTATION, 0.0f);
        final float radius = 5.0f;
        entity.setUserData(UserDataStrings.RADIUS, radius);
        final float health = 1800f;
        entity.setUserData(UserDataStrings.HEALTH_MAX, health);
        entity.setUserData(UserDataStrings.HEALTH_CURRENT, health);
        entity.setUserData(UserDataStrings.DAMAGE_FACTOR, 1f);
        entity.setUserData(UserDataStrings.LIFE_STEAL, 0f);

        for (Spatial childToScale : entity.getChildren()) {
            childToScale.scale(1.2f);
        }
        entity.addControl(new CharacterPhysicsControl(radius, 20.0f, 75.0f));

        /**
         * By setting physics damping to low value, we can effectively apply
         * impulses on it.
         */
        entity.getControl(CharacterPhysicsControl.class).setPhysicsDamping(0.2f);
        entity.addControl(new ActionQueueControl());

        /**
         * To add spells to entity, create SpellCastControl and call its
         * putSpell-method with name of the spell as argument.
         */
        final EliteSoldierAmmunitionControl ammunitionControl = new EliteSoldierAmmunitionControl();
        entity.addControl(ammunitionControl);

        final SpellCastControl spellCastControl = new SpellCastControl(EliteSoldier.worldManager);
        entity.addControl(spellCastControl);
        spellCastControl.addCastValidator(ammunitionControl);
        spellCastControl.addCastListeners(ammunitionControl);

        spellCastControl.putSpell(Spell.getSpells().get("Shotgun"), InputMappingStrings.M1);

        spellCastControl.putSpell(Spell.getSpells().get("Machinegun"), InputMappingStrings.M2);
        spellCastControl.putSpell(Spell.getSpells().get("Plasmagun"), InputMappingStrings.Q);
        spellCastControl.putSpell(Spell.getSpells().get("Rocket Launcher"), InputMappingStrings.E);
        spellCastControl.putSpell(Spell.getSpells().get("Like a Pro"), InputMappingStrings.R);
        spellCastControl.putSpell(Spell.getSpells().get("Rocket Jump"), InputMappingStrings.SPACE);

        /**
         * Map Spell names to casting animation's name. In this case all spells
         * use same animation.
         */
        CharacterAnimationControl animControl = new CharacterAnimationControl();
        final AnimationData deathAnim = new AnimationData("Die", 1f, LoopMode.DontLoop);
        final AnimationData walkAnim = new AnimationData("Walk", 1f, LoopMode.DontLoop);

        animControl.setDeathAnimation(deathAnim);
        animControl.setWalkAnimation(walkAnim);
        entity.addControl(animControl);

        final AnimationData animationData = new AnimationData("Attack", 1f, LoopMode.DontLoop);
        animControl.addActionAnimation("Cast", animationData);

        animControl.addSpellAnimation("Shotgun", animationData);
        animControl.addSpellAnimation("Machinegun", animationData);
        animControl.addSpellAnimation("Plasmagun", animationData);
        animControl.addSpellAnimation("Rocket Launcher", animationData);
        animControl.addSpellAnimation("Like a Pro", null);
        animControl.addSpellAnimation("Rocket Jump", animationData);

        entity.addControl(new InfluenceInterfaceControl());
        entity.addControl(new CharacterSyncControl());

        if (worldManager.isClient()) {
            entity.addControl(new CharacterBuffControl());
        }

        return entity;
    }
}

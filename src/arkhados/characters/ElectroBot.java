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

import arkhados.controls.CActionPlayer;
import arkhados.controls.CActionQueue;
import arkhados.controls.CCharacterDamage;
import arkhados.controls.CCharacterHeal;
import arkhados.controls.CCharacterMovement;
import arkhados.controls.CCharacterAnimation;
import arkhados.controls.CCharacterBuff;
import arkhados.controls.CCharacterHud;
import arkhados.controls.CCharacterPhysics;
import arkhados.controls.CCharacterSound;
import arkhados.controls.CCharacterSync;
import arkhados.controls.CInfluenceInterface;
import arkhados.controls.CResting;
import arkhados.controls.CSpellCast;
import arkhados.controls.CSyncInterpolation;
import arkhados.effects.EffectBox;
import arkhados.effects.RandomChoiceEffect;
import arkhados.effects.SimpleSoundEffect;
import arkhados.spell.Spell;
import arkhados.spell.spells.electrobot.Zap;
import arkhados.util.AnimationData;
import arkhados.util.InputMapping;
import arkhados.util.AbstractNodeBuilder;
import arkhados.util.BuildParameters;
import arkhados.util.UserData;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.scene.Node;

/**
 * Creates entity with EmberMage's features.
 */
public class ElectroBot extends AbstractNodeBuilder {

    public static final int ACTION_ZAP = 0;
    public static final int ACTION_ETHEREAL_FLAME = 1;

    public ElectroBot() {
        setEffectBox(new EffectBox());

        RandomChoiceEffect zap = new RandomChoiceEffect();
        zap.add(new SimpleSoundEffect("Effects/Sound/Zap2.wav"));
        zap.add(new SimpleSoundEffect("Effects/Sound/Zap1.wav"));        
        getEffectBox().addActionEffect(ACTION_ZAP, zap);
    }

    @Override
    public Node build(BuildParameters params) {
        Node entity = (Node) assets.loadModel("Models/Mage.j3o");
        float speed = 35.8f;
        entity.setUserData(UserData.SPEED, speed);
        entity.setUserData(UserData.SPEED_BASE, speed);
        float radius = 5f;
        entity.setUserData(UserData.RADIUS, radius);
        float health = 2000f;
        entity.setUserData(UserData.HEALTH_MAX, health);

        entity.setUserData(UserData.HEALTH_CURRENT, health);
        if (params.age < 0f) {
            entity.setUserData(UserData.HEALTH_CURRENT, 0f);
        }

        entity.setUserData(UserData.DAMAGE_FACTOR, 1f);
        entity.setUserData(UserData.LIFE_STEAL_BASE, 0f);
        entity.setUserData(UserData.LIFE_STEAL, 0f);

        entity.addControl(new CCharacterPhysics(radius, 20f, 75f));

        /**
         * By setting physics damping to low value, we can effectively apply
         * impulses on it.
         */
        entity.getControl(CCharacterPhysics.class).setPhysicsDamping(0.2f);
        entity.addControl(new CCharacterMovement());
        entity.addControl(new CActionQueue());

        /**
         * To add spells to entity, create SpellCastControl and call its
         * putSpell-method with name of the spell as argument.
         */
        CSpellCast spellCastControl = new CSpellCast();
        entity.addControl(spellCastControl);
        int M2Id = InputMapping.getId(InputMapping.M2);

        int RId = InputMapping.getId(InputMapping.R);

        Zap zap = (Zap) Spell.getSpell("Zap");
        spellCastControl.putSpell(zap,
                InputMapping.getId(InputMapping.M1));
        spellCastControl.putSpell(Spell.getSpell("Electric Bomb"), M2Id);
        spellCastControl.putSpell(Spell.getSpell("Electric Pull"),
                InputMapping.getId(InputMapping.Q));
        spellCastControl.putSpell(Spell.getSpell("Paralyze"),
                InputMapping.getId(InputMapping.E));
        spellCastControl.putSpell(Spell.getSpell("Electron Orbit"), RId);
        spellCastControl.putSpell(Spell.getSpell("Electro Charge"),
                InputMapping.getId(InputMapping.SPACE));
        /**
         * Map Spell names to casting animation's name. In this case all spells
         * use same animation.
         */
        AnimControl animControl = entity.getControl(AnimControl.class);

        CCharacterAnimation characterAnimControl
                = new CCharacterAnimation(animControl);
        AnimationData deathAnim
                = new AnimationData("Die", 1f, LoopMode.DontLoop);
        AnimationData walkAnim
                = new AnimationData("Walk", 1f, LoopMode.DontLoop);

        characterAnimControl.setDeathAnimation(deathAnim);
        characterAnimControl.setWalkAnimation(walkAnim);
        entity.addControl(characterAnimControl);

        AnimationData idleAnim
                = new AnimationData("Idle", 1f, LoopMode.Loop);

        AnimationData hitAnim = new AnimationData("Hit", 1f, LoopMode.DontLoop);
        AnimationData attackAnim
                = new AnimationData("Attack", 1f, LoopMode.DontLoop);

        characterAnimControl.addSpellAnimation(zap.getName(), attackAnim);
        characterAnimControl.addSpellAnimation("Electric Bomb", idleAnim);
        characterAnimControl.addSpellAnimation("Electric Pull", idleAnim);
        characterAnimControl.addSpellAnimation("Paralyze", idleAnim);
        characterAnimControl.addSpellAnimation("Electron Orbit", null);
        characterAnimControl.addSpellAnimation("Electro Charge", idleAnim);

        entity.addControl(new CInfluenceInterface());
        entity.addControl(new CCharacterDamage());
        entity.addControl(new CCharacterHeal());

        entity.addControl(new CCharacterSync());

        if (world.isClient()) {
            CCharacterSound soundControl = new CCharacterSound();
            soundControl.addSufferSound("Effects/Sound/EmberMagePain.wav");
            soundControl.setDeathSound("Effects/Sound/EmberMageDeath.wav");
            entity.addControl(soundControl);
            entity.addControl(new CCharacterBuff());
            entity.addControl(new CCharacterHud());

            entity.addControl(new CSyncInterpolation());
            entity.getControl(CInfluenceInterface.class).setIsServer(false);

            CActionPlayer actionPlayer = new CActionPlayer();
//            actionPlayer.putCastEffect(zap.getId(), zap.castEffect);
            entity.addControl(actionPlayer);
        } else {
            CResting restingControl = new CResting();
            entity.addControl(restingControl);
        }

        return entity;
    }
}

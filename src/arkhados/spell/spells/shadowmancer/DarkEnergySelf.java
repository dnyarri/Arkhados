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
package arkhados.spell.spells.shadowmancer;


import arkhados.actions.cast.ACastProjectile;
import arkhados.characters.EliteSoldier;

import arkhados.spell.Spell;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Shadowmancer's main buff (M2) spell. 
 */
public class DarkEnergySelf extends Spell {

    public static final float SPLASH_RADIUS = 25f;

    {
        iconName = "DarkEnergy.png";
        setMoveTowardsTarget(false);
    }

    public DarkEnergySelf(String name, float cooldown, float range, 
            float castTime) {
        super(name, cooldown, range, castTime);
    }

    public static Spell create() {
        final float cooldown = 2f;
        final float range = 2.8f; // Values below 2.5 are too small
        final float castTime = 0.4f;

        final DarkEnergySelf spell = new DarkEnergySelf("Dark Energy Self",
                cooldown, range, castTime);

        spell.castSpellActionBuilder = (Node caster, Vector3f location) -> {
            ACastProjectile castProjectile = new ACastProjectile(spell, world);
            castProjectile.setTypeId(EliteSoldier.ACTION_ROCKET_LAUNCHER);
            castProjectile.detonateAtTarget(true);
            return castProjectile;
        };

        spell.nodeBuilder = new EnergyBuilder();

        return spell;
    }
}

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

import arkhados.ui.HeroSelectionBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;

public class DeathMatchHeroSelectionLayerBuilder extends LayerBuilder {
    public DeathMatchHeroSelectionLayerBuilder() {
        childLayoutCenter();        
        panel(new HeroSelectionBuilder());
        controller(new DeathMatchHeroSelectionLayerController());
    }
}

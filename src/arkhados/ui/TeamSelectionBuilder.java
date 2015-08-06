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
package arkhados.ui;

import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import java.util.Collection;

public class TeamSelectionBuilder extends LayerBuilder {

    public TeamSelectionBuilder(Collection<String> options) {
        super("layer-team-selection");
        childLayoutCenter();
        panel(new TeamSelectionPanelBuilder(options));
        controller(new TeamSelectionController());
    }
}

class TeamSelectionPanelBuilder extends PanelBuilder {

    public TeamSelectionPanelBuilder(Collection<String> options) {
        childLayoutHorizontal();
        for (String option : options) {
            ButtonBuilder button = new ButtonBuilder(
                    "team-selection-button-" + option, option);
            button.interactOnClick(String.format("selectTeam(%s)", option));
            control(button);
        }
    }
}
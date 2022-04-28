/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */
package bisq.desktop.skins;

import bisq.desktop.components.controls.BisqPopup;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.StackPane;
import lombok.Getter;

public class BisqPopupSkin implements Skin<BisqPopup> {
    @Getter
    private final BisqPopup skinnable;

    public BisqPopupSkin(final BisqPopup popup) {
        this.skinnable = popup;
        popup.getRoot().getChildren().add(popup.getContentNode());
    }

    @Override
    public Node getNode() {
        return skinnable.getRoot();
    }

    @Override
    public void dispose() {}
}
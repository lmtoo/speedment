/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.component.brand;

import com.speedment.annotation.Api;
import com.speedment.internal.ui.UISession;
import com.speedment.license.Software;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * A branding container.
 * 
 * @author  Emil Forslund
 * @since   2.3.0
 */
@Api(version = "2.3")
public interface Brand {

    /**
     * Returns the primary name of this brand.
     * <p>
     * Example: {@code Speedment}
     * 
     * @return  the title 
     */
    String title();
    
    /**
     * Returns the subtitle of this brand that is to be shown togather with the 
     * title in formal occasions.
     * <p>
     * Example: {@code Open Source}
     * 
     * @return  the subtitle 
     */
    String subtitle();
    
    /**
     * Returns the version of the main software. This is the version that should 
     * be used as part of the brand. In addition to this, each {@link Software}
     * might have its own {@link Software#getVersion()}.
     * <p>
     * Example: {@code 2.3.0}
     * 
     * @return  the version
     */
    String version();
    
    /**
     * Returns the human-readable web address associated with this brand. Any
     * http-prefix should not be included.
     * <p>
     * Example: {@code www.speedment.org}
     * 
     * @return  the website
     */
    String website();
    
    /**
     * Optionally returns the path to a small logo image fit to be used on icons
     * or in the application titlebar. If no logo is specified, an empty
     * {@link Optional} is returned.
     * <p>
     * Example: {@code /images/speedment_open_source_small.png}
     * 
     * @return  the small logo
     */
    Optional<String> logoSmall();
    
    /**
     * Optionally returns the path to a large logo image fit to be used as an
     * illustration in various dialog messages. If no logo is specified, an 
     * empty {@link Optional} is returned.
     * <p>
     * Example: {@code /images/logo.png}
     * 
     * @return  the larger logo
     */
    Optional<String> logoLarge();

    /**
     * Applies this brand to the specified {@link UISession} and {@link Scene}.
     * 
     * @param session  the current session
     * @param scene    the scene to set icons and stylesheets in
     */
    static void apply(UISession session, Scene scene) {
        final Stage stage = session.getStage();
        final Brand brand = session
            .getSpeedment()
            .getUserInterfaceComponent()
            .getBrand();

        stage.setTitle(brand.title());
        brand.logoSmall()
            .map(Image::new)
            .ifPresent(icon -> {
                stage.getIcons().add(icon);

                @SuppressWarnings("unchecked")
                final Stage dialogStage = (Stage) scene.getWindow();
                if (dialogStage != null) {
                    dialogStage.getIcons().add(icon);
                }
            });

        session.getSpeedment()
            .getUserInterfaceComponent()
            .stylesheetFiles()
            .forEachOrdered(scene.getStylesheets()::add);
    }
}
package com.karumi.screenshot;

import com.karumi.screenshot.model.SuperHero;

/**
 * Created by davidgonzalez on 28/07/17.
 */

class SuperHeroBuilder {

    private static final String ANY_NAME = "Superhero";
    private static final String ANY_PHOTO = null;
    private static final boolean ANY_AVENGER = false;
    public static final String ANY_DESCRIPTION = "Superhero description";

    private String name = ANY_NAME;
    private String photo = ANY_PHOTO;
    private boolean isAvenger = ANY_AVENGER;
    private String description = ANY_DESCRIPTION;

    public SuperHeroBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SuperHeroBuilder isAvenger (boolean isAvenger) {
        this.isAvenger = isAvenger;
        return this;
    }

    public SuperHero build() {
        return new SuperHero(name, photo, isAvenger, description);
    }
}

/*
 * Copyright (C) 2017 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.screenshot;

import android.app.Activity;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.karumi.screenshot.di.MainComponent;
import com.karumi.screenshot.di.MainModule;
import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroesRepository;
import com.karumi.screenshot.ui.view.SuperHeroDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;

public class SuperHeroDetailActivityTest extends ScreenshotTest {

    @Rule
    public DaggerMockRule<MainComponent> daggerRule =
            new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
                    new DaggerMockRule.ComponentSetter<MainComponent>() {
                        @Override
                        public void setComponent(MainComponent component) {
                            SuperHeroesApplication app =
                                    (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                                            .getTargetContext()
                                            .getApplicationContext();
                            app.setComponent(component);
                        }
                    });

    @Rule
    public ActivityTestRule<SuperHeroDetailActivity> activityRule =
            new ActivityTestRule<>(SuperHeroDetailActivity.class, true, false);

    @Mock
    SuperHeroesRepository repository;

    @Test
    public void showsTitleBarInTheTop() {

        SuperHero superHero = givenThereIsASuperHero(true);

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    @Test
    public void showsSuperHeroDescriptionInTheBottom() {

        SuperHero superHero = givenThereIsASuperHero(true);
        Activity activity = startActivity(superHero);

        // It would be better to scroll to a hidden view placed on the footer or use swipes
        onView(withId(R.id.tv_super_hero_description)).perform(scrollTo());

        compareScreenshot(activity);
    }

    @Test
    public void showsAvengersBadgeIfASuperHeroIsPartOfTheAvengersTeam() {

        SuperHero superHero = givenAnAvenger();

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    @Test
    public void doesNotshowAvengersBadgeIfASuperHeroIsNotPartOfTheAvengersTeam() {

        SuperHero superHero = givenThereIsASuperHero(false);

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    // Long name
    // Long description (ellipsize)
    // Description with special characters
    // Show all info
    // Not showing title
    // Not showing description
    // Empty superhero

    private SuperHero givenAnAvenger() {
        return givenThereIsASuperHero(true);
    }

    private SuperHero givenThereIsASuperHero(boolean isAvenger) {
        String superHeroName = "SuperHero";
        String superHeroDescription = "Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.";
        SuperHero superHero = new SuperHero(superHeroName, null, isAvenger, superHeroDescription);
        when(repository.getByName(superHeroName)).thenReturn(superHero);
        return superHero;
    }

    private SuperHeroDetailActivity startActivity(SuperHero superHero) {
        Intent intent = new Intent();
        intent.putExtra("super_hero_name_key", superHero.getName());
        return activityRule.launchActivity(intent);
    }
}

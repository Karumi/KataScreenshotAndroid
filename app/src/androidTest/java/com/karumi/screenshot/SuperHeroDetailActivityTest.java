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

        SuperHero superHero = givenSuperHero(SuperHeroMother.commonSuperHero());

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    @Test
    public void showsSuperHeroDescriptionInTheBottom() {
        // GIVEN
        SuperHero superHero = givenSuperHero(SuperHeroMother.bigDescriptionSuperHero());
        Activity activity = startActivity(superHero);

        // WHEN
        // It would be better to scroll to a hidden view placed on the footer or use swipes
        onView(withId(R.id.tv_super_hero_description)).perform(scrollTo());

        // THAT
        compareScreenshot(activity);
    }

    @Test
    public void showsAvengersBadgeIfASuperHeroIsPartOfTheAvengersTeam() {

        SuperHero superHero = givenSuperHero(SuperHeroMother.givenAnAvenger());

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    @Test
    public void doesNotshowAvengersBadgeIfASuperHeroIsNotPartOfTheAvengersTeam() {

        SuperHero superHero = givenSuperHero(SuperHeroMother.givenANotAvenger());

        Activity activity = startActivity(superHero);

        compareScreenshot(activity);
    }

    private SuperHero givenSuperHero(SuperHero superHero) {
        when(repository.getByName(superHero.getName())).thenReturn(superHero);
        return superHero;
    }

    // Long name
    // Long description (ellipsize)
    // Description with special characters
    // Show all info
    // Not showing title
    // Not showing description
    // Empty superhero

    private SuperHeroDetailActivity startActivity(SuperHero superHero) {
        Intent intent = new Intent();
        intent.putExtra("super_hero_name_key", superHero.getName());
        return activityRule.launchActivity(intent);
    }
}

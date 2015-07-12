package com.questio.projects.questio.TestHelper;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Copy from https://gist.github.com/AlexKorovyansky/bb9e832bcb2ee86e4475
 * User: AlexKorovyansky
 */
public class ViewMatchers {
    public static Matcher<View> withDrawable(final int resourceId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with drawable from resource id: " + resourceId);
            }

            @Override
            public boolean matchesSafely(View view) {
                try {
                    final Drawable resourcesDrawable = view.getResources().getDrawable(resourceId);
                    if (view instanceof ImageView) {
                        final ImageView imageView = (ImageView) view;
                        if (imageView.getDrawable() == null) {
                            return resourcesDrawable == null;
                        }
                        assert resourcesDrawable != null;
                        return imageView.getDrawable().getConstantState()
                                .equals(resourcesDrawable.getConstantState());
                    }
                } catch (Resources.NotFoundException ignored) {

                }
                return false;
            }
        };
    }
}
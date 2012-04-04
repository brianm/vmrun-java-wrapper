package org.skife.vmware;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.File;

public class MoreMatchers
{
    public static Matcher<File> fileExists() {
        return new BaseMatcher<File>()
        {
            @Override
            public boolean matches(Object item)
            {
                File file = (File) item;
                return file.exists();
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("does not exist");
            }
        };
    }
}

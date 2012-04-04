package org.skife.vmware;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class MoreMatchers
{
    public static Matcher<File> fileExists()
    {
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

    public static Matcher<File> fileContentsEqual(final String body)
    {
        return new BaseMatcher<File>()
        {

            private String error = "File contents do not match";

            @Override
            public boolean matches(Object item)
            {
                File f = (File) item;
                try {
                    String contents = Files.toString(f, Charsets.UTF_8);
                    return body.equals(contents);
                }
                catch (IOException e) {
                    error = e.getMessage();
                    return false;
                }
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText(error);
            }
        };
    }
}

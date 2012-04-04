package org.skife.vmware;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.io.CharStreams.readFirstLine;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class GuestTest
{
    private static Guest guest;

    @BeforeClass
    public static void setUp() throws Exception
    {
        guest = VMRun.withExecutableAt("/Applications/VMware Fusion.app/Contents/Library/vmrun")
                     .createGuest("base-ubuntu.vmwarevm/ubuntu-12.04.vmx", "atlas", "atlas");
        guest.start();
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        guest.stop();
    }

    @Test
    public void testGetStandardOutFromScript() throws Exception
    {
        ProcessResult pr = guest.sh("echo 'hello guest!'");
        assertThat(new String(pr.getStdout()), equalTo("hello guest!\n"));
    }

    @Test
    public void testGetStandardErrFromScript() throws Exception
    {
        ProcessResult pr = guest.sh("echo 'hello guest!' > /dev/stderr");
        assertThat(new String(pr.getStderr()), equalTo("hello guest!\n"));
    }

    @Test
    public void testFindIpAddress() throws Exception
    {
        String line = readFirstLine(guest.sh("ifconfig | grep 'inet addr' | egrep -v '127.0.0.1'").getStdoutSupplier());
        Matcher m = Pattern.compile("\\s+inet addr:([\\d\\.]+)\\s.+").matcher(line);
        m.matches();
        assertThat(m.group(1), containsString("172."));
    }
}

package org.skife.vmware;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.skife.vmware.MoreMatchers.fileContentsEqual;
import static org.skife.vmware.MoreMatchers.fileExists;

public class VMRunTest
{
    private VMRun vmrun;
    private File vmx;
    private String guestUser;
    private String guestPass;

    @Before
    public void setUp() throws Exception
    {
        this.guestUser = System.getProperty("guest-user", "atlas");
        this.guestPass = System.getProperty("guest-pass", "atlas");

        File execFile = new File(System.getProperty("vmrun",
                                                    "/Applications/VMware Fusion.app/Contents/Library/vmrun"));
        this.vmx = new File(System.getProperty("vmx",
                                               "base-ubuntu.vmwarevm/ubuntu-12.04.vmx"));
        assumeThat(execFile, fileExists());
        this.vmrun = VMRun.withExecutableAt(execFile);
    }

    @Test
    public void testStartStartAndList() throws Exception
    {
        vmrun.start(vmx);
        assertThat(vmrun.list(), equalTo(Arrays.asList(vmx.getAbsoluteFile() )));
        vmrun.stop(vmx);
    }

    @Test
    public void testRunScriptInGuestWithSuccess() throws Exception
    {
        vmrun.start(vmx);
        int exit = vmrun.runScriptInGuest(vmx, guestUser, guestPass, new File("/bin/sh"), "ifconfig -a > /tmp/ifout");
        assertThat(exit, equalTo(0));
        vmrun.stop(vmx);
    }

    @Test
    public void testRunScriptInGuestWithFailure() throws Exception
    {
        vmrun.start(vmx);
        int exit = vmrun.runScriptInGuest(vmx, guestUser, guestPass, new File("/bin/sh"), "waffles taste good");
        assertThat(exit, equalTo(127));
        vmrun.stop(vmx);
    }

    @Test
    public void testCopyFileFromGuestToHost() throws Exception
    {
        vmrun.start(vmx);
        vmrun.runScriptInGuest(vmx, guestUser, guestPass, new File("/bin/sh"), "echo 'hello guest world' > /tmp/msg");

        File tmp = File.createTempFile("test", ".tmp");
        tmp.delete();

        vmrun.copyFileFromGuestToHost(vmx, guestUser, guestPass, new File("/tmp/msg"), tmp);

        assertThat(Files.toString(tmp, Charsets.US_ASCII), equalTo("hello guest world\n"));

        tmp.delete();
        vmrun.stop(vmx);
    }

    @Test
    public void testCopyFileFromHostToGuest() throws Exception
    {
        File tmp = File.createTempFile("vmrun-wrapper", ".tmp");
        Files.write("hello from host!", tmp, Charsets.UTF_8);

        File round_trip = File.createTempFile("vmrun-wrapper", ".tmp");
        round_trip.delete();

        vmrun.start(vmx);

        vmrun.copyFileFromHostToGuest(vmx, guestUser, guestPass, tmp, new File("/tmp/from-host"));

        vmrun.copyFileFromGuestToHost(vmx, guestUser, guestPass, new File("/tmp/from-host"), round_trip);

        assertThat(round_trip, fileContentsEqual("hello from host!"));

        vmrun.runScriptInGuest(vmx, guestUser, guestPass, new File("/bin/sh"), "rm /tmp/from-host");
        vmrun.stop(vmx);
    }

}

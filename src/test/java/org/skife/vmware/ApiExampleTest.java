package org.skife.vmware;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertThat;
import static org.skife.vmware.MoreMatchers.fileContentsEqual;

public class ApiExampleTest
{

    @Test
    public void testGuestApiExample() throws Exception
    {
        File tmp = File.createTempFile("java-vmrun-wrapper", ".tmp");

        Guest guest = VMRun.withExecutableAt("/Applications/VMware Fusion.app/Contents/Library/vmrun")
                           .createGuest("base-ubuntu.vmwarevm/ubuntu-12.04.vmx", "atlas", "atlas");

        guest.start();
        guest.sh("echo 'hello guest!' > /tmp/testFoo");
        guest.copyFileToHost("/tmp/testFoo", tmp);

        assertThat(tmp, fileContentsEqual("hello guest!\n"));

        guest.stop();
    }
}

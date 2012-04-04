package org.skife.vmware;

import java.io.File;
import java.io.IOException;

public class Guest
{
    private final VMRun vmrun;
    private final File  vmx;

    Guest(VMRun vmrun, File vmx)
    {
        this.vmrun = vmrun;
        this.vmx = vmx;
    }

    public void runScript(String guestUser, String guestPass, File interpreter, String scriptBody) throws IOException, InterruptedException
    {
        vmrun.runScriptInGuest(vmx, guestUser, guestPass, interpreter, scriptBody);
    }
}

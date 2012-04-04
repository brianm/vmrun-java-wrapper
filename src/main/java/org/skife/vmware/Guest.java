package org.skife.vmware;

import com.google.common.base.Throwables;

import java.io.File;

public class Guest
{
    private final VMRun vmrun;
    private final File  vmx;
    private final String user;
    private final String pass;

    Guest(VMRun vmrun, File vmx, String user, String pass)
    {
        this.vmrun = vmrun;
        this.vmx = vmx;
        this.user = user;
        this.pass = pass;
    }

    public int runScript(File interpreter, String scriptBody)
    {
        try {
            return vmrun.runScriptInGuest(vmx, user, pass, interpreter, scriptBody);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public int sh(String scriptlet) {
        return runScript(new File("/bin/sh"), scriptlet);
    }

    public void start()
    {
        try {
            vmrun.start(vmx);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void stop()
    {
        try {
            vmrun.stop(vmx);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void copyFileToHost(String guestPath, File hostPath) {
        copyFileToHost(new File(guestPath), hostPath);
    }

    public void copyFileToHost(File guestPath, File hostPath)
    {
        try {
            vmrun.copyFileFromGuestToHost(vmx, user, pass, guestPath, hostPath);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

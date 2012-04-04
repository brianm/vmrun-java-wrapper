package org.skife.vmware;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

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

    public ProcessResult runScript(File interpreter, String scriptBody)
    {
        try {

            File script = File.createTempFile("vmrun", ".sh");
            File out = File.createTempFile("vmrun", ".out");
            File err = File.createTempFile("vmrun", ".err");
            Files.write(scriptBody, script, Charsets.UTF_8);

            copyFileToGuest(script, "/tmp/vmrun-script-helper");
            int exit = vmrun.runScriptInGuest(vmx, user, pass, interpreter, "sh /tmp/vmrun-script-helper 1> /tmp/vmrun-script-helper-out 2>/tmp/vmrun-script-helper-err" );
            copyFileToHost("/tmp/vmrun-script-helper-out", out);
            copyFileToHost("/tmp/vmrun-script-helper-err", err);

            return new ProcessResult(exit, Files.toByteArray(out), Files.toByteArray(err));
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public ProcessResult sh(String scriptlet) {
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

    public void copyFileToHost(String guestPath, String hostPath) {
        copyFileToHost(new File(guestPath), new File(hostPath));
    }

    public void copyFileToHost(File guestPath, String hostPath) {
        copyFileToHost(guestPath, new File(hostPath));
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

    public void copyFileToGuest(File hostPath, String guestPath)
    {
        copyFileToGuest(hostPath, new File(guestPath));
    }

    public void copyFileToGuest(File hostPath, File guestPath)
    {
        try {
            vmrun.copyFileFromHostToGuest(vmx, user, pass, hostPath, guestPath);
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}

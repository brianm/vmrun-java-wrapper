package org.skife.vmware;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class ProcessResult
{
    private final int    exitCode;
    private final byte[] stdout;
    private final byte[] stderr;

    ProcessResult(int exitCode, byte[] stdout, byte[] stderr)
    {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    static ProcessResult run(String... cmd) throws InterruptedException, IOException
    {
        Process p = new ProcessBuilder().command(cmd).start();
        int exit = p.waitFor();
        byte[] stdout = ByteStreams.toByteArray(p.getInputStream());
        byte[] stderr = ByteStreams.toByteArray(p.getErrorStream());

        return new ProcessResult(exit, stdout, stderr);
    }

    ProcessResult explodeOnError()
    {
        if (exitCode != 0) {
            throw new RuntimeException(new String(stdout));
        }
        return this;
    }

    public int getExitCode()
    {
        return exitCode;
    }

    public byte[] getStdout()
    {
        return stdout;
    }

    public byte[] getStderr()
    {
        return stderr;
    }
}

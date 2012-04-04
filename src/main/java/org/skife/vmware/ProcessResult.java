package org.skife.vmware;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

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

    public InputSupplier<? extends Reader> getStdoutSupplier() {
        return CharStreams.newReaderSupplier(new String(stdout));
    }

    public InputSupplier<? extends Reader> getStderrSupplier() {
        return CharStreams.newReaderSupplier(new String(stderr));
    }

}

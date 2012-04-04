package org.skife.vmware;

import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.IntByReference;

import java.util.Map;

public interface LibC
{
    int kill(int pid, int signal);

    String strerror(int errno);

    int posix_spawnp(@Out IntByReference pid,
                     @In CharSequence path,
                     @In Pointer fileActions,
                     @In Pointer attr,
                     @In CharSequence[] argv,
                     @In CharSequence[] envp);

    int waitpid(int a, int b, int c);

    public static class Util
    {
        public static String[] getEnv()
        {
            String[] envp = new String[System.getenv().size()];
            int i = 0;
            for (Map.Entry<String, String> pair : System.getenv().entrySet()) {
                envp[i++] = pair.getKey() + "=" + pair.getValue();
            }
            return envp;
        }
    }

}

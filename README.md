Convenience library for using [vmrun](http://www.vmware.com/pdf/vix160_vmrun_command.pdf) inside Java applications. Right now it only really works with Fusion/Workstation as you should probably just use the HTTP interface for vSphere.

    Guest guest = VMRun.withExecutableAt(new File("/Applications/VMware Fusion.app/Contents/Library/vmrun"))
                       .createGuest(new File("ubuntu-12.04.vmwarevm/ubuntu-12.04.vmx"), "atlas", "atlas");

    guest.start();
    guest.sh("echo 'hello guest!' > /tmp/message-on-guest");
    guest.copyFileToHost("/tmp/message-on-guest", "/tmp/message-on-host");

    guest.stop();

Have fun!
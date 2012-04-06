package org.skife.vmware;

public enum GUI
{
    NoGUI
        {
            @Override
            public String commandLineValue()
            {
                return "nogui";
            }
        },

    YesGUI
        {
            @Override
            public String commandLineValue()
            {
                return "gui";
            }
        };

    public abstract String commandLineValue();
}

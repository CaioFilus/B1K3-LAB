package com.npdeas.b1k3labapp.Database.Enuns;

public enum Deficiency {

    HEARING(0),
    LOCOMOTIVE(1),
    VISUAL(2);

    public int value;

    Deficiency(int value){
        this.value = value;
    }

    public boolean compare(int i){return value == i;}

    public static Deficiency getValue(int status)
    {
        Deficiency[] as = Deficiency.values();
        for(int i = 0; i < as.length; i++)
        {
            if(as[i].compare(status))
                return as[i];
        }
        return null;
    }

}

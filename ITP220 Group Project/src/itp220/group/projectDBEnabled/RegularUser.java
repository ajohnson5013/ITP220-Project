package itp220.group.projectDBEnabled;

//PK's version

import itp220.group.project.*;
//PK's version

public class RegularUser extends User
{
    // Right now we don't have any administrative duties, so 
    // RegularUser just delegates everything to super() and adds no
    // fields/methods of its own. --PK
    public RegularUser()
    {
            super();
    }

    public RegularUser(String name)
    {
            super(name);
    }
    
    public RegularUser(String name, String passHash)
    {
        super(name, passHash);
    }
    
    public RegularUser(String name, String passHash, int ID)
    {
            super(name, passHash, ID);
    }

    public RegularUser(String name, int ID)
    {
            super(name, ID);
    }
}

package itp220.group.projectDBEnabled;

//PK's version

import itp220.group.project.*;
import java.util.*;

// Simply used to compare different posts/threads (since a Thread ISA Post)
// by their ratings... --PK

public class PostDateComparator implements Comparator<Post>{
	public PostDateComparator(){}
	@Override
        public int compare(Post a, Post b)
	{
            return a.compareTo(b);
	}
}
